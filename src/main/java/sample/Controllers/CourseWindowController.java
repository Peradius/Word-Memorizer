package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Level;
import sample.Data.Word;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CourseWindowController {
    @FXML
    private BorderPane courseBorderPane;

    @FXML
    private ListView<Level> levelListView;

    @FXML
    private TableView<Word> courseTableView;

    @FXML
    private TableColumn<Word, Integer> columnID;

    @FXML
    private TableColumn<Word, String> columnForeign;

    @FXML
    private TableColumn<Word, String> columnTranslation;

//    ---
    
    private static final String WINDOWS_LOCATION = "/windows/";

    private static final String COURSE_OVERVIEW_WINDOW = WINDOWS_LOCATION + "courseOverviewWindow.fxml";
    private static final String COURSE_VOCABULARY_WINDOW = WINDOWS_LOCATION + "courseVocabularyWindow.fxml";
    private static final String ADD_NEW_LEVEL_WINDOW = WINDOWS_LOCATION + "addNewLevelWindow.fxml";
    private static final String EDIT_LEVEL_WINDOW = WINDOWS_LOCATION + "editLevelWindow.fxml";
    private static final String ADD_NEW_WORD_WINDOW = WINDOWS_LOCATION + "addNewWordWindow.fxml";
    private static final String BULK_ADD_WORDS_WINDOW = WINDOWS_LOCATION + "bulkAddWordsWindow.fxml";
    private static final String EDIT_WORD_WINDOW = WINDOWS_LOCATION + "editWordWindow.fxml";
    private static final String REVIEW_WINDOW = WINDOWS_LOCATION + "reviewWindow.fxml";

//    ---

    private Course currentCourse;
    private int courseID;
    private List<Level> levelList = new LinkedList<>();
    private List<Word> courseVocabulary = new LinkedList<>();

    public void setCourse(Course course) {
        this.currentCourse = course;
        this.courseID = course.getCourseID();
    }

    public void initialize() {
        levelListView.setCellFactory(e -> new ListCell<>() {
            @Override
            protected void updateItem(Level level, boolean empty) {
                super.updateItem(level, empty);

                if(empty || level == null) {
                    setText("");
                } else {
                    setText(level.getLevelName());
                }
            }
        });

        levelListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super Level>) (observableValue, o, selectedLevel) -> {
            if(selectedLevel != null) {
                System.out.println(selectedLevel.getLevelName() + " from " + currentCourse.getCourseName() + " selected!");
                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromLevel(selectedLevel.getIdLevel()));
            } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
        });

        courseTableView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super Word>) (observableValue, o, selectedWord) -> {
            if(selectedWord != null) {
                System.out.println(selectedWord.getForeignWord() + " selected.");
            } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
        });
    }

    @FXML
    public void showOverview() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(COURSE_OVERVIEW_WINDOW));

        loadWindow(fxmlLoader, "Course Overview Window");

        CourseOverviewController controller = fxmlLoader.getController();
        controller.populateFields(currentCourse);
        controller.disableLoadButton();
    }

    @FXML
    public void showVocabulary() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(COURSE_VOCABULARY_WINDOW));

        loadWindow(fxmlLoader, "Course Vocabulary Window");

        CourseVocabularyWindowController controller = fxmlLoader.getController();
        controller.setSelectedCourse(currentCourse);
        controller.processResults();
    }

    @FXML
    public void exitCourse() {
        System.out.println("Exit course button clicked.");

        Stage stage = (Stage)courseBorderPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void exitApplication() {
        Platform.exit();
    }

    @FXML
    public void addLevel() {
        System.out.println("Addlevel button clicked!");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(courseBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(ADD_NEW_LEVEL_WINDOW));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.setResizable(false);
        } catch(IOException e) {
            System.out.println("Failed loading \"addNewLevelWindow.fxml\"");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            AddNewLevelController controller = fxmlLoader.getController();
            int newLevelID = controller.processResults(courseID);
            refreshLevelList();
            selectLevel(newLevelID);
        }
    }

    @FXML
    public void editLevel() {
        System.out.println("Edit level button clicked");
        if(levelListView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(courseBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(EDIT_LEVEL_WINDOW));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.setResizable(false);
            } catch (IOException e) {
                System.out.println("Failed loading \"editLevelWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditLevelController controller = fxmlLoader.getController();
            Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
            controller.populateFields(selectedLevel);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int levelID = selectedLevel.getIdLevel();
                controller.updateLevel(levelID);

                refreshLevelList();
            }
        }
    }

    @FXML
    public void removeLevel() {
        System.out.println("Remove level button clicked!");

        if(levelListView.getSelectionModel().getSelectedItem() != null) {
            Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete a level");
            alert.setHeaderText("Do you really want to delete " + selectedLevel.getLevelName() + "?\n" +
                    "You will delete all the words and the level itself!");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                int levelID = selectedLevel.getIdLevel();
                Datasource.getInstance().deleteLevelFromDB(levelID);
                System.out.println("Level deleted!");
                refreshLevelList();
                selectLevel(getFirstLevel());
            }
        }
    }

    @FXML
    public void addWord() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(courseBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(ADD_NEW_WORD_WINDOW));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.setResizable(false);
        } catch(IOException e) {
            System.out.println("Failed loading \"addnewWordWindow.fxml\"");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
            int levelID = selectedLevel.getIdLevel();
            AddNewWordController controller = fxmlLoader.getController();
            controller.processResults(courseID, levelID);

            refreshVocabularyList();
            courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromLevel(levelID));
            selectLevel(selectedLevel);
            courseTableView.refresh();
        }
    }

    @FXML
    public void bulkAddWords() {
        Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
        if(selectedLevel != null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(BULK_ADD_WORDS_WINDOW));

            try {
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);

                BulkAddWordsController controller = fxmlLoader.getController();
                controller.setCourseID(courseID);
                controller.setLevelID(selectedLevel.getIdLevel());

                stage.showAndWait();
            } catch(IOException e) {
                System.out.println("Failed loading BULK ADD WORDS");
                e.printStackTrace();
            }


            refreshVocabularyList();
            selectLevel(selectedLevel);
        }
    }

    @FXML
    public void editWord() {
        System.out.println("Edit button clicked!");

        if(courseTableView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(courseBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(EDIT_WORD_WINDOW));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.setResizable(false);
            } catch (IOException e) {
                System.out.println("Failed loading \"editWordWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditWordController controller = fxmlLoader.getController();
            Word selectedWord = courseTableView.getSelectionModel().getSelectedItem();
            controller.populateFields(selectedWord);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
                int wordID = selectedWord.getIdWord();
                controller.updateWord(wordID);
                refreshVocabularyList();

                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromLevel(selectedLevel.getIdLevel()));
            }
        }
    }

    @FXML
    public void removeWord() {

        System.out.println("Remove word button clicked!");

        if(courseTableView.getSelectionModel().getSelectedItem() != null) {
            Word selectedWord = courseTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete a contact");
            alert.setHeaderText("Do you really want to delete " + selectedWord.getForeignWord() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
                int levelID = selectedLevel.getIdLevel();
                int id = selectedWord.getIdWord();
                Datasource.getInstance().removeWordFromDB(id);
                System.out.println("Word deleted!");
                refreshVocabularyList();
                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromLevel(levelID));
                selectLevel(selectedLevel);
            }
        }
    }

    @FXML
    public void reviewAllWords() {
        System.out.println("Review all words button clicked!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(REVIEW_WINDOW));

        loadWindow(fxmlLoader, "Review Window");

        ReviewWindowController controller = fxmlLoader.getController();
        controller.setReviewCourse(true);
        controller.setSessionID(courseID);
        controller.processReview();
    }

    @FXML
    public void reviewLevelWords() {
        System.out.println("Review level button clicked!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(REVIEW_WINDOW));

        loadWindow(fxmlLoader, "Review Window");

        ReviewWindowController controller = fxmlLoader.getController();
        Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();

        controller.setReviewCourse(false);
        controller.setSessionID(selectedLevel.getIdLevel());
        controller.processReview();
    }

    private void loadWindow(FXMLLoader fxmlLoader, String windowName) {
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading " + windowName);
            e.printStackTrace();
        }
    }

    void initialLoad() {
        courseVocabulary = Datasource.getInstance().getVocabularyFromCourse(courseID);

        columnID.setCellValueFactory(new PropertyValueFactory<>("idWord"));
        columnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignWord"));
        columnTranslation.setCellValueFactory(new PropertyValueFactory<>("translatedWord"));

        courseTableView.getItems().setAll(currentCourse.getVocabulary());

        refreshLevelList();
        Level firstLevel = getFirstLevel();
        selectLevel(firstLevel);

    }

    private void selectLevel(int levelID) {
        Level selectedLevel = new Level();
        for(Level level : levelList) {
            if(level.getIdLevel() == levelID) {
                selectedLevel = level;
                break;
            }
        }

        levelListView.getSelectionModel().select(selectedLevel);
        courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromLevel(levelID));
    }

    private void selectLevel(Level level) {
        levelListView.getSelectionModel().select(level);
        courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromLevel(level.getIdLevel()));
    }

    private void refreshLevelList() {
        levelList = Datasource.getInstance().getLevelsFromCourse(courseID);
        levelListView.getItems().setAll(levelList);
    }

    private void refreshVocabularyList() {
        courseVocabulary = Datasource.getInstance().getVocabularyFromCourse(courseID);
        currentCourse.setVocabulary(courseVocabulary);
    }

    private Level getFirstLevel() {
        Level firstLevel = levelList.get(0);

        for(Level levels: levelList) {
            if( levels.getIdLevel() < firstLevel.getIdLevel() ) {
                firstLevel = levels;
            }
        }
        return firstLevel;
    }
}
