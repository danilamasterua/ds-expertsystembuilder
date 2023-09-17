package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonObject;
import ds.esb.expertsystembuilder.classes.bean.Rule;

import java.util.HashMap;

public class BinaryRule extends Rule {
    private int compared1;
    private int compared2;
    private int ifR1;
    private int ifR2;

    public BinaryRule() {
        super(0,0,0);
    }

    public BinaryRule(int queue, int compared1, int compared2, int ifR1, int ifR2, int elseR, int thenR) {
        super(queue, elseR, thenR);
        this.compared1 = compared1;
        this.compared2 = compared2;
        this.ifR1 = ifR1;
        this.ifR2 = ifR2;
    }

    public int getCompared1() {
        return compared1;
    }

    public void setCompared1(int compared1) {
        this.compared1 = compared1;
    }

    public int getCompared2() {
        return compared2;
    }

    public void setCompared2(int compared2) {
        this.compared2 = compared2;
    }

    public int getIfR1() {
        return ifR1;
    }

    public void setIfR1(int ifR1) {
        this.ifR1 = ifR1;
    }

    public int getIfR2() {
        return ifR2;
    }

    public void setIfR2(int ifR2) {
        this.ifR2 = ifR2;
    }

    @Override
    public void getRuleFromJsonObject(JsonObject jsonObject) {
        super.getRuleFromJsonObject(jsonObject);
    }

    @Override
    public boolean checkRule(HashMap<Integer, Integer> values) {
        return super.checkRule(values);
    }
}
