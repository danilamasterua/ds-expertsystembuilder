package ds.esb.expertsystembuilder.controllers;

import ds.esb.expertsystembuilder.classes.Model;
import ds.esb.expertsystembuilder.classes.Variables;
import ds.esb.expertsystembuilder.classes.bean.Variable;
import ds.esb.expertsystembuilder.classes.bean.VariableType;
import ds.esb.expertsystembuilder.services.JsonWorks;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IndexController {
    @FXML
    public StatusBar statusBar;
    @FXML
    public TextArea variableStatusesTextArea;
    @FXML
    public MenuItem chooseProjectBtn;
    @FXML
    public Label decisionLabel;
    @FXML
    public CheckMenuItem showVariables;
    public Menu languageMenu;
    public MenuItem exitBtn;
    public Label vaiablesPaneTitle;
    public Menu fileMenu;
    public Menu preferencesMenu;
    public Label decisionPaneLabel;
    @FXML
    private Label debugLabel;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private VBox variablePane;
    private ToggleGroup variablesToggleGroup = new ToggleGroup();
    private Model model = new Model();
    private HashMap<String, String> localizations = new HashMap<>();
    private HashMap<String, String> localization = new HashMap<>();

    public void initialize(){
        try {
            localizations = JsonWorks.getLocalizations();
            for (var l : localizations.entrySet()) {
                MenuItem mi = new MenuItem(l.getKey());
                mi.setOnAction(actionEvent -> {
                    String a = mi.getText();
                    localization = JsonWorks.getLocalization(localizations.get(a));
                    setLocale();
                });
                languageMenu.getItems().add(mi);
            }
            String locale = Locale.getDefault().getDisplayLanguage();
            try {
                localization = JsonWorks.getLocalization(localizations.get(locale));
            } catch (Exception e) {
                debugLabel.setText(e.getMessage());
                localization = JsonWorks.getLocalization(localizations.get("english"));
            }
            setLocale();
        } catch (RuntimeException e){
            debugLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void exit(){
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void chooseProject() throws IOException {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = directoryChooser.showDialog(stage);
        try {
            model = JsonWorks.loadProject(dir.getAbsolutePath());
            statusBar.setText(dir.getAbsolutePath());
            addVariablesFields(model.getVariables());
            chooseProjectBtn.setDisable(true);
        } catch (RuntimeException ex){
            statusBar.setText(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void setLocale(){
        chooseProjectBtn.setText(localization.get("stLoadProject"));
        showVariables.setText(localization.get("stPreferencesShowVariable"));
        exitBtn.setText(localization.get("stFileExit"));
        vaiablesPaneTitle.setText(localization.get("stVariablesPane"));
        fileMenu.setText(localization.get("stFileMenu"));
        preferencesMenu.setText(localization.get("stPreferencesMenu"));
        decisionPaneLabel.setText(localization.get("stDecisionPane"));
    }

    @FXML
    private void addVariablesFields(Variables variables){
        VBox v = new VBox();
        ArrayList<RadioButton> rblist = new ArrayList<>();
        variables.getVariables().forEach((key, variable)->{
            RadioButton rb = new RadioButton(variable.getName());
            rb.setUserData(key);
            rblist.add(rb);
        });
        rblist.forEach(rb->{
            v.getChildren().add(rb);
            rb.setToggleGroup(variablesToggleGroup);
        });
        Button button = new Button(localization.get("stButtonRead"));
        button.setId("setVariablesBtn");
        button.getStyleClass().addAll("btn", "btn-primary");
        variablePane.getChildren().addAll(v, button);
        button.setOnAction(check);
    }
    
    private final EventHandler<ActionEvent> check = new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.setProcessedRules(new ArrayList<>());
                model.setChoices(new HashMap<>());
                variableStatusesTextArea.setText("");
                AtomicInteger statusCode = new AtomicInteger(501);
                if(variablesToggleGroup.getSelectedToggle()!=null){
                    statusCode.set(200);
                }
                if(statusCode.get() == 200) {
                    RadioButton selected = (RadioButton) variablesToggleGroup.getSelectedToggle();
                    debugLabel.setText(selected.getUserData().toString());
                    statusBar.setText(localization.get("stAnswersRead"));
                    model.setPoint(Integer.parseInt(selected.getUserData().toString()));
                    String dev = model.doExpertLogic();
                    if(showVariables.isSelected()){
                        dev += model.printDecisionVariables();
                    }
                    variableStatusesTextArea.setText(dev);
                    decisionLabel.setText(model.printDecision());
                } else if (statusCode.get()==501){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(localization.get("stAlert501Title"));
                    alert.setHeaderText(localization.get("stAlert501Header"));
                    alert.setContentText(localizations.get("stAlert501Message"));
                    alert.showAndWait();
                }
            }
        };
}
