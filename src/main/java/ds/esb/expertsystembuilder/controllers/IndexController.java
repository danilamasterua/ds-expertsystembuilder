package ds.esb.expertsystembuilder.controllers;

import ds.esb.expertsystembuilder.classes.Model;
import ds.esb.expertsystembuilder.classes.Variables;
import ds.esb.expertsystembuilder.classes.bean.Variable;
import ds.esb.expertsystembuilder.classes.bean.VariableType;
import ds.esb.expertsystembuilder.services.JsonWorks;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
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
    private final Map<Integer, ComboBox<String>> variablesComboBoxes = new HashMap<>();
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
            System.out.println(locale);
            try {
                localization = JsonWorks.getLocalization(localizations.get(locale));
            } catch (Exception e) {
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
    public void chooseProject(){
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
        ArrayList<HBox> hboxes = new ArrayList<>();
        for (Map.Entry<Integer, Variable> entry:variables.getVariables().entrySet()){
            if (entry.getValue().getVariableType()==VariableType.INITIALIZABLE) {
                HBox var = new HBox(10);
                Label label = new Label(entry.getValue().getName());
                label.setMinWidth(50);
                label.setAlignment(Pos.BASELINE_RIGHT);
                ComboBox<String> status = new ComboBox<>();
                status.setPrefWidth(150);
                status.setId("var" + entry.getValue().getId());
                status.getStyleClass().add("split-menu-btn");
                for (Map.Entry<Integer, String> entry1 : entry.getValue().getStatuses().entrySet()) {
                    status.getItems().add(entry1.getValue());
                }
                var.getChildren().addAll(label, status);
                hboxes.add(var);
                variablesComboBoxes.put(entry.getValue().getId(), status);
            }
        }
        for (HBox hBox:hboxes){
            variablePane.getChildren().add(hBox);
        }
        Button button = new Button(localization.get("stButtonRead"));
        button.setId("setVariablesBtn");
        button.getStyleClass().addAll("btn", "btn-primary");
        variablePane.getChildren().add(button);
        button.setOnAction(check);
    }
    
    private final EventHandler<ActionEvent> check = new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.setProcessedRules(new ArrayList<>());
                model.setChoices(new HashMap<>());
                variableStatusesTextArea.setText("");
                AtomicInteger statusCode = new AtomicInteger(501);
                for(Map.Entry<Integer, ComboBox<String>> entry:variablesComboBoxes.entrySet()){
                    if (entry.getValue().getValue()!=null) {
                        try {
                            model.putChoice(entry.getKey(), model.getVariables().getVariableById(entry.getKey()).searchStatusIdByValue(entry.getValue().getValue()));
                            statusCode.set(200);
                        } catch (Exception e) {
                            statusCode.set(500);
                            statusBar.setText(e.getMessage());
                        }
                    }
                }
                if(statusCode.get() == 200) {
                    String logic = model.doExpertLogic();
                    if(showVariables.isSelected()){
                        logic+= model.printDecisionVariables();
                    }
                    variableStatusesTextArea.setText(logic);
                    statusBar.setText(localization.get("stAnswersRead"));
                    debugLabel.setText(String.valueOf(model.getLastRule()));
                    decisionLabel.setText(model.printDecision());
                    for (var entry:variablesComboBoxes.entrySet()){
                        entry.getValue().setValue(model.getVariables().getVariableById(entry.getKey()).getStatuses().get(model.getChoices().get(entry.getKey())));
                    }
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
