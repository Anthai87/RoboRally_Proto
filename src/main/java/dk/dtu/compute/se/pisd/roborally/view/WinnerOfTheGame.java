package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ...
 *
 * @author S163053 & S141479
 *
 */


public class WinnerOfTheGame {
        private static boolean svar;

        public static Boolean display(String title, String text, AppController appController) {
            Stage window = new Stage();

            // blocks input events or user interaction with other windows untill this one is taking care of.
            window.initModality(Modality.APPLICATION_MODAL);

            // window setup
            window.setTitle(title);
            window.setMinWidth(400);
            window.setMinHeight(150);
            window.centerOnScreen();


            Text text1 = new Text(text);
            text1.setText(text);
            text1.setFont(Font.font(null, FontWeight.SEMI_BOLD, 16));
            text1.setFill(Color.rgb(0, 0, 0));


            VBox teksten = new VBox();
            teksten.getChildren().add(text1);

            // creating two buttons (New Game, Exit)
            Button yes = new Button(" New Game ");
            yes.setStyle("-fx-font-size: 11pt");

            Button no = new Button(" Exit ");
            no.setStyle("-fx-font-size: 11pt");

            // lambda expression
            yes.setOnAction(event -> {
                window.close();
                appController.newGame();
            });

            no.setOnAction(event -> {
                window.close();

            });

            HBox buttons = new HBox(15);
            buttons.getChildren().addAll(yes, no);

            // Hele Layout
            GridPane heleLayout = new GridPane();
            heleLayout.setHgap(10);
            heleLayout.setVgap(10);
            heleLayout.add(teksten, 2, 1);
            heleLayout.add(buttons, 2, 2);

            Scene scene = new Scene(heleLayout);
            window.setScene(scene);
            window.showAndWait();

            return svar;
        }
    }
