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
import javafx.scene.Scene;
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
    @FXML
    private Label debugLabel;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private VBox variablePane;
    private Map<Integer, ComboBox<String>> variablesComboBoxes = new HashMap<>();
    private Model model = new Model();
    private String pth = "";
    private Scene scene;

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
            pth=dir.getAbsolutePath();
            statusBar.setText(dir.getAbsolutePath());
            addVariablesFields(model.getVariables());
            chooseProjectBtn.setDisable(true);
        } catch (RuntimeException ex){
            statusBar.setText(ex.getMessage());
        }
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
                status.setPrefWidth(100);
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
        Button button = new Button("Записати");
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
                    statusBar.setText("Відповіді записано");
                    debugLabel.setText(String.valueOf(model.getLastRule()));
                    decisionLabel.setText(model.printDecision());
                    for (var entry:variablesComboBoxes.entrySet()){
                        entry.getValue().setValue(model.getVariables().getVariableById(entry.getKey()).getStatuses().get(model.getChoices().get(entry.getKey())));
                    }
                } else if (statusCode.get()==501){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Змінні не ініціалізовано");
                    alert.setHeaderText("Помилка 501. Відсутні дані для початку роботи.");
                    alert.setContentText("Для початку роботи ініціалізуйте хоча б одну змінну.");
                    alert.showAndWait();
                }
            }
        };
}
