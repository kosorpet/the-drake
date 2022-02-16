package thedrake.ui;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import thedrake.PlayingSide;
import thedrake.Troop;
import thedrake.TroopFace;


public class TroopInStackView extends Pane {

    private Troop troop;
    private final TileBackgrounds backgrounds = new TileBackgrounds();
    private final PlayingSide side;
    private final int index;
    private final GameViewContext gameContext;
    private final Border highlightBorder;

    public TroopInStackView(Troop troop, PlayingSide side, int index, GameViewContext gameContext){
        this.troop = troop;
        this.side = side;
        this.index = index;
        this.gameContext = gameContext;
        this.highlightBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2.0)));
        this.setPrefSize(100.0, 100.0);
        this.setOnMouseClicked(this::onClicked);
    }

    public void update() {
        this.setBackground(this.backgrounds.getTroop(this.troop, this.side, TroopFace.AVERS));
    }

    public void setTroop(Troop troop){
        this.troop = troop;
    }

    public Troop getTroop(){
        return this.troop;
    }

    public void highlight(){
        if (this.index == 0 && gameContext.gameState().sideOnTurn() == this.side) {
            this.gameContext.deHighlight();
            this.gameContext.setHighlightedInStack(this);
            this.gameContext.showMovesFromStack();
            this.setBorder(this.highlightBorder);
        }
    }

    public void deHighlight() {
        this.setBorder(null);
    }

    private void onClicked( MouseEvent event) {
        if(index == 0 && side == gameContext.gameState().sideOnTurn()) {
            gameContext.deHighlight();
            this.highlight();
        }
    }

}
