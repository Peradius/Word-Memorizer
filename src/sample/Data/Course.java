package sample.Data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;

public class Course {
    private SimpleIntegerProperty courseID;
    private SimpleStringProperty courseName;
    private SimpleStringProperty courseLanguage;
    private SimpleStringProperty usersLanguage;
    private int levelsCount;
    private int wordsCount;

    private List<Level> levels;
    private List<Word> vocabulary;


    public Course() {
        this.courseID = new SimpleIntegerProperty();
        this.courseName = new SimpleStringProperty();
        this.courseLanguage = new SimpleStringProperty();
        this.usersLanguage = new SimpleStringProperty();
        this.levels = new LinkedList<>();
        this.vocabulary = new LinkedList<>();
    }

    public int getCourseID() {
        return courseID.get();
    }

    public void setCourseID(int courseID) {
        this.courseID.set(courseID);
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public String getCourseLanguage() {
        return courseLanguage.get();
    }

    public void setCourseLanguage(String courseLanguage) {
        this.courseLanguage.set(courseLanguage);
    }

    public String getUsersLanguage() {
        return usersLanguage.get();
    }

    public void setUsersLanguage(String usersLanguage) {
        this.usersLanguage.set(usersLanguage);
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public int getLevelsCount() {
        return levels.size();
    }

    public List<Word> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<Word> vocabulary) {
        this.vocabulary = vocabulary;
    }

    public int getWordsCount() {
        return vocabulary.size();
    }

    public void incrementLevelsCount(int val) {
        this.levelsCount += val;
    }

    public void decrementLevelsCount(int val) {
        this.levelsCount -= val;
    }

    public void incrementWordsCount(int val) {
        this.wordsCount += val;
    }

    public void decrementWordsCount(int val) {
        this.wordsCount -= val;
    }
}
