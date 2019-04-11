package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Data.Course;
import sample.Data.Datasource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    BorderPane mainBorderPane;

    @FXML
    private ListView<Course> courseListView;

    @FXML
    private MenuItem editCourseItem;

    @FXML
    private MenuItem removeCourseItem;

    @FXML
    private MenuItem loadCourseItem;

    @FXML
    private MenuItem showOverviewItem;

//    ---
    
    private static final String WINDOWS_LOCATION = "/windows/";

    private static final String COURSE_OVERVIEW_WINDOW = WINDOWS_LOCATION + "courseOverviewWindow.fxml";
    private static final String ADD_NEW_COURSE_WINDOW = WINDOWS_LOCATION + "addNewCourseWindow.fxml";
    private static final String EDIT_COURSE_WINDOW = WINDOWS_LOCATION + "editCourseWindow.fxml";
    private static final String COURSE_WINDOW = WINDOWS_LOCATION + "courseWindow.fxml";
    private static final String SETTINGS_WINDOW = WINDOWS_LOCATION + "settingsWindow.fxml";
    private static final String ABOUT_WINDOW = WINDOWS_LOCATION + "aboutWindow.fxml";
    private static final String USERS_MANUAL_WINDOW = WINDOWS_LOCATION + "usersManualWindow.fxml";
    

//    ---

    private List<Course> coursesList = new LinkedList<>();
    private Course selectedCourse;

    public void initialize() {
        if(selectedCourse == null) {
            editCourseItem.setDisable(true);
            removeCourseItem.setDisable(true);
            loadCourseItem.setDisable(true);
            showOverviewItem.setDisable(true);
        }

        refreshCourseList();

        FXMLLoader courseOverviewLoader = new FXMLLoader();
        courseOverviewLoader.setLocation(getClass().getResource(COURSE_OVERVIEW_WINDOW));
        try{
            mainBorderPane.setCenter(courseOverviewLoader.load());
            CourseOverviewController controller = courseOverviewLoader.getController();
            controller.disableCloseButton();
        } catch (IOException e ) {
            System.out.println("Error loading the course overview window");
        }

        courseListView.setCellFactory(e -> new ListCell<>() {
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

        courseListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<? super Course>) (observableValue, o, t1) -> {
            if(t1 != null) {
                selectedCourse = t1;
                System.out.println(selectedCourse.getCourseName() + " selected.");

                editCourseItem.setDisable(false);
                removeCourseItem.setDisable(false);
                loadCourseItem.setDisable(false);
                showOverviewItem.setDisable(false);

                CourseOverviewController controller = courseOverviewLoader.getController();
                controller.populateFields(selectedCourse);
            } // Without the 'if' statement, we'd get a NullPointerException after deleting an item
        });

        selectCourse(getFirstCourse());

        // Pressing Delete on course works the same as selecting "Delete course" item
        // Pressing Insert works the same as selecting "Add new course" item
        courseListView.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.DELETE) {
                removeCourse();
            }
            if(keyEvent.getCode() == KeyCode.INSERT) {
                addCourse();
            }
        });
    }

    @FXML
    public void addCourse() {
        System.out.println("Addcourse button clicked!");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(ADD_NEW_COURSE_WINDOW));

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
            int newCourseID = controller.processResults();
            if(newCourseID > 0) {
                refreshCourseList();
                selectCourse(newCourseID);
            }
        }
    }

    @FXML
    public void editCourse() {
        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(EDIT_COURSE_WINDOW));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                System.out.println("Failed loading \"editWordWindow\"");
                return;
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            EditCourseController controller = fxmlLoader.getController();
            Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
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
                selectCourse(getFirstCourse());
            }
        }
    }

    @FXML
    public void loadCourse() {
        System.out.println("Load course button pressed.");

        if(courseListView.getSelectionModel().getSelectedItem() != null) {
            selectedCourse = courseListView.getSelectionModel().getSelectedItem();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(COURSE_WINDOW));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle(selectedCourse.getCourseName());
                stage.setScene(scene);
                stage.initOwner(mainBorderPane.getScene().getWindow());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch(IOException e) {
                System.out.println("Failed loading \"course Window\"");
                System.out.println(e.getMessage());
                return;
            }

            CourseWindowController controller = fxmlLoader.getController();
            controller.setCourse(selectedCourse);
            controller.initialLoad();
        }
    }

    @FXML
    public void showSettings() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(SETTINGS_WINDOW));
        
        loadWindow(fxmlLoader, "Settings window");
    }

    @FXML
    public void showOverview() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(COURSE_OVERVIEW_WINDOW));
        
        loadWindow(fxmlLoader, "Course overview window");
        
        Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
        CourseOverviewController controller = fxmlLoader.getController();
        controller.populateFields(selectedCourse);
        controller.disableLoadButton();
    }

    @FXML
    public void showAbout() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(ABOUT_WINDOW));
        
        loadWindow(fxmlLoader, "About Window");
    }

    @FXML
    public void showUsersManual() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(USERS_MANUAL_WINDOW));
        
        loadWindow(fxmlLoader, "User's Manual");
    }

    @FXML
    public void exitApp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit application");
        alert.setHeaderText("Do you really want to exit the application?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    private void selectCourse(int courseID) {
        Course selectedCourse = new Course();
        for(Course course : coursesList) {
            if (course.getCourseID() == courseID) {
                selectedCourse = course;
                break;
            }
        }
        courseListView.getSelectionModel().select(selectedCourse);
    }

    private void selectCourse(Course course) {
        courseListView.getSelectionModel().select(course);
    }

    private Course getFirstCourse() {
        Course firstCourse = coursesList.get(0);

        for(Course course : coursesList) {
            if( course.getCourseID() < firstCourse.getCourseID()) {
                firstCourse = course;
            }
        }
        return firstCourse;
    }

    private void loadWindow(FXMLLoader fxmlLoader, String windowName) {
        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch(IOException e) {
            System.out.println("Failed loading " + windowName);
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void refreshCourseList() {
        coursesList = Datasource.getInstance().getCourses();
        courseListView.getItems().setAll(coursesList);
    }
}
