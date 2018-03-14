package sample;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Camera {

    private final Stage primaryStage;
    private final Navigation navigation;
    private final Timeline mainLoop;

    private final double step = 10;
    private final double zoomScale = 0.1;

    public Camera(Stage primaryStage, Navigation navigation) {
        this.primaryStage = primaryStage;
        this.navigation = navigation;
        this.mainLoop = MainLoop.getMainLoop();
        setMouseInput();
    }

    private void setMouseInput() {
        primaryStage.getScene().setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) {
                navigation.moveOrbsVertically(step);
            }
            if (keyEvent.getCode() == KeyCode.DOWN) {
                navigation.moveOrbsVertically(-step);
            }
            if (keyEvent.getCode() == KeyCode.LEFT) {
                navigation.moveOrbsHorizontally(step);
            }
            if (keyEvent.getCode() == KeyCode.RIGHT) {
                navigation.moveOrbsHorizontally(-step);
            }
            if (keyEvent.getCode() == KeyCode.T) {
                navigation.scaleOrbs(1 + zoomScale);
            }
            if (keyEvent.getCode() == KeyCode.W) {
                navigation.scaleOrbs(1 - zoomScale);
            }
            if (keyEvent.getCode() == KeyCode.P) {
                if (mainLoop.getStatus() == Animation.Status.RUNNING) {
                    mainLoop.stop();
                } else {
                    mainLoop.play();
                }
            }
            if (keyEvent.getCode() == KeyCode.F) {
                primaryStage.setFullScreen(true);
            }
            if  (keyEvent.getCode() == KeyCode.C) {
                if(Main.isCollisionOn) {
                    Main.isCollisionOn = false;
                } else {
                    Main.isCollisionOn = true;
                }
            }
        });
    }
}
