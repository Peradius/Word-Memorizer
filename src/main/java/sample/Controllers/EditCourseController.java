package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Course;
import sample.Data.Datasource;

public class EditCourseController {
    @FXML
    private TextField courseNameField;

    @FXML
    private TextField courseLanguageField;

    @FXML
    private TextField usersLanguageField;

    void populateFields(Course course) {
        courseNameField.setText(course.getCourseName());
        courseLanguageField.setText(course.getCourseLanguage());
        usersLanguageField.setText(course.getUsersLanguage());
    }

    void updateCourse(int courseID) {
        String newCourseName = courseNameField.getText().trim();
        String newCourseLanguage = courseLanguageField.getText().trim();
        String newUsersLanguage = usersLanguageField.getText().trim();

        if(newCourseName.equals("") || newCourseLanguage.equals("") || newUsersLanguage.equals("")) {
            System.out.println("No field can be empty!");
        } else {
            Datasource.getInstance().updateCourseInDB(courseID, newCourseName, newCourseLanguage, newUsersLanguage);
            System.out.println("Course " + newCourseName + " edited.");
        }


    }
}
