package sample;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    MainLoop mainLoop;
    public static final double G = 6.67300E-11;
    public static final int FPS = 60;
    public static double scala = 1;
    public static boolean isCollisionOn = true;
    public static final double WIDTH = Screen.getPrimary().getVisualBounds().getWidth(),
            HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainLoop = new MainLoop(FPS, "Galaxy Simulator");
        mainLoop.initialize(primaryStage);
        mainLoop.startMainLoop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
