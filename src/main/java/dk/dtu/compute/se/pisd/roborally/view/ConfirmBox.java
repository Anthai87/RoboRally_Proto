package dk.dtu.compute.se.pisd.roborally.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    public static void display(String title, String text) {
        Stage window = new Stage();

        // basic window setup
        // blocks input events or user interaction with other windows untill this one is taking care of.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);


        window.setMinWidth(350);
        window.setMinHeight(150);

        Text label = new Text(text);
        label.setText(text);
        label.setFont(Font.font(null, FontWeight.SEMI_BOLD, 20));
        label.setFill(Color.rgb(50, 45, 38));


        GridPane teksten = new GridPane();
        teksten.getChildren().add(label);


        // Buttons, yes and no

        Button yes = new Button(" Yes ");
        yes.setStyle("-fx-font-size: 11pt");
        yes.setOnAction(event ->
                window.close());

        Button no = new Button(" No ");
        no.setStyle("-fx-font-size: 11pt");

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(yes, no);

        GridPane heleLayout = new GridPane();
        heleLayout.setHgap(10);
        heleLayout.setVgap(10);
        heleLayout.add(teksten, 2, 1);
        heleLayout.add(buttons, 2, 2);

        Scene scene = new Scene(heleLayout);
        window.setScene(scene);
        window.showAndWait();
    }
}
