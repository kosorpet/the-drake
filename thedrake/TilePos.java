package thedrake;

import java.io.PrintWriter;
import java.util.List;

public interface TilePos extends JSONSerializable {
	int i();
	int j();
	char column();
	int row();

	TilePos step(int columnStep, int rowStep);
	TilePos step(Offset2D step);
	List<? extends TilePos> neighbours();
	boolean isNextTo(TilePos pos);
	TilePos stepByPlayingSide(Offset2D dir, PlayingSide side);
	  	
	boolean equalsTo(int i, int j);
	
	TilePos OFF_BOARD = new TilePos() {

		@Override
		public void toJSON(PrintWriter writer) {
			writer.printf("\"off-board\"");
		}

		@Override
		public int i() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public int j() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public char column() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int row() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TilePos step(int columnStep, int rowStep) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TilePos step(Offset2D step) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<TilePos> neighbours() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNextTo(TilePos pos) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equalsTo(int i, int j) {
			return false;
		}
		
		@Override
		public String toString() {
			return "off-board";
		}
	};
}
