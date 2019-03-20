package sample.GUI.Controllers;

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


    private Course currentCourse;
    private int courseID;
    private List<Level> levelList = new LinkedList<>();
    private List<Word> courseVocabulary = new LinkedList<>();

    public void setCourse(Course course) {
        this.currentCourse = course;
        courseID = course.getCourseID();
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
        fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/courseOverviewWindow.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading \"Course Overview Window\"");
            System.out.println(e.getMessage());
            return;
        }
        CourseOverviewController controller = fxmlLoader.getController();
        controller.populateFields(currentCourse);
    }

    @FXML
    public void showVocabulary() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/courseVocabularyWindow.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading \"Course Vocabulary Window\"");
            e.printStackTrace();
            return;
        }
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
        fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/addNewLevelWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
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
            controller.processResults(courseID);
            refreshLevelList();
        }
    }

    @FXML
    public void editLevel() {
        System.out.println("Edit level button clicked");
        if(levelListView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(courseBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/editLevelWindow.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
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
            }
        }
    }

    @FXML
    public void addWord() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(courseBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/addNewWordWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
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
            levelListView.getSelectionModel().select(selectedLevel);
            courseTableView.refresh();
        }
    }

    @FXML
    public void bulkAddWords() {
        Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();
        if(selectedLevel != null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/bulkAddWordsWindow.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch(IOException e) {
                System.out.println("Failed loading \"Bulk Add Words Window\"");
                System.out.println(e.getMessage());
                return;
            }
            BulkAddWordsController controller = fxmlLoader.getController();
            controller.setCourseID(courseID);
            controller.setLevelID(selectedLevel.getIdLevel());
            refreshVocabularyList();
        }
    }

    @FXML
    public void editWord() {
        System.out.println("Edit button clicked!");

        if(courseTableView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(courseBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/editWordWindow.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
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
                levelListView.getSelectionModel().select(selectedLevel);
            }
        }
    }

    @FXML
    public void reviewAllWords() {
        System.out.println("Review all words button clicked!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/reviewWindow.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Review Session");
            stage.setScene(scene);
            stage.initOwner(courseBorderPane.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading \"reviewWindow\"");
            return;
        }

        ReviewWindowController controller = fxmlLoader.getController();
        controller.setReviewCourse(true);
        controller.setSessionID(courseID);
        controller.processReview();
    }

    @FXML
    public void reviewLevelWords() {
        System.out.println("Review level button clicked!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/GUI/Windows/reviewWindow.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Review Session");
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading \"reviewWindow\"");
            return;
        }

        ReviewWindowController controller = fxmlLoader.getController();
        Level selectedLevel = levelListView.getSelectionModel().getSelectedItem();

        controller.setReviewCourse(false);
        controller.setSessionID(selectedLevel.getIdLevel());
        controller.processReview();
    }

    void initialLoad() {
        courseVocabulary = Datasource.getInstance().getVocabularyFromCourse(courseID);

        columnID.setCellValueFactory(new PropertyValueFactory<>("idWord"));
        columnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignWord"));
        columnTranslation.setCellValueFactory(new PropertyValueFactory<>("translatedWord"));

        courseTableView.getItems().setAll(currentCourse.getVocabulary());

        refreshLevelList();
        Level firstLevel = getFirstLevel();
        levelListView.getSelectionModel().select(firstLevel);

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
