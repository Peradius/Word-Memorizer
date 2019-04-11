package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;
import sample.Data.Level;
import sample.Data.Word;

public class EditLevelController {

    @FXML
    TextField levelNameField;

    void populateFields(Level level) {
        levelNameField.setText(level.getLevelName());
    }

    void updateLevel(int levelID) {
        String newLevelName = levelNameField.getText().trim();

        if(newLevelName.equals("")) {
            System.out.println("No field can be empty!");
        } else {
            Datasource.getInstance().updateLevelInDB(levelID, newLevelName);
            System.out.println("Level " + newLevelName + " edited.");
        }
    }
}
