package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class AppController {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(1,2,3);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magneta");
    private RoboRally roboRally;
    private GameController gameController;


    public AppController(RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null){
               // if (!stopGame){
                    return;
                };
            }
            Board board = new Board(8,8);
            gameController = new GameController(board);

            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board,"red", "Anthony",(i+1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i,i));

            }
            board.setCurrentPlayer(board.getPlayer(0));
            roboRally.createBoardView(gameController);
            gameController.initializeProgrammingPhase();
        }

    //
    public void saveGame(){
        //TODO need to be implemented
    }

    public void exit(String title, String text) {
        // TODO needs to be implemented
        Stage window = new Stage();
        // basic window setup
        // blocks input events or user interaction with other windows untill this one is taking care of.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);


        window.setMinWidth(350);
        window.setMinHeight(150);

        Text label = new Text(text);
        label.setText(text);
        label.setFont(Font.font(null, FontWeight.SEMI_BOLD,20));
        label.setFill(Color.rgb(45,35,32));


        GridPane teksten = new GridPane();
        teksten.getChildren().add(label);



        Button yes = new Button(" Yes ");
        yes.setStyle("-fx-font-size: 11pt");
        yes.setOnAction(event -> window.close());

        Button no = new Button(" No ");
        no.setStyle("-fx-font-size: 11pt");
        no.setOnAction(event -> window.close());

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(yes, no);

        GridPane heleLayout = new GridPane();
        heleLayout.setHgap(10);
        heleLayout.setVgap(10);
        heleLayout.add(teksten,2,1);
        heleLayout.add(buttons,2,2);

        Scene scene = new Scene(heleLayout);
        window.setScene(scene);
        window.showAndWait();


    }
}
