package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Datasource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Controller {
    private List<Course> coursesList;

    @FXML
    BorderPane mainBorderPane;

    @FXML
    private ListView<Course> courseListView;

    private Course selectedCourse;
    private int selectedCourseID;

    @FXML
    public void addCourse() {
        System.out.println("Addcourse button clicked!");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addNewCourseWindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch(IOException e) {
            System.out.println("Failed loading \"addNewCourseWindow.fxml\"");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            AddNewCourseController controller = fxmlLoader.getController();
            controller.processResults();
            refreshCourseList();
        }
    }

    @FXML
    public void editCourse() {
        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("editCourseWindow.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Failed loading \"editWordWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditCourseController controller = fxmlLoader.getController();
            Course selectedCourse = (Course) courseListView.getSelectionModel().getSelectedItem();
            controller.populateFields(selectedCourse);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int courseID = selectedCourse.getCourseID();
                controller.updateCourse(courseID);

                refreshCourseList();
            }
        }
    }

    @FXML
    public void removeCourse() {
        System.out.println("Remove course button clicked!");

        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete a course");
            alert.setHeaderText("Do you really want to delete " + selectedCourse.getCourseName() + "?\n" +
                    "You will delete all the words, levels and the course itself!");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                int courseID = selectedCourse.getCourseID();
                Datasource.getInstance().deleteCourseFromDB(courseID);
                System.out.println("Course deleted!");
                refreshCourseList();
            }
        }
    }

    @FXML
    public void loadCourse() {
        System.out.println("Load course button pressed.");

        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            selectedCourse = courseListView.getSelectionModel().getSelectedItem();
            selectedCourseID = selectedCourse.getCourseID();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("courseWindow.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle(selectedCourse.getCourseName());
                stage.setScene(scene);
                stage.show();
            } catch(IOException e) {
                System.out.println("Failed loading \"course Window\"");
                System.out.println(e.getMessage());
                return;
            }

            CourseWindowController controller = fxmlLoader.getController();
            controller.setCourse(selectedCourse);
            controller.setCourseID(selectedCourseID);
            controller.initialLoad();

        }
    }

    public void initialize() {

        refreshCourseList();

        courseListView.setCellFactory(e -> new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course course, boolean empty) {
                        super.updateItem(course, empty);

                        if(empty || course == null) {
                            setText("");
                        } else {
                            setText(course.getCourseName());
                        }
                    }
                });

        courseListView.getSelectionModel().selectedItemProperty().addListener(e -> new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1 != null) {
                    Course selectedCourse = (Course) t1;
                    System.out.println(selectedCourse.getCourseName() + " selected.");
                } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
            }
        });

    }

    public void refreshCourseList() {
        coursesList = Datasource.getInstance().getCourses();
        courseListView.getItems().setAll(coursesList);
    }






}
