package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Level;
import sample.Data.Word;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CourseWindowController {
    private List<Level> levelList;
    
    @FXML
    private BorderPane courseBorderPane;

    @FXML
    private ListView<Level> levelListView;

    @FXML
    private TableView courseTableView;

    @FXML
    private TableColumn<Word, Integer> columnID;

    @FXML
    private TableColumn<Word, String> columnEnglish;

    @FXML
    private TableColumn<Word, String> columnPolish;

    private Course currentCourse;
    private int courseID;

    public void setCourse(Course course) {
        this.currentCourse = course;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @FXML
    public void addLevel() {
        System.out.println("Addlevel button clicked!");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(courseBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewLevelWindow.fxml"));

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
            fxmlLoader.setLocation(getClass().getResource("editLevelWindow.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Failed loading \"editLevelWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditLevelController controller = fxmlLoader.getController();
            Level selectedLevel = (Level) levelListView.getSelectionModel().getSelectedItem();
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
    public void exitCourse() {
        System.out.println("Exit course button clicked. To be implemented");
    }

    @FXML
    public void addWord() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(courseBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewWordWindow.fxml"));

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
            AddNewWordController controller = fxmlLoader.getController();
            controller.processResults(courseID);
            courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromCourse(courseID));
            courseTableView.refresh();
        }
    }

    @FXML
    public void removeWord() {

        System.out.println("Remove word button clicked!");

        if(courseTableView.getSelectionModel().getSelectedItem() != null) {
            Word selectedWord = (Word)courseTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete a contact");
            alert.setHeaderText("Do you really want to delete " + selectedWord.getForeignWord() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                int id = selectedWord.getIdWord();
                Datasource.getInstance().removeWordFromDB(id);
                System.out.println("Word deleted!");
                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromCourse(courseID));
            }
        }
    }

    @FXML
    public void reviewWords() {
        System.out.println("Review button clicked!");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("reviewWindow.fxml"));
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
        controller.setCourseID(courseID);
        controller.processReview();

    }

    @FXML
    public void editWord() {
        System.out.println("Edit button clicked!");

        if(courseTableView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(courseBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editWordWindow.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Failed loading \"editWordWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditWordController controller = fxmlLoader.getController();
            Word selectedWord = (Word) courseTableView.getSelectionModel().getSelectedItem();
            controller.populateFields(selectedWord);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int wordID = selectedWord.getIdWord();
                controller.updateWord(wordID);

                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromCourse(courseID));
            }
        }
    }
    
    public void initialLoad() {
        List<Word> vocab = Datasource.getInstance().getVocabularyFromCourse(courseID);
        currentCourse.setVocabulary(vocab);

        columnID.setCellValueFactory(new PropertyValueFactory<>("idWord"));
        columnEnglish.setCellValueFactory(new PropertyValueFactory<>("foreignWord"));
        columnPolish.setCellValueFactory(new PropertyValueFactory<>("translatedWord"));

        courseTableView.getItems().setAll(currentCourse.getVocabulary());

    }
    
    
    
    public void initialize() {
        levelListView.getSelectionModel().selectedItemProperty().addListener(e -> new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1 != null) {
                    Level selectedLevel = (Level) t1;
                    System.out.println(selectedLevel.getLevelName() + " selected.");
                } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
            }
        });

        courseTableView.getSelectionModel().selectedItemProperty().addListener(e -> new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1 != null) {
                    Word selectedWord = (Word) t1;
                    System.out.println(selectedWord.getForeignWord() + " selected.");
                } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
            }
        });
    }

    public void refreshLevelList() {
        levelList = Datasource.getInstance().getLevelsFromCourse(courseID);
        levelListView.getItems().setAll(levelList);
    }
}
