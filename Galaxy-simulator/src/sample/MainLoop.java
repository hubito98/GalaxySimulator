package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainLoop {
    private static Timeline mainLoop;

    private Scene gameScene;
    private Group sceneNodes;

    private final int FPS;
    private final String windowTitle;

    private final OrbsManager orbsManager = new OrbsManager();
    private final Navigation navigation;
    private Camera camera;
    private OrbsCreator orbsCreator;

    public MainLoop(final int FPS, final String windowTitle) {
        this.FPS = FPS;
        this.windowTitle = windowTitle;
        this.navigation = new Navigation(orbsManager);
        createAndSetGameLoop();
    }

    private void createAndSetGameLoop() {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(new Duration(1000/FPS), event -> {
            updateOrbsGravity();
            updateOrbs();
            checkCollision();
            cleanupOrbs();
        });

        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        setMainLoop(timeline);
    }

    public void initialize(final Stage primaryStage) {
        primaryStage.setTitle(windowTitle);
        setSceneNodes(new Group());
        setLegend();
        setGameScene(new Scene(getSceneNodes(), Main.WIDTH, Main.HEIGHT, Color.web("000")));
        primaryStage.setScene(getGameScene());
        primaryStage.setFullScreen(true);
        primaryStage.show();

        orbsCreator = new OrbsCreator(getGameScene(), getSceneNodes(), getOrbsManager());
        camera = new Camera(primaryStage, navigation);

        addOrbs(
                new Orb( Main.WIDTH / 2, Main.HEIGHT / 2, 30,
                        0, 0, 2_000_000_000, Color.web("#ff0")),
                new Orb(Main.WIDTH / 2 - 90, Main.HEIGHT / 2, 5,
                        0, 1.1, 20, Color.web("#4aa"))
        );
    }

    private void addOrbs(Orb... orbs) {
        sceneNodes.getChildren().addAll(orbs);
        orbsManager.addOrbs(orbs);
    }

    public void startMainLoop() {
        MainLoop.mainLoop.play();
    }

    private void updateOrbsGravity() {
        for(int i = 0; i < orbsManager.getAllOrbs().size() - 1; i++) {
            for(int j = i+1; j < orbsManager.getAllOrbs().size(); j++) {
                orbsManager.getAllOrbs().get(i).updateGravity(orbsManager.getAllOrbs().get(j));
            }
        }
    }

    private void updateOrbs() {
        for(Orb orb : orbsManager.getAllOrbs()) {
            orb.update();
        }
    }

    private void checkCollision() {
        if(Main.isCollisionOn) {
            for (Orb orbA : orbsManager.getAllOrbs()) {
                for (Orb orbB : orbsManager.getAllOrbs()) {
                    if (handleCollision(orbA, orbB)) {
                        if (orbA.getMass() >= orbB.getMass() * 1000) {
                            orbsManager.addOrbsToRemove(orbB);
                            sceneNodes.getChildren().removeAll(orbB);
                        } else if (orbB.getMass() >= orbA.getMass() * 1000) {
                            orbsManager.addOrbsToRemove(orbA);
                            sceneNodes.getChildren().removeAll(orbA);
                        } else {
                            orbsManager.addOrbsToRemove(orbA, orbB);
                            sceneNodes.getChildren().removeAll(orbA, orbB);
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean handleCollision(Orb orbA, Orb orbB) {
        return orbA.checkCollision(orbB);
    }

    private void cleanupOrbs() {
        orbsManager.cleanupOrbs();
    }

    public Scene getGameScene() {
        return gameScene;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setLegend() {
        Label legend = new Label("Arrows - moving camera\n" +
                "w/t - zoom\n" + "p - pause\n" + "double click to create orb\n" +
                "click on orb to edit\n" + "f - fullscreen\n" + "c - on/off collisions");
        legend.setPrefWidth(Main.WIDTH);
        legend.setAlignment(Pos.TOP_RIGHT);
        legend.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        legend.setTextFill(Color.web("#aaa"));
        legend.setEffect(new InnerShadow(2, Color.web("fff")));
        sceneNodes.getChildren().add(legend);
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    public void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    public static Timeline getMainLoop() {
        return mainLoop;
    }

    public static void setMainLoop(Timeline mainLoop) {
        MainLoop.mainLoop = mainLoop;
    }

    public int getFPS() {
        return FPS;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public OrbsManager getOrbsManager() {
        return orbsManager;
    }
}
