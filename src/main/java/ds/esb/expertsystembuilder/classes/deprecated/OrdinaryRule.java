//package ds.esb.expertsystembuilder.classes;
//
//import com.google.gson.JsonObject;
//import ds.esb.expertsystembuilder.classes.bean.Rule;
//
//
///**
// * Class for ordinary rule. Use only with deprecated method <i>Model.doLogic</i>. Do not recommend to use.
// */
//public class OrdinaryRule extends Rule {
//    private int target;
//    private int ifR;
//    public OrdinaryRule() {
//        super(0,0,0);
//    }
//    public OrdinaryRule(int queue, int target, int ifR, int elseR, int thenR) {
//        super(queue, elseR, thenR);
//        this.setQueue(queue);
//        this.setIfR(ifR);
//        this.setTarget(target);
//    }
//    public int getIfR() {
//        return ifR;
//    }
//    public void setIfR(int ifR) {
//        this.ifR = ifR;
//    }
//    public int getTarget() {return target;}
//    public void setTarget(int target) {this.target = target;}
//    @Override
//    public void getRuleFromJsonObject(JsonObject jo) {
//        super.getRuleFromJsonObject(jo);
//        this.setTarget(jo.get("target").getAsInt());
//        this.setIfR(jo.get("if").getAsInt());
//    }
//    @Override
//    public boolean checkRule(Model model){
//        return false;
//    }
//}