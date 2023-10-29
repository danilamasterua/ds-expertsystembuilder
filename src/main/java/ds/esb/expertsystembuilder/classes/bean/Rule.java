package ds.esb.expertsystembuilder.classes.bean;

import com.google.gson.JsonObject;

import ds.esb.expertsystembuilder.classes.Model;

/**
 * Parent class for rules.
 */
public abstract class Rule {
    private int queue;
    private int thenR;

    public Rule() {}
    public int getQueue() {
        return queue;
    }
    public void setQueue(int queue) {
        this.queue = queue;
    }
    public int getThenR() {return thenR;}
    public void setThenR(int thenR) {this.thenR = thenR;}

    public void getRuleFromJsonObject(JsonObject jsonObject){
        this.setQueue(jsonObject.get("queue").getAsInt());
        this.setThenR(jsonObject.get("then").getAsInt());
    }
    public boolean checkRule(Model model){return false;}
}
