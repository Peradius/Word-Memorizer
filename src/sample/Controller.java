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
import sample.Data.Word;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Controller {
    private List<Course> coursesList;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    private TableView courseTableView;

    @FXML
    private ListView<Course> courseListView;

    @FXML
    private TableColumn<Word, Integer> columnID;

    @FXML
    private TableColumn<Word, String> columnEnglish;

    @FXML
    private TableColumn<Word, String> columnPolish;

    private Course selectedCourse;
    private int selectedCourseID;



    @FXML
    public void addWord() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
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
            controller.processResults(selectedCourseID);
            courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromCourse(selectedCourseID));
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
                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromCourse(selectedCourseID));
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
        controller.setCourseID(selectedCourseID);
        controller.processReview();

    }

    @FXML
    public void editWord() {
        System.out.println("Edit button clicked!");

        if(courseTableView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
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

                courseTableView.getItems().setAll(Datasource.getInstance().getVocabularyFromCourse(selectedCourseID));
            }
        }
    }

    @FXML
    public void addCourse() {
        System.out.println("Addcourse button clicked!");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewCourseWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Failed loading \"addNewCourseWindow.fxml\"");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            AddNewCourseController controller = fxmlLoader.getController();
            controller.processResults();
            refreshCourseList();
        }
    }

    @FXML
    public void editCourse() {
        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editCourseWindow.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Failed loading \"editWordWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditCourseController controller = fxmlLoader.getController();
            Course selectedCourse = (Course) courseListView.getSelectionModel().getSelectedItem();
            controller.populateFields(selectedCourse);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int courseID = selectedCourse.getCourseID();
                controller.updateCourse(courseID);

                refreshCourseList();
            }
        }
    }

    //ZAIMPLEMENTOWAĆ REMOVECOURSE!

    @FXML
    public void removeCourse() {
        System.out.println("Remove course button pressed. Function to be implemented!");
    }

    @FXML
    public void loadCourse() {
        System.out.println("Load course button pressed.");

        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            selectedCourse = courseListView.getSelectionModel().getSelectedItem();
            selectedCourseID = selectedCourse.getCourseID();

            List<Word> vocab = Datasource.getInstance().getVocabularyFromCourse(selectedCourseID);
            selectedCourse.setVocabulary(vocab);

            columnID.setCellValueFactory(new PropertyValueFactory<>("idWord"));
            columnEnglish.setCellValueFactory(new PropertyValueFactory<>("foreignWord"));
            columnPolish.setCellValueFactory(new PropertyValueFactory<>("translatedWord"));

            courseTableView.getItems().setAll(selectedCourse.getVocabulary());

        }
    }

    public void initialize() {

        refreshCourseList();

        courseListView.setCellFactory(e -> new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course course, boolean empty) {
                        super.updateItem(course, empty);

                        if(empty || course == null) {
                            setText("");
                        } else {
                            setText(course.getCourseName());
                        }
                    }
                });

        courseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1 != null) {
                    Course selectedCourse = (Course) t1;
                    System.out.println(selectedCourse.getCourseName() + " selected.");
                } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
            }
        });

        courseTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1 != null) {
                    Word selectedWord = (Word) t1;
                    System.out.println(selectedWord.getForeignWord() + " selected.");
                } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
            }
        });
    }

    public void refreshCourseList() {
        coursesList = Datasource.getInstance().getCourses();
        courseListView.getItems().setAll(coursesList);
    }






}
