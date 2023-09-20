package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ds.esb.expertsystembuilder.classes.bean.Decision;
import ds.esb.expertsystembuilder.classes.bean.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class DecisionContainer {
    private HashMap<Integer, Decision> decisions = new HashMap<>();

    public DecisionContainer() {
    }
    public DecisionContainer(HashMap<Integer, Decision> decisions) {
        this.decisions = decisions;
    }
    public HashMap<Integer, Decision> getDecisions() {return decisions;}
    public void setDecisions(HashMap<Integer, Decision> decisions) {this.decisions = decisions;}

    public int loadDecisionsFromJson(String path){
        try {
            Path file = FileSystems.getDefault().getPath(path+"/decisions.json");
            String json = Files.readString(file);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for(JsonElement el:array){
                JsonObject jo = el.getAsJsonObject();

            }
            return 200;
        } catch (IOException ex){
            ex.printStackTrace();
            return 500;
        }
    }
}
