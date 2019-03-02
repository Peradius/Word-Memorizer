package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Word;

public class AddNewWordController {

    @FXML
    TextField foreignWordField;

    @FXML
    TextField translatedWordField;

    public void processResults() {
        String foreignWord = foreignWordField.getText().trim();
        String translatedWord = translatedWordField.getText().trim();


        Datasource.getInstance().addNewWordToDB(foreignWord, translatedWord);

        System.out.println("Word " + foreignWord + " added.");

    }

}
