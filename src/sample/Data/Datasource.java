package sample.Data;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Datasource {

    // Constants required for connection with the database

    public static final String DB_NAME = "vocabTestDatabase.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Programowanie\\Projekty\\courses\\" + DB_NAME;

    // List of all the constants used in the database

    public static final String TABLE_COURSES = "Courses";
    public static final String COLUMN_COURSES_ID = "ID_Course";
    public static final String COLUMN_COURSES_NAME = "Course_Name";
    public static final String COLUMN_COURSES_COURSELANGUAGE = "Course_Language";
    public static final String COLUMN_COURSES_USERSLANGUAGE = "Users_Language";
    public static final String COLUMN_COURSES_LEVELS = "Levels";
    public static final String COLUMN_COURSES_WORDS = "Words";
    public static final int INDEX_COURSES_ID = 1;
    public static final int INDEX_COURSES_NAME = 2;
    public static final int INDEX_COURSES_COURSELANGUAGE = 3;
    public static final int INDEX_COURSES_USERSLANGUAGE = 4;
    public static final int INDEX_COURSES_LEVELS = 5;
    public static final int INDEX_COURSES_WORDS = 6;

    public static final String TABLE_LEVELS = "Levels";
    public static final String COLUMN_LEVELS_ID = "ID_Level";
    public static final String COLUMN_LEVELS_IDCOURSE = "ID_Course";
    public static final String COLUMN_LEVELS_NAME = "Level_Name";
    public static final String COLUMN_LEVELS_WORDS = "Words";
    public static final int INDEX_LEVELS_ID = 1;
    public static final int INDEX_LEVELS_IDCOURSE = 2;
    public static final int INDEX_LEVELS_NAME = 3;
    public static final int INDEX_LEVELS_WORDS = 4;

    public static final String TABLE_WORDS = "Words";
    public static final String COLUMN_WORDS_ID = "ID_Word";
    public static final String COLUMN_WORDS_IDCOURSE = "ID_Course";
    public static final String COLUMN_WORDS_IDLEVEL = "ID_Level";
    public static final String COLUMN_WORDS_FOREIGN = "Foreign_Word";
    public static final String COLUMN_WORDS_TRANSLATED = "Translated_Word";
    public static final int INDEX_WORDS_ID = 1;
    public static final int INDEX_WORDS_IDCOURSE = 2;
    public static final int INDEX_WORDS_IDLEVEL = 3;
    public static final int INDEX_WORDS_FOREIGN = 4;
    public static final int INDEX_WORDS_TRANSLATED = 5;

    // List of all the necessary queries used in this project
    // The comment below every constant represents its SQL form

    public static final String ADD_NEW_COURSE = "INSERT INTO " + TABLE_COURSES + '('
            + COLUMN_COURSES_NAME + ", " + COLUMN_COURSES_COURSELANGUAGE + ", " + COLUMN_COURSES_USERSLANGUAGE + ") VALUES(?, ?, ?)";
    // INSERT INTO Courses(Course_Name, Course_Language, Users_Language) VALUES ("?", "?", "?");

    public static final String ADD_NEW_LEVEL = "INSERT INTO " + TABLE_LEVELS + '('
            + COLUMN_LEVELS_IDCOURSE + ", " + COLUMN_LEVELS_NAME + ") VALUES(?, ?)";
    //INSERT INTO LEVELS(ID_Course, Level_Name) values (?, "?");

    public static final String ADD_NEW_WORD = "INSERT INTO " + TABLE_WORDS + '('
            + COLUMN_WORDS_IDCOURSE + ", " + COLUMN_WORDS_IDLEVEL + ", " + COLUMN_WORDS_FOREIGN + ", " + COLUMN_WORDS_TRANSLATED + ") VALUES(?, ?, ?, ?)";
    // INSERT INTO Words(ID_Course, ID_Level, Foreign_Word, Translated_Word) VALUES (?, ?, "?", "?");

    public static final String QUERY_COURSES = "SELECT * FROM " + TABLE_COURSES;
    // SELECT * FROM Courses;

    public static final String QUERY_VOCAB_FROM_COURSE = "SELECT * FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDCOURSE + " = ?";
    // SELECT * FROM Words WHERE ID_Course = ?;

    public static final String QUERY_VOCAB_FROM_LEVEL = "SELECT * FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDLEVEL + " = ?";
    // SELECT * FROM Words WHERE ID_Level = ?;

    public static final String QUERY_LEVELS_FROM_COURSE = "SELECT * FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_IDCOURSE + " = ?";
    // SELECT * FROM Levels WHERE ID_Course = ?;

    public static final String UPDATE_WORD = "UPDATE " + TABLE_WORDS + " SET "
            + COLUMN_WORDS_FOREIGN + " = ?, " + COLUMN_WORDS_TRANSLATED + " = ? WHERE " + COLUMN_WORDS_ID + " = ?";
    // UPDATE Words SET Foreign_Word = "?", Translated_Word = "?" WHERE ID_Word = "?"

    public static final String DELETE_WORD = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_ID + " = ?";
    // DELETE FROM Words WHERE ID_Word = "?"




//4. edytuj kurs (nazwa kursu)
//5. edytuj poziom (nazwa poziomu)





    // List of all the necessary PreparedStatements used in this project

    private PreparedStatement addNewCourse;
    private PreparedStatement addNewLevel;
    private PreparedStatement addNewWord;
    private PreparedStatement queryVocabularyFromCourse;
    private PreparedStatement queryVocabularyFromLevel;
    private PreparedStatement queryLevelsFromCourse;
    private PreparedStatement updateWord;
    private PreparedStatement deleteWord;
    private PreparedStatement queryCourses;



    // To be commented !!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1!!!!!!!!!!!!!1

     // w datasourcie mam statementsy, funkcje (metody), a w comtrollerze wywoluje te metody wraz z parmaetrami w nawiasach

    private Connection connection;
    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            addNewCourse = connection.prepareStatement(ADD_NEW_COURSE, Statement.RETURN_GENERATED_KEYS);
            addNewLevel = connection.prepareStatement(ADD_NEW_LEVEL, Statement.RETURN_GENERATED_KEYS);
            addNewWord = connection.prepareStatement(ADD_NEW_WORD, Statement.RETURN_GENERATED_KEYS);
            queryVocabularyFromCourse = connection.prepareStatement(QUERY_VOCAB_FROM_COURSE);
            queryVocabularyFromLevel = connection.prepareStatement(QUERY_VOCAB_FROM_LEVEL);
            queryLevelsFromCourse = connection.prepareStatement(QUERY_LEVELS_FROM_COURSE);
            updateWord = connection.prepareStatement(UPDATE_WORD);
            deleteWord = connection.prepareStatement(DELETE_WORD);
            queryCourses = connection.prepareStatement(QUERY_COURSES);

            return true;
        } catch (SQLException e) {
            System.out.println("Connection to the database failed!");
            e.printStackTrace();
            return false;
        }
    }

    public static Datasource getInstance() {
        return instance;
    }

    public static void printTrace() {
        System.out.println(CONNECTION_STRING);
    }

    public List<Course> getCourses() {
        try {
            List<Course> courseList = new LinkedList<>();
            ResultSet results = queryCourses.executeQuery();

            while(results.next()) {
                int courseID = results.getInt(INDEX_COURSES_ID);
                String courseName = results.getString(INDEX_COURSES_NAME);
                String courseLanguage = results.getString(INDEX_COURSES_COURSELANGUAGE);
                String usersLanguage = results.getString(INDEX_COURSES_USERSLANGUAGE);

                Course newCourse = new Course();
                newCourse.setCourseID(courseID);
                newCourse.setCourseName(courseName);
                newCourse.setCourseLanguage(courseLanguage);
                newCourse.setUsersLanguage(usersLanguage);

                courseList.add(newCourse);
            }

            return courseList;
        } catch (SQLException e) {
            System.out.println("Error getting courses!");
            e.printStackTrace();
            return null;
        }
    }

    public List<Word> getVocabularyFromCourse(int courseID) {
        try {
            List<Word> vocabulary = new LinkedList<>();
            queryVocabularyFromCourse.setInt(1, courseID);
            ResultSet results = queryVocabularyFromCourse.executeQuery();

            while (results.next()) {
                int idWord = results.getInt(INDEX_WORDS_ID);
                String foreignWord = results.getString(INDEX_WORDS_FOREIGN);
                String translatedWord = results.getString(INDEX_WORDS_TRANSLATED);

                Word newWord = new Word();
                newWord.setIdWord(idWord);
                newWord.setForeignWord(foreignWord);
                newWord.setTranslatedWord(translatedWord);

                vocabulary.add(newWord);
            }

            return vocabulary;

        } catch (SQLException e) {
            System.out.println("Error while getting vocabulary from the database! \n");
            e.getMessage();
            return null;
        }
    }

    public List<Word> getVocabularyFromLevel(int levelID) {
        try {
            List<Word> vocabulary = new LinkedList<>();
            queryVocabularyFromLevel.setInt(1, levelID);
            ResultSet results = queryLevelsFromCourse.executeQuery();

            while (results.next()) {
                int idWord = results.getInt(INDEX_WORDS_ID);
                String foreignWord = results.getString(INDEX_WORDS_FOREIGN);
                String translatedWord = results.getString(INDEX_WORDS_TRANSLATED);

                Word newWord = new Word();
                newWord.setIdWord(idWord);
                newWord.setForeignWord(foreignWord);
                newWord.setTranslatedWord(translatedWord);

                vocabulary.add(newWord);
            }

            return vocabulary;

        } catch (SQLException e) {
            System.out.println("Error while getting vocabulary from the database! \n");
            e.printStackTrace();
            return null;
        }
    }

    public void addNewWordToLevel(int courseID, int levelID, String wordValue, String translationValue) {
        try {
            connection.close();

            connection.setAutoCommit(false);

            addNewWord.setInt(1, courseID);
            addNewWord.setInt(2, levelID);
            addNewWord.setString(3, wordValue);
            addNewWord.setString(4, translationValue);

            int affectedRows = addNewWord.executeUpdate();

            if(affectedRows == 1) {
                connection.commit();
            } else {
                throw new SQLException("Failed to add a new word!");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding new word to the level! \n");
            e.printStackTrace();
            return;
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Failed restoring autoCommit to true!");
                e.getMessage();
            }
        }
    }














    public void updateWordInDB(int wordID, String newWordValue, String newTranslationValue) {
        try {
            updateWord.setString(1, newWordValue);
            updateWord.setString(2, newTranslationValue);
            updateWord.setInt(3, wordID);

            updateWord.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating a word in the database!");
            e.printStackTrace();
            return;
        }
    }

    public void removeWordFromDB(int wordID) {
        try {
            deleteWord.setInt(1, wordID);

            deleteWord.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error deleting record from database");
            e.printStackTrace();
            return;
        }
    }

    public void close() {
        try {
            if(addNewCourse != null) {
                addNewCourse.close();
            }
            if(addNewLevel != null) {
                addNewLevel.close();
            }
            if(addNewWord != null) {
                addNewWord.close();
            }
            if(queryVocabularyFromCourse != null) {
                queryLevelsFromCourse.close();
            }
            if(queryVocabularyFromLevel != null) {
                queryVocabularyFromLevel.close();
            }
            if(queryLevelsFromCourse != null) {
                queryLevelsFromCourse.close();
            }
            if(updateWord != null) {
                updateWord.close();
            }
            if(deleteWord != null) {
                deleteWord.close();
            }
            if(queryCourses != null) {
                queryCourses.close();
            }

            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Closing the database failed!");
            e.printStackTrace();
        }
    }
}
