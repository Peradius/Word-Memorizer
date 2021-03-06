package sample.Data;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Datasource {

    // Constants required for connection with the database

    private static final String DB_NAME = "vocabTestDatabase.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Programowanie\\Projekty\\courses\\" + DB_NAME;

    // List of all the constants used in the database

    private static final String TABLE_COURSES = "Courses";
    private static final String COLUMN_COURSES_ID = "ID_Course";
    private static final String COLUMN_COURSES_NAME = "Course_Name";
    private static final String COLUMN_COURSES_COURSELANGUAGE = "Course_Language";
    private static final String COLUMN_COURSES_USERSLANGUAGE = "Users_Language";
    private static final int INDEX_COURSES_ID = 1;
    private static final int INDEX_COURSES_NAME = 2;
    private static final int INDEX_COURSES_COURSELANGUAGE = 3;
    private static final int INDEX_COURSES_USERSLANGUAGE = 4;

    private static final String TABLE_LEVELS = "Levels";
    private static final String COLUMN_LEVELS_ID = "ID_Level";
    private static final String COLUMN_LEVELS_IDCOURSE = "ID_Course";
    private static final String COLUMN_LEVELS_NAME = "Level_Name";
    private static final int INDEX_LEVELS_ID = 1;
    private static final int INDEX_LEVELS_IDCOURSE = 2;
    private static final int INDEX_LEVELS_NAME = 3;

    private static final String TABLE_WORDS = "Words";
    private static final String COLUMN_WORDS_ID = "ID_Word";
    private static final String COLUMN_WORDS_IDCOURSE = "ID_Course";
    private static final String COLUMN_WORDS_IDLEVEL = "ID_Level";
    private static final String COLUMN_WORDS_FOREIGN = "Foreign_Word";
    private static final String COLUMN_WORDS_TRANSLATED = "Translated_Word";
    private static final int INDEX_WORDS_ID = 1;
    private static final int INDEX_WORDS_IDCOURSE = 2;
    private static final int INDEX_WORDS_IDLEVEL = 3;
    private static final int INDEX_WORDS_FOREIGN = 4;
    private static final int INDEX_WORDS_TRANSLATED = 5;

    // List of all the necessary queries used in this project
    // The comment below every constant represents its SQL form

    private static final String ADD_NEW_COURSE = "INSERT INTO " + TABLE_COURSES + '('
            + COLUMN_COURSES_NAME + ", " + COLUMN_COURSES_COURSELANGUAGE + ", " + COLUMN_COURSES_USERSLANGUAGE + ") VALUES(?, ?, ?)";
    // INSERT INTO Courses(Course_Name, Course_Language, Users_Language) VALUES ("?", "?", "?");

    private static final String ADD_NEW_LEVEL = "INSERT INTO " + TABLE_LEVELS + '('
            + COLUMN_LEVELS_IDCOURSE + ", " + COLUMN_LEVELS_NAME + ") VALUES(?, ?)";
    //INSERT INTO LEVELS(ID_Course, Level_Name) values (?, "?");

    private static final String ADD_NEW_WORD = "INSERT INTO " + TABLE_WORDS + '('
            + COLUMN_WORDS_IDCOURSE + ", " + COLUMN_WORDS_IDLEVEL + ", " + COLUMN_WORDS_FOREIGN + ", " + COLUMN_WORDS_TRANSLATED + ") VALUES(?, ?, ?, ?)";
    // INSERT INTO Words(ID_Course, ID_Level, Foreign_Word, Translated_Word) VALUES (?, ?, "?", "?");

    private static final String QUERY_COURSES = "SELECT * FROM " + TABLE_COURSES;
    // SELECT * FROM Courses;

    private static final String QUERY_VOCAB_FROM_COURSE = "SELECT * FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDCOURSE + " = ?";
    // SELECT * FROM Words WHERE ID_Course = ?;

    private static final String QUERY_VOCAB_FROM_LEVEL = "SELECT * FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDLEVEL + " = ?";
    // SELECT * FROM Words WHERE ID_Level = ?;

    private static final String QUERY_LEVELS_FROM_COURSE = "SELECT * FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_IDCOURSE + " = ?";
    // SELECT * FROM Levels WHERE ID_Course = ?;

    private static final String UPDATE_LEVEL = "UPDATE " + TABLE_LEVELS + " SET " + COLUMN_LEVELS_NAME + " = ?"
            + " WHERE " + COLUMN_LEVELS_ID + " = ?";
    // UPDATE Levels SET Level_Name = ? WHERE ID_Level ?

    private static final String UPDATE_WORD = "UPDATE " + TABLE_WORDS + " SET "
            + COLUMN_WORDS_FOREIGN + " = ?, " + COLUMN_WORDS_TRANSLATED + " = ? WHERE " + COLUMN_WORDS_ID + " = ?";
    // UPDATE Words SET Foreign_Word = "?", Translated_Word = "?" WHERE ID_Word = "?"

    private static final String DELETE_COURSE = "DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSES_ID + " = ?";
    //DELETE FROM Courses WHERE ID_Course = "?"

    private static final String DELETE_LEVEL_BY_ID = "DELETE FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_ID + " = ?";
    // DELETE FROM Levels WHERE ID_Level = "?"

    private static final String DELETE_ALL_LEVELS_FROM_COURSE = "DELETE FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_IDCOURSE + " = ?";
    // DELETE FROM Levels WHERE ID_Course = "?"

    private static final String DELETE_WORD_BY_ID = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_ID + " = ?";
    // DELETE FROM Words WHERE ID_Word = "?"

    private static final String DELETE_ALL_WORDS_FROM_COURSE = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDCOURSE + " = ?";
    // DELETE FROM Words WHERE ID_Course = "?"

    private static final String DELETE_ALL_WORDS_FROM_LEVEL = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDLEVEL + " = ?";
    // DELETE FROM Words WHERE ID_Level = ?

    private static final String COUNT_LEVELS_IN_COURSE = "SELECT COUNT(*) FROM " + TABLE_LEVELS + " WHERE " + COLUMN_LEVELS_IDCOURSE + " = ?";
    //  SELECT COUNT (*) FROM LEVELS WHERE ID_Course = ?

    private static final String COUNT_WORDS_IN_COURSE = "SELECT COUNT(*) FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDCOURSE + " = ?";
    //  SELECT COUNT (*) FROM WORDS WHERE ID_Course = ?

    private static final String COUNT_WORDS_IN_LEVEL = "SELECT COUNT(*) FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_IDLEVEL + " = ?";
    //  SELECT COUNT (*) FROM WORDS WHERE ID_Level = ?

    private static final String UPDATE_COURSE = "UPDATE " + TABLE_COURSES + " SET "
            + COLUMN_COURSES_NAME + " = ?, " + COLUMN_COURSES_COURSELANGUAGE + " = ?, " + COLUMN_COURSES_USERSLANGUAGE + " = ? WHERE "
            + COLUMN_COURSES_ID + " = ?";
    // UPDATE Courses SET Course_Name = ?, Course_Language = ?, Users_Language = ? WHERE ID_Course = ?

    // List of all the necessary PreparedStatements used in this project

    private PreparedStatement addNewCourse;
    private PreparedStatement addNewLevel;
    private PreparedStatement addNewWord;

    private PreparedStatement queryCourses;
    private PreparedStatement queryVocabularyFromCourse;
    private PreparedStatement queryVocabularyFromLevel;
    private PreparedStatement queryLevelsFromCourse;

    private PreparedStatement updateCourse;
    private PreparedStatement updateLevel;
    private PreparedStatement updateWord;

    private PreparedStatement countLevelsInCourse;
    private PreparedStatement countWordsInLevel;
    private PreparedStatement countWordsInCourse;

    private PreparedStatement deleteCourse;
    private PreparedStatement deleteAllLevelsFromCourse;
    private PreparedStatement deleteLevelByID;
    private PreparedStatement deleteAllWordsFromCourse;
    private PreparedStatement deleteWordByID;
    private PreparedStatement deleteAllWordsFromLevel;

    private Connection connection;
    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            addNewCourse = connection.prepareStatement(ADD_NEW_COURSE, Statement.RETURN_GENERATED_KEYS);
            addNewLevel = connection.prepareStatement(ADD_NEW_LEVEL, Statement.RETURN_GENERATED_KEYS);
            addNewWord = connection.prepareStatement(ADD_NEW_WORD);
            queryVocabularyFromCourse = connection.prepareStatement(QUERY_VOCAB_FROM_COURSE);
            queryVocabularyFromLevel = connection.prepareStatement(QUERY_VOCAB_FROM_LEVEL);
            queryLevelsFromCourse = connection.prepareStatement(QUERY_LEVELS_FROM_COURSE);
            updateCourse = connection.prepareStatement(UPDATE_COURSE);
            updateWord = connection.prepareStatement(UPDATE_WORD);
            updateLevel = connection.prepareStatement(UPDATE_LEVEL);
            countLevelsInCourse = connection.prepareStatement(COUNT_LEVELS_IN_COURSE);
            countWordsInLevel = connection.prepareStatement(COUNT_WORDS_IN_LEVEL);
            countWordsInCourse = connection.prepareStatement(COUNT_WORDS_IN_COURSE);
            deleteCourse = connection.prepareStatement(DELETE_COURSE);
            deleteLevelByID = connection.prepareStatement(DELETE_LEVEL_BY_ID);
            deleteWordByID = connection.prepareStatement(DELETE_WORD_BY_ID);
            queryCourses = connection.prepareStatement(QUERY_COURSES);
            deleteAllLevelsFromCourse = connection.prepareStatement(DELETE_ALL_LEVELS_FROM_COURSE);
            deleteAllWordsFromCourse = connection.prepareStatement(DELETE_ALL_WORDS_FROM_COURSE);
            deleteAllWordsFromLevel = connection.prepareStatement(DELETE_ALL_WORDS_FROM_LEVEL);

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

    private void performRollbackFunction() {
        try {
            System.out.println("Performing a rollback!");
            connection.rollback();
        } catch (SQLException e1) {
            System.out.println("Rollback failed! " + e1.getMessage());
        }
    }

    private void performFinallyFunction() {
        try {
            System.out.println("Resetting default commit behavior");
            connection.setAutoCommit(true);
        } catch(SQLException e) {
            System.out.println("Couldn't reset auto-commit! " + e.getMessage());
        }
    }

    public void deleteLevelFromDB(int levelID) {
        try {
            connection.setAutoCommit(false);

            deleteAllWordsFromLevel.setInt(1, levelID);
            deleteLevelByID.setInt(1, levelID);

            deleteAllWordsFromLevel.executeUpdate();
            deleteLevelByID.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            System.out.println("Delete level exception: " + e.getMessage());
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
        }
    }

    private int executeStatementUpdateInt(PreparedStatement statement, String subject) throws SQLException {
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Operation with " + subject + " successfully performed!!");
                connection.commit();
                ResultSet generatedKeys = statement.getGeneratedKeys();
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed adding new level!");
            }
    }

    public int addNewLevelToCourse(int courseID, String levelName) {
        try {
            connection.setAutoCommit(false);

            addNewLevel.setInt(1, courseID);
            addNewLevel.setString(2, levelName);

            return executeStatementUpdateInt(addNewLevel, "Level"); // Returns new level ID
        } catch (Exception e) {
            System.out.println("Insert new level exception: " + e.getMessage());
            performRollbackFunction();
            return -1;
        }
        finally {
           performFinallyFunction();
        }
    }

    private void executeStatementUpdateVoid(PreparedStatement statement, String subject) throws SQLException{
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Operation with " + subject + " successfully performed!!");
                connection.commit();
            } else {
                throw new SQLException("Failed adding new level!");
            }
    }

    public void updateLevelInDB(int levelID, String newLevelName) {
        try {
            connection.setAutoCommit(false);

            updateLevel.setString(1, newLevelName);
            updateLevel.setInt(2, levelID);

            executeStatementUpdateVoid(updateLevel, "update level");
        } catch (Exception e) {
            System.out.println("Update level exception: " + e.getMessage());
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
        }
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

    public List<Level> getLevelsFromCourse(int courseID) {
        try {
            List<Level> levels = new LinkedList<>();
            queryLevelsFromCourse.setInt(1, courseID);
            ResultSet results = queryLevelsFromCourse.executeQuery();

            while (results.next()) {
                int idLevel = results.getInt(INDEX_LEVELS_ID);
                String levelName = results.getString(INDEX_LEVELS_NAME);

                Level newLevel = new Level();
                newLevel.setIdLevel(idLevel);
                newLevel.setLevelName(levelName);

                levels.add(newLevel);
            }
            return levels;

        } catch (SQLException e) {
            System.out.println("Error while getting levels from the database! \n");
            e.getMessage();
            return null;
        }
    }

    private List<Word> queryVocabulary(PreparedStatement statement, int idVal) throws SQLException {
        List<Word> wordsList = new LinkedList<>();

        statement.setInt(1, idVal);
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            int idWord = results.getInt(INDEX_WORDS_ID);
            int idLevel = results.getInt(INDEX_WORDS_IDLEVEL);
            String foreignWord = results.getString(INDEX_WORDS_FOREIGN);
            String translatedWord = results.getString(INDEX_WORDS_TRANSLATED);

            Word newWord = new Word();
            newWord.setIdWord(idWord);
            newWord.setIdLevel(idLevel);
            newWord.setForeignWord(foreignWord);
            newWord.setTranslatedWord(translatedWord);

            wordsList.add(newWord);
        }
        return wordsList;
    }

    public List<Word> getVocabularyFromCourse(int courseID) {
        try {
            return queryVocabulary(queryVocabularyFromCourse, courseID);
        } catch (SQLException e) {
            System.out.println("Error while getting vocabulary from the database! \n");
            e.getMessage();
            return null;
        }
    }

    public List<Word> getVocabularyFromLevel(int levelID) {
        try {
            return queryVocabulary(queryVocabularyFromLevel, levelID);
        } catch (SQLException e) {
            System.out.println("Error while getting vocabulary from the level! \n");
            e.printStackTrace();
            return null;
        }
    }

    public int getLevelsCountFromCourse(Course course) {
        try {
            int courseID = course.getCourseID();
            countLevelsInCourse.setInt(1, courseID);

            ResultSet result = countLevelsInCourse.executeQuery();
            return result.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getting level count from courses \n");
            e.printStackTrace();
            return -1;
        }
    }

    public int getWordsCountFromCourse(Course course) {
        try {
            int courseID = course.getCourseID();
            countWordsInCourse.setInt(1, courseID);

            ResultSet result = countWordsInCourse.executeQuery();
            return result.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getting words count from course \n");
            e.printStackTrace();
            return -1;
        }
    }

//    public int getWordsCountFromLevel(Level level) {
//        try {
//            int levelID = level.getIdLevel();
//            countWordsInLevel.setInt(1, levelID);
//
//            ResultSet result = countWordsInLevel.executeQuery();
//            return result.getInt(1);
//        } catch (SQLException e) {
//            System.out.println("Error getting words count from level \n");
//            e.printStackTrace();
//            return -1;
//        }
//    }

    public int addNewCourse(String courseName, String teachingLanguage, String usersLanguage) {
        try {
            connection.setAutoCommit(false);

            addNewCourse.setString(1, courseName);
            addNewCourse.setString(2, teachingLanguage);
            addNewCourse.setString(3, usersLanguage);

            int affectedRows = addNewCourse.executeUpdate();
            if(affectedRows == 1) {
                System.out.println("Course " + courseName + " sucessfully added to the database!");
                connection.commit();
                try{
                    ResultSet generatedKeys = addNewCourse.getGeneratedKeys();
                    int newCourseID = generatedKeys.getInt(1);
                    addNewLevelToCourse(newCourseID, "Level 1");
                    return newCourseID;
                } catch(SQLException e) {
                    System.out.println("Failed obtaining the course ID");
                    return -1;
                }
            } else {
                throw new SQLException("Failed adding new course!");
            }
        } catch (Exception e) {
            System.out.println("Insert new course exception: " + e.getMessage());
            performRollbackFunction();
            return -1;
        }
        finally {
            performFinallyFunction();
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
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
        }
    }

    public void deleteCourseFromDB(int courseID) {
        try {
            connection.setAutoCommit(false);

            deleteAllWordsFromCourse.setInt(1, courseID);
            deleteAllLevelsFromCourse.setInt(1, courseID);
            deleteCourse.setInt(1, courseID);

            deleteAllWordsFromCourse.executeUpdate();
            deleteAllLevelsFromCourse.executeUpdate();
            deleteCourse.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            System.out.println("Delete course exception: " + e.getMessage());
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
        }
    }

    public void addNewWordToLevel(int courseID, int levelID, String wordValue, String translationValue) {
        try {
            connection.setAutoCommit(false);

            addNewWord.setInt(1, courseID);
            addNewWord.setInt(2, levelID);
            addNewWord.setString(3, wordValue);
            addNewWord.setString(4, translationValue);

            executeStatementUpdateVoid(addNewWord, "add new word");
        } catch (Exception e) {
            System.out.println("Insert new word exception: " + e.getMessage());
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
        }
    }

    public void updateWordInDB(int wordID, String newWordValue, String newTranslationValue) {
        try {
            connection.setAutoCommit(false);

            updateWord.setString(1, newWordValue);
            updateWord.setString(2, newTranslationValue);
            updateWord.setInt(3, wordID);

            executeStatementUpdateVoid(updateWord, "update word");
        } catch (Exception e) {
            System.out.println("Update word exception: " + e.getMessage());
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
        }
    }

    public void removeWordFromDB(int wordID) {
        try {
            connection.setAutoCommit(false);

            deleteWordByID.setInt(1, wordID);

            executeStatementUpdateVoid(deleteWordByID, "delete word");
        } catch (Exception e) {
            System.out.println("Delete word exception: " + e.getMessage());
            performRollbackFunction();
        }
        finally {
            performFinallyFunction();
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
            if(deleteLevelByID != null) {
                deleteLevelByID.close();
            }
            if(deleteWordByID != null) {
                deleteWordByID.close();
            }
            if(queryCourses != null) {
                queryCourses.close();
            }
            if(deleteAllLevelsFromCourse != null) {
                deleteAllLevelsFromCourse.close();
            }
            if(deleteAllWordsFromCourse != null) {
                deleteAllWordsFromCourse.close();
            }
            if(updateLevel != null) {
                updateLevel.close();
            }
            if(deleteAllWordsFromLevel != null) {
                deleteAllWordsFromLevel.close();
            }
            if(countWordsInCourse != null) {
                countWordsInCourse.close();
            }
            if(countWordsInLevel != null) {
                countWordsInLevel.close();
            }
            if(countLevelsInCourse != null) {
                countLevelsInCourse.close();
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
