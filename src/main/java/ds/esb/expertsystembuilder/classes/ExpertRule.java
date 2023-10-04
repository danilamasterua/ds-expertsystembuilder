package ds.esb.expertsystembuilder.classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ds.esb.expertsystembuilder.classes.bean.ExpertRuleType;
import ds.esb.expertsystembuilder.classes.bean.Rule;

import java.util.HashMap;

public class ExpertRule extends Rule {
    private HashMap<Integer, Integer> varIf = new HashMap<>();
    private ExpertRuleType type;
    private int target;


    public ExpertRule() {super();}
    public ExpertRule(int queue, int thenR) {
        super(queue, thenR);
    }
    public HashMap<Integer, Integer> getVarIf() {return varIf;}
    public void setVarIf(HashMap<Integer, Integer> varIf) {this.varIf = varIf;}
    public ExpertRuleType getType() {return type;}
    public void setType(ExpertRuleType type) {this.type = type;}
    public int getTarget() {return target;}
    public void setTarget(int target) {this.target = target;}

    public void getRuleFromJsonObject(JsonObject jo){
        super.getRuleFromJsonObject(jo);
        JsonArray jsonArray = jo.get("varIf").getAsJsonArray();
        for (var entry:jsonArray){
            JsonObject jsonObject = entry.getAsJsonObject();
            varIf.put(jsonObject.get("variableId").getAsInt(), jsonObject.get("statusId").getAsInt());
        }
        this.type = ExpertRuleType.valueOf(jo.get("type").getAsString().toUpperCase());
        this.target = jo.get("target").getAsInt();
    }

    @Override
    public boolean checkRule(Model model) {
        return this.type.check(this.varIf, model);
    }
}
