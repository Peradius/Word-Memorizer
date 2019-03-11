package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Word;

public class EditCourseController {
    @FXML
    private TextField courseNameField;

    @FXML
    private TextField courseLanguageField;

    @FXML
    private TextField usersLanguageField;

    public void populateFields(Course course) {
        courseNameField.setText(course.getCourseName());
        courseLanguageField.setText(course.getCourseLanguage());
        usersLanguageField.setText(course.getUsersLanguage());
    }

    public void updateCourse(int courseID) {
        String newCourseName = courseNameField.getText().trim();
        String newCourseLanguage = courseLanguageField.getText().trim();
        String newUsersLanguage = usersLanguageField.getText().trim();

        Datasource.getInstance().updateCourseInDB(courseID, newCourseName, newCourseLanguage, newUsersLanguage);
    }
}
