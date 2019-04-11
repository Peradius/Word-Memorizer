package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;

public class AddNewLevelController {
    @FXML
    TextField levelNameField;

    public int processResults(int courseID) {
        String levelName = levelNameField.getText().trim();

        if(levelName.equals("")) {
            System.out.println("No field can be empty!");
            return -1;
        } else {
            int newLevelID = Datasource.getInstance().addNewLevelToCourse(courseID, levelName);

            System.out.println("Level " + levelName + " added.");
            return newLevelID;
        }
    }
}
