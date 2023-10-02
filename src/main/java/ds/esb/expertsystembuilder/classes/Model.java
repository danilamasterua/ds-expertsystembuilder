package ds.esb.expertsystembuilder.classes;

import ds.esb.expertsystembuilder.classes.bean.Decision;
import ds.esb.expertsystembuilder.classes.bean.Rule;
import ds.esb.expertsystembuilder.classes.bean.Variable;
import java.util.HashMap;

public class Model {
    private Variables variables;
    private RulesContainer rules;
    private DecisionContainer decisions;
    private Decision decision;
    private HashMap<Integer, Integer> choices = new HashMap<>();

    public Model(Variables variables, RulesContainer rules, DecisionContainer decisions) {
        this.variables = variables;
        this.rules = rules;
        this.decisions = decisions;
    }
    public Model() {}
    public Model(Variables variables, RulesContainer rules) {
        this.variables = variables;
        this.rules = rules;
    }
    public Variables getVariables() {return variables;}
    public void setVariables(Variables variables) {this.variables = variables;}
    public RulesContainer getRules() {return rules;}
    public void setRules(RulesContainer rules) {this.rules = rules;}
    public DecisionContainer getDecisions() {return decisions;}
    public void setDecisions(DecisionContainer decisions) {this.decisions = decisions;}
    public Decision getDecision() {return decision;}
    public void setDecision(Decision decision) {this.decision = decision;}
    public HashMap<Integer, Integer> getChoices() {return choices;}
    public void setChoices(HashMap<Integer, Integer> choices) {this.choices = choices;}

    public void putChoice(Integer varId, Integer chId){
        this.choices.put(varId, chId);
    }
    
    public String doLogic(){
            StringBuilder sb = new StringBuilder();
            for(Rule entry:rules.getRules()){
                if(entry instanceof OrdinaryRule ordinaryRule){
                    Variable variable = variables.getVariableById(ordinaryRule.getTarget());
                    sb.append(">> Якщо ").append(variable.getName()).append(" - ").append(variable.getStatuses().get(ordinaryRule.getIfR()))
                            .append(" то ").append(decisions.getDecisions().get(entry.getThenR()).getInference()).append("\n");
                    sb.append("== ").append(variable.getName()).append(" - ").append(variable.getStatuses().get(choices.get(variable.getId()))).append(" тому ");
                    if(choices.get(variable.getId())==((OrdinaryRule) entry).getIfR()){
                        sb.append(decisions.getDecisions().get(entry.getThenR()).getInference()).append("\n");
                        this.decision = decisions.getDecisions().get(entry.getThenR());
                    } else {
                        sb.append(decisions.getDecisions().get(entry.getElseR()).getInference()).append("\n");
                        this.decision = decisions.getDecisions().get(entry.getElseR());
                        break;
                    }
                }
                if(entry instanceof BinaryRule binaryRule){
                    Variable var1 = variables.getVariableById(binaryRule.getCompared1());
                    Variable var2 = variables.getVariableById(binaryRule.getCompared2());
                    if(binaryRule.getbType().equals("and")){
                        sb.append(">> Якщо ").append(var1.getName()).append(" - ").append(var1.getStatuses().get(Integer.parseInt(binaryRule.getIfR1())))
                                .append(" та ").append(var2.getName()).append(" - ").append(var2.getStatuses().get(Integer.parseInt(binaryRule.getIfR1())))
                                .append(" то ").append(decisions.getDecisions().get(entry.getThenR()).getInference()).append("\n");
                        sb.append("== ").append(var1.getName()).append(" - ").append(var1.getStatuses().get(choices.get(var1.getId())))
                                .append(" та ").append(var2.getName()).append(" - ").append(var2.getStatuses().get(choices.get(var2.getId())))
                                .append(" тому ");
                        if(choices.get(var1.getId())==Integer.parseInt(((BinaryRule) entry).getIfR1())&&
                                choices.get(var2.getId())==Integer.parseInt(((BinaryRule) entry).getIfR2())){
                            sb.append(decisions.getDecisions().get(entry.getThenR()).getInference()).append("\n");
                            this.decision = decisions.getDecisions().get(entry.getThenR());
                        } else {
                            sb.append(decisions.getDecisions().get(entry.getElseR()).getInference()).append("\n");
                            this.decision = decisions.getDecisions().get(entry.getElseR());
                            break;
                        }
                    }
                }
            }
            return sb.toString();
    }
}
