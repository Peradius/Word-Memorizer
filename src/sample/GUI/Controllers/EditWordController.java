package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.Data.Datasource;
import sample.Data.Word;

public class EditWordController {

    @FXML
    TextField foreignWordField;

    @FXML
    TextField translatedWordField;

    public void populateFields(Word word) {
        foreignWordField.setText(word.getForeignWord());
        translatedWordField.setText(word.getTranslatedWord());
    }

    public void updateWord(int id) {
        String newWordValue = foreignWordField.getText().trim();
        String newTranslationValue = translatedWordField.getText().trim();
        Datasource.getInstance().updateWordInDB(id, newWordValue, newTranslationValue);
    }
}
