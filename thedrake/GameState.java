package thedrake;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

public class GameState implements JSONSerializable {
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState(
			Board board, 
			Army blueArmy, 
			Army orangeArmy, 
			PlayingSide sideOnTurn, 
			GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}
	
	public Board board() {
		return board;
	}
	
	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}
	
	public GameResult result() {
		return result;
	}
	
	public Army army(PlayingSide side) {
		if(side == PlayingSide.BLUE) {
			return blueArmy;
		}
		
		return orangeArmy;
	}
	
	public Army armyOnTurn() {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn() {
		if(sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;
		
		return blueArmy;
	}
	
	public Tile tileAt(TilePos pos) {
		if(!blueArmy.boardTroops().at(pos).isPresent() && !orangeArmy.boardTroops().at(pos).isPresent())
			return board().at(pos);
		if(blueArmy.boardTroops().at(pos).isPresent())
			return blueArmy.boardTroops().at(pos).get();
		else
			return orangeArmy.boardTroops().at(pos).get();
	}
	
	private boolean canStepFrom(TilePos origin) {
		return result == GameResult.IN_PLAY && origin != TilePos.OFF_BOARD
				&& (sideOnTurn != PlayingSide.BLUE || blueArmy.boardTroops().at(origin).isPresent())
				&& (sideOnTurn != PlayingSide.ORANGE || orangeArmy.boardTroops().at(origin).isPresent())
				&& (sideOnTurn != PlayingSide.BLUE || blueArmy.boardTroops().at(origin).isPresent())
				&& (sideOnTurn != PlayingSide.ORANGE || orangeArmy.boardTroops().at(origin).isPresent())
				&& blueArmy.boardTroops().isLeaderPlaced() && orangeArmy.boardTroops().isLeaderPlaced()
				&& !blueArmy.boardTroops().isPlacingGuards() && !orangeArmy.boardTroops().isPlacingGuards()
				&& (sideOnTurn != PlayingSide.BLUE || orangeArmy.boardTroops().at(origin).isEmpty())
				&& (sideOnTurn != PlayingSide.ORANGE || blueArmy.boardTroops().at(origin).isEmpty());
	}

	private boolean canStepTo(TilePos target) {
		return target != TilePos.OFF_BOARD && result == GameResult.IN_PLAY && blueArmy.boardTroops().at(target).isEmpty() && orangeArmy.boardTroops().at(target).isEmpty()
				&& board.at(target).canStepOn();
	}
	
	private boolean canCaptureOn(TilePos target) {
		return result == GameResult.IN_PLAY && (blueArmy.boardTroops().at(target).isPresent() || orangeArmy.boardTroops().at(target).isPresent())
				&& (sideOnTurn != PlayingSide.ORANGE || blueArmy.boardTroops().at(target).isPresent())
				&& (sideOnTurn != PlayingSide.BLUE || orangeArmy.boardTroops().at(target).isPresent());
	}
	
	public boolean canStep(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}
	
	public boolean canPlaceFromStack(TilePos target) {
		if(target == TilePos.OFF_BOARD || result != GameResult.IN_PLAY || !board.at(target).canStepOn() || board.at(target).hasTroop()
		        || (sideOnTurn == PlayingSide.BLUE && blueArmy.stack().isEmpty())
				|| (sideOnTurn == PlayingSide.ORANGE && orangeArmy.stack().isEmpty())
		        || blueArmy.boardTroops().at(target).isPresent() || orangeArmy.boardTroops().at(target).isPresent()){
			return false;
		}
		if(sideOnTurn.equals(PlayingSide.ORANGE) && !orangeArmy.boardTroops().isLeaderPlaced() && target.row() != board().dimension())
			return false;
		if(sideOnTurn.equals(PlayingSide.BLUE) && !blueArmy.boardTroops().isLeaderPlaced() && target.row() != 1)
			return false;

		if(sideOnTurn.equals(PlayingSide.BLUE) && blueArmy.boardTroops().isLeaderPlaced()){
			if(blueArmy.boardTroops().isPlacingGuards()){
				for(int i = 0; i < target.neighbours().size(); i++){
					if(target.neighbours().get(i).equals(blueArmy.boardTroops().leaderPosition())) {
						return true;
					}
				}
				return false;
			}
			else{
				for(int i = 0; i < target.neighbours().size(); i++){
					if(blueArmy.boardTroops().at(target.neighbours().get(i)).isPresent())
						return true;
				}
				return false;
			}
		}

		if(sideOnTurn.equals(PlayingSide.ORANGE) && orangeArmy.boardTroops().isLeaderPlaced()){
			if(orangeArmy.boardTroops().isPlacingGuards()){
				for(int i = 0; i < target.neighbours().size(); i++){
					if(target.neighbours().get(i).equals(orangeArmy.boardTroops().leaderPosition()))
						return true;
				}
				return false;
			}
			else{
				for(int i = 0; i < target.neighbours().size(); i++){
					if(orangeArmy.boardTroops().at(target.neighbours().get(i)).isPresent())
						return true;
				}
				return false;
			}
		}
		return true;
	}
	
	public GameState stepOnly(BoardPos origin, BoardPos target) {		
		if(canStep(origin, target))		 
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
		
		throw new IllegalArgumentException();
	}
	
	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target), 
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if(canPlaceFromStack(target)) {
			return createNewGameState(
					armyNotOnTurn(), 
					armyOnTurn().placeFromStack(target), 
					GameResult.IN_PLAY);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign() {
		return createNewGameState(
				armyNotOnTurn(), 
				armyOnTurn(), 
				GameResult.VICTORY);
	}
	
	public GameState draw() {
		return createNewGameState(
				armyOnTurn(), 
				armyNotOnTurn(), 
				GameResult.DRAW);
	}
	
	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if(armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result); 
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{");
		writer.printf("\"result\":");
		result.toJSON(writer);
		writer.printf(",");
		writer.printf("\"board\":");
		board.toJSON(writer);
		writer.printf(",");
		writer.printf("\"blueArmy\":");
		blueArmy.toJSON(writer);
		writer.printf(",");
		writer.printf("\"orangeArmy\":");
		orangeArmy.toJSON(writer);
		writer.printf("}");
	}
}
