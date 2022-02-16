package thedrake.ui;

import thedrake.GameState;
import thedrake.Move;

public interface GameViewContext {

    void executeMove(Move move);

    void tileViewSelected(TileView tileView);

    void showMovesFromStack();

    void setHighlightedInStack(TroopInStackView troop);

    void deHighlight();

    GameState gameState();
}
