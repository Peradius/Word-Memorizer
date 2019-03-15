package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Data.Datasource;
import sample.Data.Word;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class BulkAddWordsController {
    private static final int INSERT_TEXT_TOGGLE_ID = 0;
    private static final int CHOOSE_FILE_TOGGLE_ID = 1;

    private int courseID;
    private int levelID;

    private static List<Word> importedVocab = new LinkedList<>();

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    @FXML
    private GridPane bulkAddWordsGridPane;

    @FXML
    private ToggleGroup insertVocabMethod;

    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a .csv file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getName());

            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile)))) {
                // Initial list clear
                importedVocab.clear();

                String line;

                while((line = br.readLine()) != null) {
                    String [] lineArr = line.split(",");
                    String foreignWord = lineArr[0];
                    String translatedWord = lineArr[1];

                    Word newWord = new Word();
                    newWord.setForeignWord(foreignWord);
                    newWord.setTranslatedWord(translatedWord);
                    newWord.setIdCourse(courseID);
                    newWord.setIdLevel(levelID);

                    importedVocab.add(newWord);
                }
            } catch(FileNotFoundException e) {
                System.out.println("File not found!");
                e.getMessage();
            } catch(IOException e) {
                System.out.println("Error using FileInputStream!");
                e.getMessage();
            }
        }
        else {
            System.out.println("File selection cancelled.");
        }
    }

    @FXML
    public void submitAction() {
        if(insertVocabMethod.getSelectedToggle() == insertVocabMethod.getToggles().get(INSERT_TEXT_TOGGLE_ID)) {
            // Insert text method
            System.out.println("TO BE IMPLEMENTED!");

        } else if(insertVocabMethod.getSelectedToggle() == insertVocabMethod.getToggles().get(CHOOSE_FILE_TOGGLE_ID)) {
            // Choose file method
            for(Word word : importedVocab) {
                Datasource.getInstance().addNewWordToLevel(courseID, levelID, word.getForeignWord(), word.getTranslatedWord());
            }
            System.out.println("SUCCESS INSERTING VIA FILE CHOOSER!");
            closeWindow();

        } else {
            System.out.println("Insert Vocab Method Error!");
            closeWindow();
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage)bulkAddWordsGridPane.getScene().getWindow();
        stage.close();
    }
}
