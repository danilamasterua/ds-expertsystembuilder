package ds.esb.expertsystembuilder.services;

import ds.esb.expertsystembuilder.classes.Model;
import ds.esb.expertsystembuilder.classes.RulesContainer;
import ds.esb.expertsystembuilder.classes.Variables;

public class JsonWorks {
    public static Model loadProject(String path) throws RuntimeException{
        Variables variables = new Variables();
        RulesContainer rules = new RulesContainer();
        int result = variables.loadVariablesFromJson(path);
        if (result!=200){
            throw new RuntimeException("File with variables was not found, or has an incompatible format");
        } else {
            result = rules.loadExpertRules(path);
            if (result!=200){
                throw new RuntimeException("File with rules was not found, or has an incompatible format");
            }
        }
        return new Model(variables, rules);
    }
}
