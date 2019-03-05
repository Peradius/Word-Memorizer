package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Data.Datasource;
import sample.Data.Word;

import java.util.List;
import java.util.Optional;

public class ReviewWindowController {

    // ID from which the words will be taken (CourseID for now, but also LevelID for the future)
    private int courseID;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }



    @FXML
    DialogPane reviewWindowPane;

    @FXML
    TextField foreignWordField;

    @FXML
    TextField translatedWordField;

    @FXML
    Label resultLabel;

    @FXML
    Label totalWordsLabel;

    @FXML
    Label goodWordsLabel;

    @FXML
    Label wrongWordsLabel;

    @FXML
    public void nextButton() {
        String translatedWord = vocabularyToReview.get(x).getTranslatedWord();
        String answer = translatedWordField.getText();

        if(translatedWord.equals(answer)) {
            System.out.println("Correct!");
            ++correctAnswersAmount;
            goodWordsLabel.setText("" + correctAnswersAmount);
        } else {
            System.out.println("Incorrect! It should be: " + translatedWord);
            ++wrongAnswersAmount;
            wrongWordsLabel.setText("" + wrongAnswersAmount);
        }
        x++;
        reviewSingleWord();
    }

    private int correctAnswersAmount = 0;
    private int wrongAnswersAmount = 0;
    private int x = 0;
    private List<Word> vocabularyToReview = Datasource.getInstance().getVocabularyFromCourse(courseID);
    private int reviewSessionSize = vocabularyToReview.size();

    public void processReview() {
        totalWordsLabel.setText("" + reviewSessionSize);
        reviewSingleWord();
    }

    private void reviewSingleWord() {
        if(x < reviewSessionSize) {
            // There are words in the queue to be reviewed

            Word currentWord = vocabularyToReview.get(x);
            String foreignWord = currentWord.getForeignWord();

            foreignWordField.setText(foreignWord);
            translatedWordField.clear();
        } else {
            // All words were reviewed. Displaying the final message.

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Review complete!");
            alert.setHeaderText("You have completed the review session!");
            alert.setContentText("Words to review: " + reviewSessionSize + "\n" +
                    "Correct answers: " + correctAnswersAmount + "\n" +
                    "Wrong answers: " + wrongAnswersAmount);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) reviewWindowPane.getScene().getWindow();
                stage.close();

            }
        }
    }

}
