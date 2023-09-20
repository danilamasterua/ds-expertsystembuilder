package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private ArrayList<Rule> rules = new ArrayList<>();

    public RulesContainer() {
    }
    public RulesContainer(ArrayList<Rule> rules) {this.rules = rules;}
    public ArrayList<Rule> getRules() {return rules;}
    public void setRules(ArrayList<Rule> rules) {this.rules = rules;}
    public void addRule(Rule rule){
        rules.add(rule);
        rules.sort(Comparator.comparingInt(Rule::getQueue));
    }
    public int loadRulesFromJson(String path){
        try {
            Path file = FileSystems.getDefault().getPath(path+"/rules.json");
            String json = Files.readString(file);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for(JsonElement el:array){
                JsonObject jo = el.getAsJsonObject();
                String type = jo.get("type").getAsString();
                Rule rule;
                if(type.equals("ordinary")){
                    rule = new OrdinaryRule();
                } else {
                    rule = new BinaryRule();
                }
                rule.getRuleFromJsonObject(jo);
                this.addRule(rule);
            }
            return 200;
        } catch (IOException e){
            e.printStackTrace();
            return 500;
        }
    }
}

