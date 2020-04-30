package dk.dtu.compute.se.pisd.roborally.view;

/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

import com.sun.istack.internal.NotNull;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
// ViewObserver er for updateView metoden, bestemmer hvordan felterne skal se ud.
// StackPane layout; Det der kommer først på ligger nederst i layoutet.
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 55;
    final public static int SPACE_WIDTH = 55;

    public final Space space;

    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

      if ((space.x + space.y) % 2 == 0)
            this.setStyle("-fx-background-color: dimgrey");
      else
            this.setStyle("-fx-background-color: grey");

        //updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon figure = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                figure.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                figure.setFill(Color.MEDIUMPURPLE);
            }
            figure.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(figure);
        } else {
            this.setStyle(null);
        }
        // update the style of the space according to the changes;
        //updatePlayer();
    }


    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            for (FieldAction action : space.getActions()) {

                if (action instanceof ConveyorBelt) {
                    ConveyorBelt belt = (ConveyorBelt) action;
                    Image arrow = new Image("images/conveyorBelt.jpg");
                    ImageView conveyBelt = new ImageView(arrow);
                    conveyBelt.setImage(arrow);
                    conveyBelt.setFitHeight(SPACE_HEIGHT);
                    conveyBelt.setFitWidth(SPACE_WIDTH);
                    conveyBelt.setRotate((90 * belt.getHeading().ordinal()) % 360);
                    this.getChildren().add(conveyBelt);
                }
                else if (action instanceof Gear) {
                    Image image = new Image("images/Gear.jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setImage(image);
                    imageView.setFitHeight(SPACE_HEIGHT);
                    imageView.setFitWidth(SPACE_WIDTH);
                    this.getChildren().add(imageView);
                }
                else if (action instanceof Checkpoint) {
                    if(((Checkpoint) action).getCheckpoint()==1) {
                        Image checkPoint = new Image("images/checkPoint.jpg");
                        ImageView imageView1 = new ImageView(checkPoint);
                        imageView1.setImage(checkPoint);
                        imageView1.setFitHeight(SPACE_HEIGHT);
                        imageView1.setFitWidth(SPACE_WIDTH);
                        this.getChildren().add(imageView1);
                    }
                    else if(((Checkpoint) action).getCheckpoint()==2) {
                        Image checkPoint2 = new Image("images/checkPoint2.jpg");
                        ImageView imageView2 = new ImageView(checkPoint2);
                        imageView2.setImage(checkPoint2);
                        imageView2.setFitHeight(SPACE_HEIGHT);
                        imageView2.setFitWidth(SPACE_WIDTH);
                        this.getChildren().add(imageView2);

                    }
                     else {
                        Image checkPoint3 = new Image("images/checkPoint3.jpg");
                        ImageView imageView2 = new ImageView(checkPoint3);
                        imageView2.setImage(checkPoint3);
                        imageView2.setFitHeight(SPACE_HEIGHT);
                        imageView2.setFitWidth(SPACE_WIDTH);
                        this.getChildren().add(imageView2);

                    }

                } else if (action instanceof Pits){
                    Image Pits = new Image("images/Pits.jpg");
                    ImageView imageView2 = new ImageView(Pits);
                    imageView2.setImage(Pits);
                    imageView2.setFitHeight(SPACE_HEIGHT);
                    imageView2.setFitWidth(SPACE_WIDTH);
                    this.getChildren().add(imageView2);
                }
            }
            Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);
            gc.setLineCap(StrokeLineCap.ROUND);
            for (Heading wall : space.getWalls()) {
                switch (wall) {
                    case SOUTH:
                        gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                        break;
                    case WEST:
                        gc.strokeLine(2, 2, 2, SPACE_HEIGHT - 2);
                        break;
                    case NORTH:
                        gc.strokeLine(2, 2, SPACE_WIDTH - 2, 2);
                        break;
                    case EAST:
                        gc.strokeLine(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                        break;
                }
            }
            this.getChildren().add(canvas);

            Player player = space.getPlayer();
            if (player != null) {
                Polygon arrow = new Polygon(0.0, 0.0, 10.0, 20.0, 20.0, 0.0);
                try {
                    // farverne kommer fra klassen Player.
                    arrow.setFill(Color.valueOf(player.getColor()));
                } catch (Exception e) {
                    arrow.setFill(Color.MEDIUMPURPLE);
                }
                arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
                this.getChildren().add(arrow);
            }
        }
    }
}

