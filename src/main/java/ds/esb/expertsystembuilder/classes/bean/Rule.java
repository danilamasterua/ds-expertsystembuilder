package ds.esb.expertsystembuilder.classes.bean;

import com.google.gson.JsonObject;

import java.util.HashMap;

public abstract class Rule {
    private int queue;
    private int elseR;
    private int thenR;
    public Rule(int queue, int elseR, int thenR) {
        this.queue = queue;
        this.elseR = elseR;
        this.thenR = thenR;
    }
    public int getQueue() {
        return queue;
    }
    public void setQueue(int queue) {
        this.queue = queue;
    }
    public int getElseR() {return elseR;}
    public void setElseR(int elseR) {this.elseR = elseR;}
    public int getThenR() {return thenR;}
    public void setThenR(int thenR) {this.thenR = thenR;}

    public void getRuleFromJsonObject(JsonObject jsonObject){
        this.setQueue(jsonObject.get("queue").getAsInt());
    }
    public boolean checkRule(HashMap<Integer, Integer> values){return false;}
}
