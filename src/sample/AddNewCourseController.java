package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;

import java.sql.Statement;

public class AddNewCourseController {

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField teachingLanguageField;

    @FXML
    private TextField courseLanguageField;

    public void processResults() {
        String courseName= courseNameField.getText().trim();
        String teachingLanguage = teachingLanguageField.getText().trim();
        String courseLanguage = courseLanguageField.getText().trim();

        Datasource.getInstance().addNewCourse(courseName, teachingLanguage, courseLanguage);
        System.out.println("Course " + courseName + " added.");
    }
}
