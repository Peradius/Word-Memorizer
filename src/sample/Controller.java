package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Word;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Controller {
    private List<Datasource> coursesList;
    private ObservableList<ObservableList> data;

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
            controller.processResults();
            courseTableView.getItems().setAll(Datasource.getInstance().queryVocabulary());

        }
    }



    @FXML
    public void removeWord() {
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
                courseTableView.getItems().setAll(Datasource.getInstance().queryVocabulary());

            }
        }
    }

    @FXML
    public void reviewWords() {
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
        controller.processReview();

    }

    @FXML
    public void editWord() {
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

                courseTableView.getItems().setAll(Datasource.getInstance().queryVocabulary());
            }
        }
    }

    @FXML
    public void addCourse() {
        System.out.println("Add course button pressed. Function to be implemented!");
    }

    @FXML
    public void editCourse() {
        System.out.println("Edit course button pressed. Function to be implemented!");
    }

    @FXML
    public void removeCourse() {
        System.out.println("Remove course button pressed. Function to be implemented!");
    }

    public void initialize() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("idWord"));
        columnEnglish.setCellValueFactory(new PropertyValueFactory<>("foreignWord"));
        columnPolish.setCellValueFactory(new PropertyValueFactory<>("translatedWord"));

        courseTableView.getItems().setAll(Datasource.getInstance().queryVocabulary());

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






}
