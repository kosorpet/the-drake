package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) {
		this.playingSide = playingSide;
		this.troopMap = Collections.emptyMap();
		this.leaderPosition = TilePos.OFF_BOARD;
		this.guards = 0;
	}

	public BoardTroops(
			PlayingSide playingSide,
			Map<BoardPos, TroopTile> troopMap,
			TilePos leaderPosition, 
			int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		return Optional.ofNullable(troopMap.get(pos));
	}
	
	public PlayingSide playingSide() {
		return this.playingSide;
	}
	
	public TilePos leaderPosition() {
		return this.leaderPosition;
	}

	public int guards() {
		return this.guards;
	}
	
	public boolean isLeaderPlaced() {
		return this.leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards() {
		return isLeaderPlaced() && this.guards < 2;
	}	
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if(at(target).isPresent())
			throw new IllegalArgumentException();
		BoardTroops boardTroops;
		if(!isLeaderPlaced()) {
			boardTroops = new BoardTroops(this.playingSide, new HashMap<>(), target, this.guards);
		}
		else if(isPlacingGuards()) {
			boardTroops = new BoardTroops(this.playingSide, this.troopMap, this.leaderPosition, this.guards+1);
		}
		else{
			boardTroops = new BoardTroops(this.playingSide, this.troopMap, this.leaderPosition, this.guards);
		}

		TroopTile tile = new TroopTile(troop, boardTroops.playingSide, TroopFace.AVERS);
		boardTroops.troopMap.put(target, tile);
		return boardTroops;
	}

	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if(isPlacingGuards() || !isLeaderPlaced())
			throw new IllegalStateException();
		if(!troopMap.containsKey(origin) || at(target).isPresent())
			throw new IllegalArgumentException();

		BoardTroops boardTroops;
		if(leaderPosition.equals(origin)){
			boardTroops = new BoardTroops(this.playingSide, this.troopMap, target, this.guards);
		}
		else{
			boardTroops = new BoardTroops(this.playingSide, this.troopMap, this.leaderPosition, this.guards);
		}
		TroopTile toMove = boardTroops.troopMap.get(origin);
		boardTroops.troopMap.remove(origin);
		boardTroops.troopMap.put(target, toMove.flipped());
		return boardTroops;
	}
	
	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");			
		}
		
		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");			
		}
		
		if(!at(origin).isPresent())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
		if(isPlacingGuards() || !isLeaderPlaced())
			throw new IllegalStateException();
		if(at(target).isEmpty())
			throw new IllegalArgumentException();

		BoardTroops boardTroops;
		if(this.leaderPosition.equals(target)){
			boardTroops = new BoardTroops(this.playingSide, this.troopMap, TilePos.OFF_BOARD, this.guards);
		}
		else {
			boardTroops = new BoardTroops(this.playingSide, this.troopMap, this.leaderPosition, this.guards);
		}
		boardTroops.troopMap.remove(target);
		return boardTroops;
	}

	@Override
	public void toJSON(PrintWriter writer) {
		List<BoardPos> posList = new ArrayList<BoardPos>(troopMap.keySet());
		posList.sort(Comparator.comparing(BoardPos::toString));
		writer.printf("{");
		writer.printf("\"side\":");
		playingSide.toJSON(writer);
		writer.printf(",");
		writer.printf("\"leaderPosition\":");
		leaderPosition.toJSON(writer);
		writer.printf(",");
		writer.printf("\"guards\":");
		writer.printf("%d", guards);
		writer.printf(",");
		writer.printf("\"troopMap\":");
		writer.printf("{");
		for (BoardPos boardPos : posList) {
			boardPos.toJSON(writer);
			writer.printf(":");
			troopMap.get(boardPos).toJSON(writer);
			if(posList.indexOf(boardPos) == posList.size() -1)
				writer.printf("}");
			else
				writer.printf(",");
		}
		if(posList.isEmpty())
			writer.printf("}");
		writer.printf("}");
	}
}
