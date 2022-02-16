package thedrake.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import thedrake.*;

import java.util.List;

public class StackView extends HBox {
    private List<Troop> stack;
    private final PlayingSide side;
    private final GameViewContext gameContext;

    public StackView(PlayingSide side, GameViewContext gameContext){
        this.side = side;
        this.stack = gameContext.gameState().army(side).stack();
        this.gameContext = gameContext;
        this.setPadding(new Insets(5.0));
        this.setSpacing(5.0);
    }

    public void update(){
        this.stack = gameContext.gameState().army(side).stack();
        this.getChildren().clear();
        for(int i = 0; i < this.stack.size(); i++){
            TroopInStackView newView = new TroopInStackView(this.stack.get(i), this.side, i, this.gameContext);
            this.getChildren().add(newView);
            newView.update();
        }
    }
}
