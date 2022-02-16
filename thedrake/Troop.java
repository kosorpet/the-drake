package thedrake;

import java.io.PrintWriter;
import java.util.List;

public class Troop implements JSONSerializable{
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversAction, List<TroopAction> reversAction){
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversAction = aversAction;
        this.reversAction = reversAction;
    }
    public Troop(String name, Offset2D pivot, List<TroopAction> aversAction, List<TroopAction> reversAction){
        this.name = name;
        this.reversPivot = pivot;
        this.aversPivot = pivot;
        this.aversAction = aversAction;
        this.reversAction = reversAction;
    }
    public Troop(String name, List<TroopAction> aversAction, List<TroopAction> reversAction){
        this.name = name;
        this.aversPivot = new Offset2D(1, 1);
        this.reversPivot = new Offset2D(1, 1);
        this.aversAction = aversAction;
        this.reversAction = reversAction;
    }
    public String name(){
        return this.name;
    }
    public Offset2D pivot(TroopFace face){
        if (face == TroopFace.AVERS)
            return this.aversPivot;
        return this.reversPivot;
    }
    public List<TroopAction> actions(TroopFace face){
        if(face == TroopFace.AVERS)
            return aversAction;
        return reversAction;
    }
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    private final List<TroopAction> aversAction;
    private final List<TroopAction> reversAction;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("%c%s%c", '"', name, '"');
    }
}
