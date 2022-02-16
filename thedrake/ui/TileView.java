package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import thedrake.BoardPos;
import thedrake.Move;
import thedrake.Tile;

public class TileView extends Pane {

    private final BoardPos boardPos;

    private Tile tile;

    private final TileBackgrounds backgrounds = new TileBackgrounds();

    private final Border selectBorder = new Border(
        new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private final GameViewContext gameContext;

    private Move move;

    private final ImageView moveImage;

    public TileView(BoardPos boardPos, Tile tile, GameViewContext gameContext) {
        this.boardPos = boardPos;
        this.tile = tile;
        this.gameContext = gameContext;

        setPrefSize(100, 100);
        update();

        setOnMouseClicked(e -> onClick());

        moveImage = new ImageView(getClass().getResource("/assets/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);
    }

    private void onClick() {
        if (move != null)
            gameContext.executeMove(move);
        else if (tile.hasTroop())
            select();
    }

    public void select() {
        setBorder(selectBorder);
        gameContext.tileViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }

    public void update() {
        setBackground(backgrounds.get(tile));
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);

    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public BoardPos position() {
        return boardPos;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

}
