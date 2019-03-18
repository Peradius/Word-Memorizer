package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Data.Course;
import sample.Data.Datasource;
import sample.Data.Word;
import java.util.List;

public class CourseVocabularyWindowController {
    private Course selectedCourse;

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    @FXML
    private TableView<Word> wordsTableView;

    @FXML
    private TableColumn<Word, Integer> columnLevel;

    @FXML
    private TableColumn<Word, String> columnForeign;

    @FXML
    private TableColumn<Word, String> columnTranslation;

    public void processResults() {
        List<Word> courseVocabulary = Datasource.getInstance().getVocabularyFromCourse(selectedCourse.getCourseID());

        columnLevel.setCellValueFactory(new PropertyValueFactory<>("idLevel"));
        columnForeign.setCellValueFactory(new PropertyValueFactory<>("foreignWord"));
        columnTranslation.setCellValueFactory(new PropertyValueFactory<>("translatedWord"));

        wordsTableView.getItems().setAll(courseVocabulary);

    }
}
