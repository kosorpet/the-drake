package thedrake.ui;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import thedrake.PlayingSide;
import thedrake.TroopTile;

public class CapturedList extends VBox {

    public CapturedList( ){

        this.setTranslateY(16);
        this.setTranslateX(-5);
        this.setMaxHeight(415);
        this.setMinWidth(120);
        Text title = new Text("Captured units:");
        title.setTranslateX(3);
        title.setFont(Font.font("Arial", 17));
        title.setStyle("-fx-font-weight: bold");
        this.getChildren().add(title);
    }


    void addUnit(TroopTile captured){
        Text unitText = new Text(captured.troop().name());
        unitText.setTranslateX(3);
        unitText.setFont(Font.font("Arial", 17));
        if(captured.side() == PlayingSide.BLUE) {
            unitText.setFill(Color.DEEPSKYBLUE);
        }
        else{
            unitText.setFill(Color.DARKORANGE);
        }
        getChildren().add(unitText);
    }
}
