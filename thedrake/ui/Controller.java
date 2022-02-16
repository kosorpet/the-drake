package thedrake.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import thedrake.PlayingSide;

import static thedrake.ui.TheDrakeApp.createInitialGameState;

public class Controller implements Initializable {

    private Stage stage;

    public void showEndScreen(PlayingSide side){
        EndMessage endMessage = new EndMessage(side, this);
        endMessage.setId("end");
        Scene end = new Scene(endMessage);
        end.getStylesheets().addAll(this.getClass().getResource("endScreen.css").toExternalForm());
        stage.setScene(end);
        stage.show();
    }

    @FXML
    public Button closeButton;

    @FXML
    public Button twoPlayerButton;

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handletwoPlayerButtonAction(ActionEvent event) {
        GameView gameView = new GameView(createInitialGameState(), this);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(gameView));
        stage.setTitle("The Drake (kosorpet)");
        stage.show();
    }

    @FXML
    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("drake.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
