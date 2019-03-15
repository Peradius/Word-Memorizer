package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Level;

public class CourseOverviewController {
    
    @FXML
    private Label courseNameField;

    @FXML
    private Label teachingLanguageField;

    @FXML
    private Label usersLanguageField;

    @FXML
    private Label levelsField;

    @FXML
    private Label wordsField;

    @FXML
    private Button closeButton;

    @FXML
    public void closeWindow() {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }
    
    public void populateFields(Course course) {
        Integer levelsInt = course.getLevelsCount();
        String levelsCount = levelsInt.toString();

        Integer wordsInt = course.getWordsCount();
        String wordsCount = wordsInt.toString();

        courseNameField.setText(course.getCourseName());
        teachingLanguageField.setText(course.getCourseName());
        usersLanguageField.setText(course.getUsersLanguage());
        levelsField.setText(levelsCount);
        wordsField.setText(wordsCount);
    }
}
