package thedrake;

import javax.print.DocFlavor;
import java.io.PrintWriter;

public class Board implements JSONSerializable {

	// Konstruktor. Vytvoří čtvercovou hrací desku zadaného rozměru, kde všechny dlaždice jsou prázdné, tedy BoardTile.EMPTY
	public Board(int dimension) {
		this.dimension = dimension;
		this.tiles = new BoardTile[this.dimension][this.dimension];
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				this.tiles[i][j] = BoardTile.EMPTY;
			}
		}
	}

	// Rozměr hrací desky
	public int dimension() {
		return this.dimension;
	}

	// Vrací dlaždici na zvolené pozici.
	public BoardTile at(TilePos pos) {
		return this.tiles[pos.i()][pos.j()];
	}

	// Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
	public Board withTiles(TileAt ...ats) {
		Board newBoard = new Board(this.dimension);
		for (int i = 0; i < this.tiles.length; i++) {
			newBoard.tiles[i] = this.tiles[i].clone();
		}

		for (TileAt at : ats) {
			newBoard.tiles[at.pos.i()][at.pos.j()] = at.tile;
		}
		return newBoard;
	}

	// Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
	public PositionFactory positionFactory() {
		return new PositionFactory(this.dimension);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{");
		writer.printf("\"dimension\":%d,", dimension);
		writer.printf("\"tiles\":[");
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[j][i].toJSON(writer);
				if(j != tiles[j].length -1){
					writer.printf(",");
				}
			}
			if(i == tiles.length -1){
				writer.printf("]");
			}
			else {
				writer.printf(",");
			}
		}
		writer.printf("}");
	}

	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
	private final int dimension;
	private final BoardTile[][] tiles;
}

