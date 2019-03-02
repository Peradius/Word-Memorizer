package sample.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Course {
    private String courseName;
    private String teachingLanguage;
    private String courseLanguage;
    private List<Word> vocabulary;
    private Datasource datasource; // database
    private Course instance = new Course();


    public Course(String courseName, String teachingLanguage, String courseLanguage, Datasource datasource) {
        this.courseName = courseName;
        this.teachingLanguage = teachingLanguage;
        this.courseLanguage = courseLanguage;
        this.datasource = datasource;
        this.vocabulary = new LinkedList<>();
    }

    public Course getInstance() {
        return instance;
    }

    private Course() {}

    public void addNewWord(Word word) {
        vocabulary.add(word);
    }

    public void loadVocabulary() {
        this.vocabulary = datasource.queryVocabulary();
    }


}
