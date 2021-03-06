package sample.Controllers;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;

public class AddNewWordController {

    @FXML
    TextField foreignWordField;

    @FXML
    TextField translatedWordField;

    public void processResults(int courseID, int levelID) {
        String foreignWord = foreignWordField.getText().trim();
        String translatedWord = translatedWordField.getText().trim();

        if(foreignWord.equals("") || translatedWord.equals("")) {
            System.out.println("No field can be empty!");
        } else {
            Datasource.getInstance().addNewWordToLevel(courseID, levelID, foreignWord, translatedWord);
            System.out.println("Word " + foreignWord + " added.");
        }
    }
}
