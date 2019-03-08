package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;

public class AddNewLevelController {
    @FXML
    TextField levelNameField;

    public void processResults(int courseID) {
        String levelName = levelNameField.getText().trim();

        Datasource.getInstance().addNewLevelToCourse(courseID, levelName);

        System.out.println("Level " + levelName + " added.");

    }
}
