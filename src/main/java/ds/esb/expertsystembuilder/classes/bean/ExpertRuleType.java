package ds.esb.expertsystembuilder.classes.bean;

import ds.esb.expertsystembuilder.classes.Model;
import javafx.scene.control.ChoiceDialog;

import java.util.*;

public enum ExpertRuleType {
    AND {
        @Override
        public boolean check(HashMap<Integer, Integer> varIf, Model model) {
            boolean ret = false;
            ExpertRuleType.checkVariables(varIf, model);
            for (Map.Entry<Integer, Integer> entry:varIf.entrySet()) {
                if (entry.getValue().equals(model.getChoices().get(entry.getKey()))&&model.getChoices().get(entry.getKey())!=null){
                    ret=true;
                } else {
                    ret=false;
                    break;
                }
            }
            return ret;
        }
    },
    OR {
        @Override
        public boolean check(HashMap<Integer, Integer> varIf, Model model) {
            boolean ret = false;
            ExpertRuleType.checkVariables(varIf, model);
            for (Map.Entry<Integer, Integer> entry:varIf.entrySet()) {
                if (entry.getValue().equals(model.getChoices().get(entry.getKey()))){
                    ret=true;
                    break;
                }
            }
            return ret;
        }
    };
    public abstract boolean check(HashMap<Integer, Integer> varIf, Model model);
    private static Optional<String> getResult(Variable variable) {
        List<String> choices = new ArrayList<>();
        for (var etr: variable.getStatuses().entrySet()){
            choices.add(etr.getValue());
        }
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("-", choices);
        choiceDialog.setTitle("Ініціалізація змінної");
        choiceDialog.setHeaderText("Ініціалізуйте змінну "+ variable.getName());
        choiceDialog.setContentText(variable.getName());
        return choiceDialog.showAndWait();
    }
    private static void checkVariables(HashMap<Integer, Integer> varIf, Model model){
        for (var entry:varIf.entrySet()){
            if(!model.getChoices().containsKey(entry.getKey())){
                Variable variable = model.getVariables().getVariableById(entry.getKey());
                Optional<String> result = getResult(variable);
                result.ifPresent((String res) -> model.putChoice(entry.getKey(), model.getVariables().getVariableById(entry.getKey()).searchStatusIdByValue(res)));
            }
        }
    }
}
