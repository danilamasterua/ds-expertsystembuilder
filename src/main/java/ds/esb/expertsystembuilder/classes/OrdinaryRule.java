package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonObject;
import ds.esb.expertsystembuilder.classes.bean.Rule;

public class OrdinaryRule extends Rule {
    private int target;
    private int ifR;
    private int elseR;

    public OrdinaryRule() {
    }
    public OrdinaryRule(int queue, int target, int ifR, int elseR) {
        this.setQueue(queue);
        this.setIfR(ifR);
        this.setElseR(elseR);
        this.setTarget(target);
    }

    public int getIfR() {
        return ifR;
    }

    public void setIfR(int ifR) {
        this.ifR = ifR;
    }

    public int getElseR() {
        return elseR;
    }

    public void setElseR(int elseR) {
        this.elseR = elseR;
    }
    public int getTarget() {return target;}
    public void setTarget(int target) {this.target = target;}
    @Override
    public void getRuleFromJsonObject(JsonObject jo) {
        super.getRuleFromJsonObject(jo);
        this.setTarget(jo.get("target").getAsInt());
        this.setIfR(jo.get("if").getAsInt());
        this.setElseR(jo.get("else").getAsInt());
    }
}
