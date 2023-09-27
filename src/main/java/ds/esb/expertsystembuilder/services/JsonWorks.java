package ds.esb.expertsystembuilder.services;

import ds.esb.expertsystembuilder.classes.Model;
import ds.esb.expertsystembuilder.classes.RulesContainer;
import ds.esb.expertsystembuilder.classes.Variables;
import ds.esb.expertsystembuilder.classes.DecisionContainer;

public class JsonWorks {
    public static Model loadProject(String path) throws RuntimeException{
        Variables variables = new Variables();
        RulesContainer rules = new RulesContainer();
        DecisionContainer decisions = new DecisionContainer();
        int result = variables.loadVariablesFromJson(path);
        if (result!=200){
            throw new RuntimeException("File with variables was not found, or has an incompatible format");
        } else {
            result = rules.loadRulesFromJson(path);
            if (result!=200){
                throw new RuntimeException("File with rules was not found, or has an incompatible format");
            } else {
                result = decisions.loadDecisionsFromJson(path);
                if (result!=200){
                    throw new RuntimeException("File with decisions was not found, or has an incompatible format");
                }
            }
        }
        return new Model(variables, rules, decisions);
    }
}
