package ds.esb.expertsystembuilder.classes.bean;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Variable {
    private int id;
    private String name;
    private final HashMap<Integer, String> statuses = new HashMap<>();
    private VariableType variableType;

    public Variable() {
    }

    public VariableType getVariableType() {return variableType;}
    public void setVariableType(VariableType variableType) {this.variableType = variableType;}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, String> getStatuses() {
        return statuses;
    }

//    public void setStatuses(HashMap<Integer, String> statuses) {this.statuses = statuses;}


    public void getVariableFromJsonObject(JsonObject jsonObject){
        this.setId(jsonObject.get("varId").getAsInt());
        this.setName(jsonObject.get("varName").getAsString());
        this.setVariableType(VariableType.valueOf(jsonObject.get("varType").getAsString().toUpperCase()));
        JsonArray jsonArray = jsonObject.get("varStatuses").getAsJsonArray();
        for(JsonElement jo:jsonArray){
            this.statuses.put(jo.getAsJsonObject().get("statusId").getAsInt(), jo.getAsJsonObject().get("statusName").getAsString());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\n");
        sb.append("'name: '").append(this.getName()).append("\n'statuses: ['");
        for (Map.Entry<Integer, String> entry:this.statuses.entrySet()){
            sb.append("\n{").append("'status': ").append(entry.getValue()).append("}");
        }
        sb.append("\n}");
        return sb.toString();
    }

    public int searchStatusIdByValue(String value){
        int id=-1;
        for(Map.Entry<Integer, String> entry: statuses.entrySet()){
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return id;
    }
}
