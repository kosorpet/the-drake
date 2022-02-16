package thedrake;

import java.io.PrintWriter;

public enum TroopFace implements JSONSerializable {
    AVERS,
    REVERS;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("%c%s%c", '"', this, '"');
    }
}
