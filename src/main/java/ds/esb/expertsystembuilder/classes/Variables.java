package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ds.esb.expertsystembuilder.classes.bean.Variable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Variables {
    private HashMap<Integer, Variable> variables=new HashMap<>();

    public Variables() {
    }
    public Variables(HashMap<Integer, Variable> variables) {
        this.variables = variables;
    }
    public HashMap<Integer, Variable> getVariables() {return variables;}
    public void setVariables(HashMap<Integer, Variable> variables) {this.variables = variables;}

    public int loadVariablesFromJson(String folder){
        try {
            Path path = FileSystems.getDefault().getPath(folder+"/variables.json");
            String json = Files.readString(path);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement el:array){
                int id = el.getAsJsonObject().get("varId").getAsInt();
                Variable newVar = new Variable();
                newVar.getVariableFromJsonObject(el.getAsJsonObject());
                variables.put(id, newVar);
            }
            return 200;
        } catch (IOException e){
            e.printStackTrace();
        }
        return 500;
    }
    public String toString(){
        StringBuilder sb = new StringBuilder("[\n");
        for (Map.Entry<Integer, Variable> entry:variables.entrySet()){
            sb.append("'").append(entry.getKey()).append("': ").append(entry.getValue().toString());
        }
        sb.append("]");
        return sb.toString();
    }
    public ObservableList<Variable> toObservableList(){
        ObservableList<Variable> retList = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Variable> entry:this.variables.entrySet()){
            retList.add(entry.getValue());
        }
        return retList;
    }
    public Variable getVariableById(int id){
        return variables.get(id);
    }
}
