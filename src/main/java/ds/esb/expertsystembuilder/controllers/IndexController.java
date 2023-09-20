package ds.esb.expertsystembuilder.controllers;

import ds.esb.expertsystembuilder.classes.Model;
import ds.esb.expertsystembuilder.classes.Variables;
import ds.esb.expertsystembuilder.classes.bean.Variable;
import ds.esb.expertsystembuilder.services.JsonWorks;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IndexController {
    @FXML
    public StatusBar statusBar;
    @FXML
    public TextArea variableStatusesTextArea;
    @FXML
    public MenuItem chooseProjectBtn;
    @FXML
    private Label debugLabel;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private VBox variablePane;
    private Map<String, ComboBox<String>> variablesComboBoxes = new HashMap<>();
    private Map<Integer, Integer> variablesResults = new HashMap<>();
    Model model = new Model();


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
        } catch (Exception ex){
            statusBar.setText(ex.getMessage());
        }
    }

    @FXML
    private void addVariablesFields(Variables variables){
        ArrayList<HBox> hboxes = new ArrayList<>();
        for (Map.Entry<Integer, Variable> entry:variables.getVariables().entrySet()){
            HBox var = new HBox(10);

            Label label = new Label(entry.getValue().getName());
            label.setMinWidth(50);
            label.setAlignment(Pos.BASELINE_RIGHT);
            ComboBox<String> status = new ComboBox<String>();
            status.setMinWidth(70);
            status.setId("var"+entry.getValue().getId());
            for (Map.Entry<Integer, String> entry1:entry.getValue().getStatuses().entrySet()){
                status.getItems().add(entry1.getValue());
            }
            var.getChildren().addAll(label, status);
            hboxes.add(var);
            variablesComboBoxes.put(String.valueOf(entry.getValue().getId()), status);
        }
        for (HBox hBox:hboxes){
            variablePane.getChildren().add(hBox);
        }
        Button button = new Button("Записати");
        button.setId("setVariablesBtn");
        variablePane.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(Map.Entry<String, ComboBox<String>> entry:variablesComboBoxes.entrySet()){
                    try {
                        int varId = Integer.parseInt(entry.getKey());
                        Variable variable = model.getVariables().getVariableById(varId);
                        variablesResults.put(varId, variable.searchStatusIdByValue(entry.getValue().getValue()));
                        statusBar.setText("Відповіді записано");
                    } catch (Exception e){
                        statusBar.setText(e.getMessage());
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Integer, Integer> entry:variablesResults.entrySet()){
                    try {
                        Variable variable=model.getVariables().getVariableById(entry.getKey());
                        sb.append(variable.getName()).append("->").append(variable.getStatuses().get(entry.getValue())).append("\n");
                    } catch (Exception e){
                        statusBar.setText(e.getMessage());
                    }
                }
                variableStatusesTextArea.setText(sb.toString());
            }
        });
    }
}
