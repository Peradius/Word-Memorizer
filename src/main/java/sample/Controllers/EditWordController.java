package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;
import sample.Data.Word;

public class EditWordController {

    @FXML
    TextField foreignWordField;

    @FXML
    TextField translatedWordField;

    void populateFields(Word word) {
        foreignWordField.setText(word.getForeignWord());
        translatedWordField.setText(word.getTranslatedWord());
    }

    void updateWord(int id) {
        String newWordValue = foreignWordField.getText().trim();
        String newTranslationValue = translatedWordField.getText().trim();

        if(newWordValue.equals("") || newTranslationValue.equals("")) {
            System.out.println("No field can be empty!");
        } else {
            Datasource.getInstance().updateWordInDB(id, newWordValue, newTranslationValue);
            System.out.println("Word " + newWordValue + " edited.");
        }
    }
}
