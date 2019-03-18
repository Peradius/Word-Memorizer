package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;

public class AddNewLevelController {
    @FXML
    TextField levelNameField;

    public int processResults(int courseID) {
        String levelName = levelNameField.getText().trim();

        int newLevelID = Datasource.getInstance().addNewLevelToCourse(courseID, levelName);

        System.out.println("Level " + levelName + " added.");
        return newLevelID;
    }
}
