package thedrake;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable{
    ORANGE,
    BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("%c%s%c", '"', this, '"');
    }
}
