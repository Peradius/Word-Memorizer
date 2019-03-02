package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;

public class AddNewCourseController {

    @FXML
    private TextField courseNameField;

    @FXML
    private TextField databaseNameField;

    @FXML
    private TextField teachingLanguageField;

    @FXML
    private TextField courseLangaugeField;

    public void processResults() {
        String courseName= courseNameField.getText().trim();
        String databaseName= databaseNameField.getText().trim();
        databaseName = databaseName.replaceAll(" ", "");
        String teachingLanguage = teachingLanguageField.getText().trim();
        String courseLanguage = courseLangaugeField.getText().trim();


//        Datasource.getInstance().addNewWordToDB(foreignWord, translatedWord);

    }


}
