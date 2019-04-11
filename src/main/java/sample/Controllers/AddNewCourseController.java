package sample.Controllers;

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

    public int processResults() {
        String courseName= courseNameField.getText().trim();
        String teachingLanguage = teachingLanguageField.getText().trim();
        String courseLanguage = courseLanguageField.getText().trim();

        if(courseName.equals("") || teachingLanguage.equals("") || courseLanguage.equals("")) {
            System.out.println("No field can be empty!");
            return -1;
        } else {
            int newCourseID = Datasource.getInstance().addNewCourse(courseName, teachingLanguage, courseLanguage);
            System.out.println("Course " + courseName + " added.");
            return newCourseID;
        }
    }
}
