package thedrake.ui;

import javafx.scene.PointLight;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import thedrake.PlayingSide;

public class TurnIndicator extends VBox {

    private PlayingSide sideOnTurn;

    private final Text sideText;

    TurnIndicator(){
        this.setTranslateY(150);
        this.setMaxHeight(100);
        this.sideOnTurn = PlayingSide.BLUE;
        Text text = new Text("Side on turn:");
        text.setStyle("-fx-font: 30 Arial");
        text.setTranslateX(5);
        this.getChildren().add(text);
        this.sideText = new Text(sideOnTurn.toString());
        this.sideText.setFill(Color.DEEPSKYBLUE);
        this.sideText.setStyle("-fx-font: 30 Arial");
        this.getChildren().add(sideText);
        this.sideText.setTranslateX(5);
    }

    public void flipPlayer(){
        if(sideOnTurn == PlayingSide.BLUE){
            sideOnTurn = PlayingSide.ORANGE;
            sideText.setFill(Color.DARKORANGE);
        }
        else{
            sideOnTurn = PlayingSide.BLUE;
            sideText.setFill(Color.DEEPSKYBLUE);
        }
        sideText.setText(sideOnTurn.toString());
        getChildren().set(1, sideText);
    }
}
