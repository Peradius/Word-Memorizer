package sample.Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Level {
    private SimpleIntegerProperty idLevel;
    private SimpleIntegerProperty idCourse;
    private SimpleStringProperty levelName;
    private SimpleIntegerProperty wordsCount;

    public Level() {
        this.idLevel = new SimpleIntegerProperty();
        this.idCourse = new SimpleIntegerProperty();
        this.levelName = new SimpleStringProperty();
        this.wordsCount = new SimpleIntegerProperty();
    }

    public int getIdLevel() {
        return idLevel.get();
    }

    public void setIdLevel(int idLevel) {
        this.idLevel.set(idLevel);
    }

    public String getLevelName() {
        return levelName.get();
    }

    public void setLevelName(String levelName) {
        this.levelName.set(levelName);
    }

    public int getIdCourse() {
        return idCourse.get();
    }

    public void setIdCourse(int idCourse) {
        this.idCourse.set(idCourse);
    }

    public int getWordsCount() {
        return wordsCount.get();
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount.set(wordsCount);
    }
}
