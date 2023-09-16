package ds.esb.expertsystembuilder.classes;

public class Model {
    private Variables variables;
    private RulesContainer rules;

    public Model() {}
    public Model(Variables variables, RulesContainer rules) {
        this.variables = variables;
        this.rules = rules;
    }
    public Variables getVariables() {return variables;}
    public void setVariables(Variables variables) {this.variables = variables;}
    public RulesContainer getRules() {return rules;}
    public void setRules(RulesContainer rules) {this.rules = rules;}


}
