package thedrake;

import java.awt.desktop.PreferencesEvent;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TroopTile implements Tile, JSONSerializable{
    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side(){
        return this.side;
    }

    public TroopFace face(){
        return this.face;
    }

    public Troop troop(){
        return this.troop;
    }

    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> moves = new ArrayList<>();
        for(int i = 0; i < troop.actions(face).size(); i++) {
            moves.addAll(troop.actions(face).get(i).movesFrom(pos, side, state));
        }
        return moves;
    }

    public TroopTile flipped(){
        return new TroopTile(this.troop, this.side, this.face == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{");
        writer.printf("\"troop\":");
        troop.toJSON(writer);
        writer.printf(",");
        writer.printf("\"side\":");
        side.toJSON(writer);
        writer.printf(",");
        writer.printf("\"face\":");
        face.toJSON(writer);
        writer.printf("}");
    }
}
