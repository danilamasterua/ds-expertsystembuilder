package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ds.esb.expertsystembuilder.classes.bean.Rule;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

public class RulesContainer {
    private final ArrayList<Rule> rules = new ArrayList<>();

    public RulesContainer() {
    }
//    public RulesContainer(ArrayList<Rule> rules) {this.rules = rules;}
    public ArrayList<Rule> getRules() {return rules;}
//    public void setRules(ArrayList<Rule> rules) {this.rules = rules;}
    public void addRule(Rule rule){
        rules.add(rule);
        rules.sort(Comparator.comparingInt(Rule::getQueue));
    }

    public int loadExpertRules(String path){
        try{
            Path file = FileSystems.getDefault().getPath(path+"/rules.json");
            String json = Files.readString(file);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for(var el:array){
                JsonObject jo = el.getAsJsonObject();
                Rule rule = new ExpertRule();
                rule.getRuleFromJsonObject(jo);
                this.addRule(rule);
            }
            return 200;
        } catch (IOException e){
            return 500;
        }
    }

    public Rule getRuleById(int id){
        Rule ruleD = null;
        for (Rule rule: this.rules) {
            if(rule.getQueue()==id){
                 ruleD=rule;
            }
        }
        if (ruleD==null){
            throw new RuntimeException("Rule with id="+id+" is not found");
        } else {
            return ruleD;
        }
    }
}

