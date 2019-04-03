package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Level;

import java.io.IOException;

public class CourseOverviewController {
    private static final String WINDOWS_LOCATION = "/sample/GUI/Windows/";
    private static final String COURSE_WINDOW = WINDOWS_LOCATION + "courseWindow.fxml";

    private Course course;

    public void setCourse(Course course) {
        this.course = course;
    }

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

    @FXML
    public void loadCourse() {
        System.out.println("Load course button pressed.");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(COURSE_WINDOW));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle(course.getCourseName());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading \"course Window\"");
            System.out.println(e.getMessage());
            return;
        }

        CourseWindowController controller = fxmlLoader.getController();
        controller.setCourse(course);
        controller.initialLoad();
    }

    
    public void populateFields(Course course) {
        Integer levelsInt = Datasource.getInstance().getLevelsCountFromCourse(course);
        String levelsCount = levelsInt.toString();

        Integer wordsInt = Datasource.getInstance().getWordsCountFromCourse(course);
        String wordsCount = wordsInt.toString();

        setCourse(course);

        courseNameField.setText(course.getCourseName());
        teachingLanguageField.setText(course.getCourseLanguage());
        usersLanguageField.setText(course.getUsersLanguage());
        levelsField.setText(levelsCount);
        wordsField.setText(wordsCount);
    }

    public void disableAllButtons() {
        closeButton.setVisible(false);
    }
}
