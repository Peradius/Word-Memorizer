package sample.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SettingsWindowController {
    private List<String> languagesList = new ArrayList<>();
    private List<String> layoutsList = new ArrayList<>();
    private List<String> viewsList = new ArrayList<>();
    private List<Integer> learnSessionSizeList = new ArrayList<>();
    private List<Integer> reviewSessionSizeList = new ArrayList<>();

    // == Application option fields ==
    private String appLanguage = "English";
    private String appLayout = "Standard";
    private String appViewMode = "Window mode";
    private Integer learnSize = 0;
    private Integer reviewSize = 0;

    public SettingsWindowController() {
    }

    @FXML
    private GridPane settingsGridPane;

    @FXML
    private ChoiceBox<String> languages;

    @FXML
    private ChoiceBox<String> applicationLayout;

    @FXML
    private ChoiceBox<String> viewMode;

    @FXML
    private ChoiceBox<Integer> learnSessionSize;

    @FXML
    private ChoiceBox<Integer> reviewSessionSize;

    public void initialize() {
        loadData();
    }

    @FXML
    public void saveConfig() {
        this.appLanguage = languages.getValue();
        this.appLayout = applicationLayout.getValue();
        this.appViewMode = viewMode.getValue();
        this.learnSize = learnSessionSize.getValue();
        this.reviewSize = reviewSessionSize.getValue();

        System.out.println("SETTINGS SET!");
        System.out.println("Language: " + appLanguage);
        System.out.println("Layout: " + appLayout);
        System.out.println("View mode: " + appViewMode);
        System.out.println("Learn size: " + learnSize);
        System.out.println("Review size: " + reviewSize);

        exit();
    }

    @FXML
    public void exit() {
        Stage stage = (Stage) settingsGridPane.getScene().getWindow();
        stage.close();
    }

    private void loadData() {
        // == Languages ==
        languagesList.clear();
        languagesList.add("English"); // Only English for now, there's going to be more in the future

        languages.getItems().addAll(languagesList);
        languages.getSelectionModel().selectFirst();

        // == Layouts ==
        layoutsList.clear();
        layoutsList.add("Standard"); // The only layout available for now, there's going to be more in the future (eg. white, dark)

        applicationLayout.getItems().addAll(layoutsList);
        applicationLayout.getSelectionModel().selectFirst();

        // == Views ==
        viewsList.clear();
        viewsList.add("Window mode"); // There's going to be fullscreen view mode in the future

        viewMode.getItems().addAll(viewsList);
        viewMode.getSelectionModel().selectFirst();

        // == Learn session size ==
        learnSessionSizeList.clear();
        learnSessionSizeList.add(0); // No options available for now. There's going to be 5, 10, 15, 20 in the future

        learnSessionSize.getItems().addAll(learnSessionSizeList);
        learnSessionSize.getSelectionModel().selectFirst();

        // == Review session size ==
        reviewSessionSizeList.clear();
        reviewSessionSizeList.add(0); // No options available for now. There's going to be 5, 10, 15, 20 in the future

        reviewSessionSize.getItems().addAll(reviewSessionSizeList);
        reviewSessionSize.getSelectionModel().selectFirst();
    }
}
