package thedrake;

import java.io.PrintWriter;

public interface JSONSerializable {
	void toJSON(PrintWriter writer);
}
