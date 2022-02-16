package thedrake.ui;
import javafx.scene.layout.BorderPane;

import thedrake.*;


import java.util.List;
import java.util.Objects;

public class GameView extends BorderPane implements GameViewContext {

    private final Controller controller;

    private GameState gameState;

    private final BoardView boardView;
    private final StackView blueStackView;
    private final StackView orangeStackView;
    private final TurnIndicator turnIndicator;

    private ValidMoves validMoves;

    private TileView highlighted;
    private TroopInStackView highlightedInStack;

    private final CapturedList capturedList;

    public GameView(GameState gameState, Controller controller){
        this.controller = controller;
        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);

        this.boardView = new BoardView(this);
        setCenter(boardView);

        this.orangeStackView = new StackView(PlayingSide.ORANGE, this);
        this.blueStackView = new StackView(PlayingSide.BLUE, this);
        setTop(orangeStackView);
        setBottom(blueStackView);

        this.turnIndicator = new TurnIndicator();
        setLeft(turnIndicator);

        this.capturedList = new CapturedList();
        setRight(capturedList);

        this.update();
    }

    public void update(){
        boardView.updateTiles();
        orangeStackView.update();
        blueStackView.update();
    }

    void checkCapture(Move move){
        if(gameState.tileAt(move.target()).hasTroop()){
            if(gameState.sideOnTurn() == PlayingSide.BLUE &&
                    gameState.army(PlayingSide.ORANGE).boardTroops().at(move.target()).isPresent()){
                TroopTile captured = gameState.army(PlayingSide.ORANGE).boardTroops().at(move.target()).get();
                if(Objects.equals(captured.troop().name(), "Drake")){
                    controller.showEndScreen(PlayingSide.BLUE);
                }
                capturedList.addUnit(captured);
            }
            if(gameState.sideOnTurn() == PlayingSide.ORANGE &&
                    gameState.army(PlayingSide.BLUE).boardTroops().at(move.target()).isPresent()){
                TroopTile captured = gameState.army(PlayingSide.BLUE).boardTroops().at(move.target()).get();
                if(Objects.equals(captured.troop().name(), "Drake")){
                    controller.showEndScreen(PlayingSide.ORANGE);
                }
                capturedList.addUnit(captured);
            }
        }
    }

    void checkBlockedGuard(){
        if(validMoves.movesFromStack().isEmpty() && gameState.sideOnTurn() == PlayingSide.BLUE){
            controller.showEndScreen(PlayingSide.ORANGE);
        }
        if(validMoves.movesFromStack().isEmpty() && gameState.sideOnTurn() == PlayingSide.ORANGE){
            controller.showEndScreen(PlayingSide.BLUE);
        }
    }

    @Override
    public void executeMove(Move move) {
        turnIndicator.flipPlayer();
        if(highlighted != null) {
            highlighted.unselect();
            highlighted = null;
            boardView.clearMoves();

            checkCapture(move);

            gameState = move.execute(gameState);
            validMoves = new ValidMoves(gameState);

            boardView.updateTiles();
            return;
        }
        if(highlightedInStack != null){
            highlightedInStack.deHighlight();
            highlightedInStack = null;
            boardView.clearMoves();
            gameState = move.execute(gameState);
            validMoves = new ValidMoves(gameState);

            checkBlockedGuard();

            boardView.updateTiles();
            blueStackView.update();
            orangeStackView.update();
        }
    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (highlighted != null && highlighted != tileView) {
            highlighted.unselect();
        }

        if(highlightedInStack != null)
            highlightedInStack.deHighlight();
        highlighted = tileView;

        boardView.clearMoves();
        boardView.showMoves(validMoves.boardMoves(tileView.position()));
    }

    @Override
    public void showMovesFromStack() {
        List<Move> moves = this.validMoves.movesFromStack();
        this.boardView.showMoves(moves);
    }

    @Override
    public void setHighlightedInStack(TroopInStackView troop) {
        highlightedInStack = troop;
    }

    @Override
    public void deHighlight() {
        if(highlighted != null)
            this.highlighted.unselect();
        this.highlighted = null;
        if(highlightedInStack != null)
            this.highlightedInStack.deHighlight();
        this.highlightedInStack = null;
    }

    @Override
    public GameState gameState() {
        return this.gameState;
    }

}
