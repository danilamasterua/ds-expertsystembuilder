package ds.esb.expertsystembuilder.classes.bean;

import com.google.gson.JsonObject;

public class Decision {
    private int id;
    private String inference;

    public Decision() {
    }
    public Decision(int id, String inference) {
        this.id = id;
        this.inference = inference;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getInference() {return inference;}
    public void setInference(String inference) {this.inference = inference;}
    public void getDecisionFromJsonObject(JsonObject jo){
        this.setId(jo.get("decisionId").getAsInt());
        this.setInference(jo.get("inference").getAsString());
    }
}
