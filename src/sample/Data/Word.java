package sample.Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Word {
    private SimpleIntegerProperty idWord;
    private SimpleStringProperty foreignWord;
    private SimpleStringProperty translatedWord;

    public Word() {
        this.idWord = new SimpleIntegerProperty();
        this.foreignWord = new SimpleStringProperty();
        this.translatedWord = new SimpleStringProperty();
    }

    public int getIdWord() {
        return idWord.get();
    }

    public void setIdWord(int idWord) {
        this.idWord.set(idWord);
    }

    public String getForeignWord() {
        return foreignWord.get();
    }

    public void setForeignWord(String foreignWord) {
        this.foreignWord.set(foreignWord);
    }

    public String getTranslatedWord() {
        return translatedWord.get();
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord.set(translatedWord);
    }
}

