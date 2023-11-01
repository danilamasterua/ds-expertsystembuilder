package ds.esb.expertsystembuilder.classes;

import ds.esb.expertsystembuilder.classes.bean.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Model {
    private Variables variables;
    private RulesContainer rules;
    private HashMap<Integer, Integer> choices = new HashMap<>();
    private ArrayList<Integer> processedRules = new ArrayList<>();
    private Integer point;

    public Model(Variables variables, RulesContainer rules) {
        this.variables = variables;
        this.rules = rules;
    }
    public Model() {}
    public Variables getVariables() {return variables;}
//    public void setVariables(Variables variables) {this.variables = variables;}

//public RulesContainer getRules() {return rules;}

//    public void setRules(RulesContainer rules) {this.rules = rules;}
    public HashMap<Integer, Integer> getChoices() {return choices;}
    public void setChoices(HashMap<Integer, Integer> choices) {this.choices = choices;}
//    public ArrayList<Integer> getProcessedRules() {return processedRules;}
    public void setProcessedRules(ArrayList<Integer> processedRules) {this.processedRules = processedRules;}
//    public void setLastRule(int lastRule) {this.lastRule = lastRule;}
    public void putChoice(Integer varId, Integer chId){
        this.choices.put(varId, chId);
    }
    public void setPoint(Integer point){this.point=point;}

    public String doExpertLogic(){
        StringBuilder sb = new StringBuilder();
        AtomicBoolean isOk = new AtomicBoolean(false);
        for (var rule:rules.getRules()){
            assert rule instanceof ExpertRule;
                if (((ExpertRule) rule).getTarget() == point) {
                    isOk.set(true);
                    processedRules.add(rule.getQueue());
                    sb.append(printExpertRule((ExpertRule) rule));
                    if (rule.checkRule(this)) {
                        choices.put(((ExpertRule) rule).getTarget(), rule.getThenR());
                        break;
                    }
                }
        }
        if(!isOk.get()){
            sb.append("This variable is not found as target\n");
        }
        return sb.toString();
    }

    public String printExpertRule(ExpertRule rule){
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Rule №").append(rule.getQueue()).append(" --- If ");
        for (var vif:rule.getVarIf().entrySet()){
            Variable variable = this.variables.getVariableById(vif.getKey());
            sb.append(variable.getName()).append(" - ").append(variable.getStatuses().get(vif.getValue()));
            counter++;
            if (counter!=rule.getVarIf().size()) {
                switch (rule.getType()) {
                    case OR -> sb.append(" or ");
                    case AND -> sb.append(" and ");
                }
            }
        }
        Variable targetVariable = variables.getVariableById(rule.getTarget());
        sb.append(" then ").append(targetVariable.getName()).append(" - ").append(targetVariable.getStatuses().get(rule.getThenR())).append("\n");
        return sb.toString();
    }

    public String printDecisionVariables(){
        StringBuilder sb = new StringBuilder("Variables states:\n");
        for(var variable:variables.getVariables().entrySet()){
            sb.append(variable.getValue().getName()).append(" - ").append(variable.getValue().getStatuses().get(choices.get(variable.getKey()))).append("\n");
        }
        return sb.toString();
    }

    public String printDecision(){
        return variables.getVariableById(point).getName() + " - "
                + variables.getVariableById(point).getStatuses().get(choices.get(point));
    }
}
