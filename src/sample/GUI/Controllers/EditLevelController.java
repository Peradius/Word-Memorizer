package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;
import sample.Data.Level;
import sample.Data.Word;

public class EditLevelController {

    @FXML
    TextField levelNameField;

    public void populateFields(Level level) {
        levelNameField.setText(level.getLevelName());
    }

    public void updateLevel(int levelID) {
        String newLevelName = levelNameField.getText().trim();
        Datasource.getInstance().updateLevelInDB(levelID, newLevelName);
    }
}
