package ds.esb.expertsystembuilder.classes.bean;

import com.google.gson.JsonObject;

public abstract class Rule {
    private int queue;

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public void getRuleFromJsonObject(JsonObject jsonObject){
        this.setQueue(jsonObject.get("queue").getAsInt());
    }
    public boolean checkRule(){return false;}
}
