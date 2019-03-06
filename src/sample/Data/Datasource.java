package sample.Data;

import java.sql.*;
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

    public static final String DELETE_COURSE = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDCOURSE + " = ?;"
            + " DELETE FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_IDCOURSE + " = ?;"
            + " DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSES_ID + " = ?";
    //DELETE FROM Words WHERE ID_Course = 5;
    //DELETE FROM Levels WHERE ID_Course = 5;
    //DELETE FROM Courses WHERE ID_Course = 5;

    public static final String DELETE_LEVEL_FROM_COURSE = "DELETE FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_ID + " = ?";
    // DELETE FROM Levels WHERE ID_Level = "?"

    public static final String DELETE_WORD = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_ID + " = ?";
    // DELETE FROM Words WHERE ID_Word = "?"

    public static final String UPDATE_COURSE = "UPDATE " + TABLE_COURSES + " SET "
            + COLUMN_COURSES_NAME + " = ?, " + COLUMN_COURSES_COURSELANGUAGE + " = ?, " + COLUMN_COURSES_USERSLANGUAGE + " = ? WHERE "
            + COLUMN_COURSES_ID + " = ?";
    // UPDATE Courses SET Course_Name = ?, Course_Language = ?, Users_Language = ? WHERE ID_Course = ?

    // List of all the necessary PreparedStatements used in this project

    private PreparedStatement addNewCourse;
    private PreparedStatement addNewLevel;
    private PreparedStatement addNewWord;
    private PreparedStatement queryVocabularyFromCourse;
    private PreparedStatement queryVocabularyFromLevel;
    private PreparedStatement queryLevelsFromCourse;
    private PreparedStatement updateWord;
    private PreparedStatement updateCourse;
    private PreparedStatement deleteCourse;
    private PreparedStatement deleteLevelFromCourse;
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
            updateCourse = connection.prepareStatement(UPDATE_COURSE);
            deleteCourse = connection.prepareStatement(DELETE_COURSE);
            deleteLevelFromCourse = connection.prepareStatement(DELETE_LEVEL_FROM_COURSE);
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

    public List<Course> getCourses(){
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
//        try {
//            List<Word> vocabulary = new LinkedList<>();
//            queryVocabularyFromLevel.setInt(1, levelID);
//            ResultSet results = queryLevelsFromCourse.executeQuery();
//
//            while (results.next()) {
//                int idWord = results.getInt(INDEX_WORDS_ID);
//                String foreignWord = results.getString(INDEX_WORDS_FOREIGN);
//                String translatedWord = results.getString(INDEX_WORDS_TRANSLATED);
//
//                Word newWord = new Word();
//                newWord.setIdWord(idWord);
//                newWord.setForeignWord(foreignWord);
//                newWord.setTranslatedWord(translatedWord);
//
//                vocabulary.add(newWord);
//            }
//
//            return vocabulary;
//
//        } catch (SQLException e) {
//            System.out.println("Error while getting vocabulary from the database! \n");
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    public void addNewCourse(String courseName, String teachingLanguage, String usersLanguage) {
        try {
            connection.setAutoCommit(false);

            addNewCourse.setString(1, courseName);
            addNewCourse.setString(2, teachingLanguage);
            addNewCourse.setString(3, usersLanguage);

            int affectedRows = addNewCourse.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Course " + courseName + " sucessfully added to the database!");
                connection.commit();
            } else {
                throw new SQLException("Failed adding new course!");
            }
        } catch (Exception e) {
            System.out.println("Insert new course exception: " + e.getMessage());
            try {
                System.out.println("Performing a rollback!");
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Rollback failed! " + e1.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    public void updateCourseInDB(int courseID, String newCourseName, String newCourseLanguage, String newUsersLanguage) {
        try {
            connection.setAutoCommit(false);

            updateCourse.setString(1, newCourseName);
            updateCourse.setString(2, newCourseLanguage);
            updateCourse.setString(3, newUsersLanguage);
            updateCourse.setInt(4, courseID);

            updateCourse.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update course exception: " + e.getMessage());
            try {
                System.out.println("Performing a rollback!");
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Rollback failed! " + e1.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    public void deleteCourseFromDB(int courseID) {
        try {
            connection.setAutoCommit(false);

            deleteCourse.setInt(1, courseID);
            deleteCourse.setInt(2, courseID);
            deleteCourse.setInt(3, courseID);

            if(deleteCourse.execute()) {
                System.out.println("Course with ID " + courseID + " sucessfully deleted from the database!");
                connection.commit();
            } else {
                throw new SQLException("Failed deleting course!");
            }
        } catch (Exception e) {
            System.out.println("Delete course exception: " + e.getMessage());
            try {
                System.out.println("Performing a rollback!");
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Rollback failed! " + e1.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    public void addNewWordToLevel(int courseID, int levelID, String wordValue, String translationValue) {
        try {
            connection.setAutoCommit(false);

            addNewWord.setInt(1, courseID);
            addNewWord.setInt(2, levelID);
            addNewWord.setString(3, wordValue);
            addNewWord.setString(4, translationValue);

            int affectedRows = addNewWord.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Word " + wordValue + " sucessfully added to the database!");
                connection.commit();
            } else {
                throw new SQLException("Failed adding new word!");
            }
        } catch (Exception e) {
            System.out.println("Insert new word exception: " + e.getMessage());
            try {
                System.out.println("Performing a rollback!");
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Rollback failed! " + e1.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    public void updateWordInDB(int wordID, String newWordValue, String newTranslationValue) {
        try {
            connection.setAutoCommit(false);

            updateWord.setString(1, newWordValue);
            updateWord.setString(2, newTranslationValue);
            updateWord.setInt(3, wordID);

            int affectedRows = updateWord.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Word " + newWordValue + " sucessfully updated in the database!");
                connection.commit();
            } else {
                throw new SQLException("Failed updating word!");
            }
        } catch (Exception e) {
            System.out.println("Update word exception: " + e.getMessage());
            try {
                System.out.println("Performing a rollback!");
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Rollback failed! " + e1.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    public void removeWordFromDB(int wordID) {
        try {
            connection.setAutoCommit(false);

            deleteWord.setInt(1, wordID);

            int affectedRows = deleteWord.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Word ID=" + wordID + " sucessfully deleted from the database!");
                connection.commit();
            } else {
                throw new SQLException("Failed deleting word!");
            }
        } catch (Exception e) {
            System.out.println("Delete word exception: " + e.getMessage());
            try {
                System.out.println("Performing a rollback!");
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Rollback failed! " + e1.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
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
            if(updateCourse != null) {
                updateCourse.close();
            }
            if(deleteCourse != null) {
                deleteCourse.close();
            }
            if(deleteLevelFromCourse != null) {
                deleteLevelFromCourse.close();
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
