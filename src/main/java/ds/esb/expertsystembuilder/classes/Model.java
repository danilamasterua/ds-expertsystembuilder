package ds.esb.expertsystembuilder.classes;

import ds.esb.expertsystembuilder.classes.bean.Decision;
import ds.esb.expertsystembuilder.classes.bean.Rule;
import ds.esb.expertsystembuilder.classes.bean.Variable;
import ds.esb.expertsystembuilder.classes.bean.VariableType;
import ds.esb.expertsystembuilder.classes.deprecated.DecisionContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    private Variables variables;
    private RulesContainer rules;
    private HashMap<Integer, Integer> choices = new HashMap<>();
    private ArrayList<Integer> processedRules = new ArrayList<>();
    private int lastRule;

    public Model(Variables variables, RulesContainer rules) {
        this.variables = variables;
        this.rules = rules;
    }
    public Model() {}
    public Variables getVariables() {return variables;}
    public void setVariables(Variables variables) {this.variables = variables;}
    public RulesContainer getRules() {return rules;}
    public void setRules(RulesContainer rules) {this.rules = rules;}
    public HashMap<Integer, Integer> getChoices() {return choices;}
    public void setChoices(HashMap<Integer, Integer> choices) {this.choices = choices;}
    public ArrayList<Integer> getProcessedRules() {return processedRules;}
    public void setProcessedRules(ArrayList<Integer> processedRules) {this.processedRules = processedRules;}
    public int getLastRule() {return lastRule;}
    public void setLastRule(int lastRule) {this.lastRule = lastRule;}
    public void putChoice(Integer varId, Integer chId){
        this.choices.put(varId, chId);
    }

    public String doExpertLogic(){
        StringBuilder sb = new StringBuilder();
        variables.getVariables().forEach((integer, variable) -> {
            if(choices.containsKey(integer)){
                rules.getRules().forEach(rule -> {
                    if(rule instanceof ExpertRule eRule){
                        if(eRule.getVarIf().containsKey(integer)&&!processedRules.contains(eRule.getQueue())){
                            processedRules.add(eRule.getQueue());
                            if(eRule.checkRule(this)){
                                sb.append(printExpertRule(eRule));
                                choices.put(eRule.getTarget(), eRule.getThenR());
                                lastRule = eRule.getQueue();
                            }
                        }
                    }
                });
            }
        });
        return sb.toString();
    }

    public String printExpertRule(ExpertRule rule){
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Правило №").append(rule.getQueue()).append(" --- Якщо ");
        for (var vif:rule.getVarIf().entrySet()){
            Variable variable = this.variables.getVariableById(vif.getKey());
            sb.append(variable.getName()).append(" - ").append(variable.getStatuses().get(vif.getValue()));
            counter++;
            if (counter!=rule.getVarIf().size()) {
                switch (rule.getType()) {
                    case OR -> sb.append(" aбо ");
                    case AND -> sb.append(" i ");
                }
            }
        }
        Variable targetVariable = variables.getVariableById(rule.getTarget());
        sb.append(" то ").append(targetVariable.getName()).append(" - ").append(targetVariable.getStatuses().get(rule.getThenR())).append("\n");
        return sb.toString();
    }

    public String printDecisionVariables(){
        StringBuilder sb = new StringBuilder("Статус змінних:\n");
        for(var variable:variables.getVariables().entrySet()){
            sb.append(variable.getValue().getName()).append(" - ").append(variable.getValue().getStatuses().get(choices.get(variable.getKey()))).append("\n");
        }
        return sb.toString();
    }

    public String printDecision(){
        StringBuilder sb = new StringBuilder();
        Rule rule = getRules().getRuleById(lastRule);
        if (rule instanceof ExpertRule eRule){
            sb.append(variables.getVariableById(eRule.getTarget()).getName()).append(" - ")
                    .append(variables.getVariableById(eRule.getTarget()).getStatuses().get(eRule.getThenR()));
        }
        return sb.toString();
    }
}
