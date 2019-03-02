package sample.Data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Datasource {

    private static final String DB_NAME = "vocabTestDatabase.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Programowanie\\Projekty\\courses\\" + DB_NAME;

    public static final String TABLE_WORDS = "Words";
    public static final String COLUMN_WORDS_ID = "ID";
    public static final String COLUMN_WORDS_ENGLISH = "English";
    public static final String COLUMN_WORDS_POLISH = "Polish";
    public static final int INDEX_WORDS_ID = 1;
    public static final int INDEX_WORDS_ENGLISH = 2;
    public static final int INDEX_WORDS_POLISH = 3;


    private Connection connection;
    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
    }

    public static void printTrace() {
        System.out.println(CONNECTION_STRING);
    }

    public List<Word> queryVocabulary() {
        try {
            List<Word> vocabulary = new LinkedList<>();
            String query = "SELECT * FROM " + TABLE_WORDS;
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int idWord = results.getInt(INDEX_WORDS_ID);
                String foreignWord = results.getString(INDEX_WORDS_ENGLISH);
                String translatedWord = results.getString(INDEX_WORDS_POLISH);

                Word newWord = new Word();
                newWord.setIdWord(idWord);
                newWord.setForeignWord(foreignWord);
                newWord.setTranslatedWord(translatedWord);

                vocabulary.add(newWord);
            }

            return vocabulary;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addNewWordToDB(String wordValue, String translationValue) {

        try {
            String query = "INSERT INTO " + TABLE_WORDS + " (" + COLUMN_WORDS_ENGLISH + ", " + COLUMN_WORDS_POLISH + ") VALUES ('" +
                    wordValue + "', '" + translationValue + "')";
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWordInDB(int id, String wordValue, String translationValue) {
        try {
            String query = "UPDATE " + TABLE_WORDS + " SET " + COLUMN_WORDS_ENGLISH + "='" + wordValue + "', " + COLUMN_WORDS_POLISH + "='" + translationValue + "'"
                    + " WHERE " + COLUMN_WORDS_ID + " = " + id;
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Error updating a contact!");
            e.printStackTrace();
            return;
        }
    }

    public void removeWordFromDB(int id) {
        try {
            String query = "DELETE FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORDS_ID + " = " + id;
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch(SQLException e) {
            System.out.println("Error deleting record from database");
            e.printStackTrace();
            return;
        }
    }

    public List<Word> fetchVocabularyToReviewList() {
        List<Word> wordList = queryVocabulary();
        return wordList;
    }

    public HashMap<String, String> fetchVocabularyToReview() {
        HashMap<String, String> vocabulary = new HashMap<>();
        List<Word> wordList = queryVocabulary();

        for(Word singleWord : wordList) {
            String foreignWord = singleWord.getForeignWord();
            String translation = singleWord.getTranslatedWord();
            vocabulary.put(foreignWord, translation);
        }
        return vocabulary;
    }

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("Connection to the database failed!");
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Closing the database failed!");
            e.printStackTrace();
        }
    }
}
