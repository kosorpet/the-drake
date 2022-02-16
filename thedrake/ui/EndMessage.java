package thedrake.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import thedrake.PlayingSide;
import thedrake.ui.Controller;


import java.io.IOException;

public class EndMessage extends Pane {

    private final Controller controller;

    EventHandler<ActionEvent> menuPress = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            try {
                controller.switchToMenu(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };

    EventHandler<ActionEvent> newGamePress = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e)
        {
            controller.handletwoPlayerButtonAction(e);
        }
    };

    public EndMessage(PlayingSide winner, Controller controller){

        this.controller = controller;
        setMinHeight(600);
        setMinWidth(600);
        Text text;
        if(winner == PlayingSide.BLUE){
            text = new Text("Orange loses!");
        }
        else {
            text = new Text("Blue loses!");
        }
        text.setFont(Font.font("Source Sans Pro Black", 50));
        text.setStyle("-fx-font-weight: bold");

        text.setWrappingWidth(400);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setX(600/2 - text.getWrappingWidth()/2);
        text.setY(100);

        this.getChildren().add(text);

        Button menu = new Button();
        menu.setText("Hlavní Menu");
        menu.setMinWidth(140);
        menu.setMinHeight(30);
        menu.setTranslateX(600/2 - 70);
        menu.setTranslateY(400);
        menu.setOnAction(menuPress);
        menu.setBlendMode(BlendMode.SRC_OVER);
        menu.setFont(Font.font("Source Sans Pro Black", 16));
        this.getChildren().add(menu);

        Button newGame = new Button();
        newGame.setText("Nová Hra");
        newGame.setMinWidth(140);
        newGame.setMinHeight(30);
        newGame.setTranslateX(600/2 - 70);
        newGame.setTranslateY(400 + 30 + 10);
        newGame.setOnAction(newGamePress);
        newGame.setBlendMode(BlendMode.SRC_OVER);
        newGame.setFont(Font.font("Source Sans Pro Black", 16));
        this.getChildren().add(newGame);

    }

}
