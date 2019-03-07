package sample.Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Level {
    private SimpleIntegerProperty idLevel;
    private SimpleStringProperty levelName;

    public Level() {
        this.idLevel = new SimpleIntegerProperty();
        this.levelName = new SimpleStringProperty();
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
}
