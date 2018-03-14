package sample;

import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


public class OrbsCreator {
    private final Scene gameScene;
    private final OrbsManager orbsManager;
    private final Group sceneNodes;

    private OrbsPathCreator orbsPathCreator;
    private List<Orb> orbsOnPath;
    private final Orb tempOrb;

    private VBox editBox;

    private CreatorStatus status = CreatorStatus.COORDINATES;
    private boolean isColorPicker = false, isMassPicker = false, isPausing = false;

    private Label hint;

    public OrbsCreator(Scene gameScene, Group sceneNodes, OrbsManager orbsManager) {
        this.gameScene = gameScene;
        this.orbsManager = orbsManager;
        this.sceneNodes = sceneNodes;
        this.tempOrb = new Orb(0, 0, 0,
                0, 0, 0, Color.web("#fff"));
        initPathStuff();
        initEditBox();
        setHint();
        sceneNodes.getChildren().addAll(tempOrb, hint);
        setMouseClickEvent();
        setMouseMoveEvent();
    }

    private void initPathStuff() {
        orbsOnPath = new LinkedList<>();
        orbsPathCreator = new OrbsPathCreator(sceneNodes, orbsManager, orbsOnPath, tempOrb);
    }

    private void initEditBox() {
        editBox = new VBox(10);
        editBox.setTranslateX(50);
        editBox.setTranslateY(30);
        editBox.setBorder(Border.EMPTY);
        editBox.setAlignment(Pos.CENTER);
        editBox.setEffect(new InnerShadow(30, Color.web("#fff")));
    }

    private void setHint() {
        this.hint = new Label();
        this.hint.setPrefWidth(Main.WIDTH);
        this.hint.setAlignment(Pos.CENTER);
        this.hint.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
        this.hint.setTextFill(Color.web("#aaa"));
        this.hint.setEffect(new InnerShadow(2, Color.web("fff")));
    }

    private void setMouseClickEvent() {
        gameScene.setOnMouseClicked(event -> {
            if(status == CreatorStatus.EDITING)
                return;

            Point2D clickedPoint = new Point2D(event.getX(), event.getY());

            if(orbsManager.getAllOrbs().size() == 0){
                createOrb(clickedPoint, event.getClickCount());
                return;
            }

            boolean isDeleting = false;
            for(Orb orb : orbsManager.getAllOrbs()) {
                if(orb.contains(clickedPoint) && status == CreatorStatus.COORDINATES) {
                    editOrb(orb);
                    isDeleting = true;
                    break;
                }
            }

            if(!isDeleting) {
                createOrb(clickedPoint, event.getClickCount());
            }
        });
    }

    private void editOrb(Orb orb) {
        isPausing = (MainLoop.getMainLoop().getStatus() == Timeline.Status.RUNNING);
        MainLoop.getMainLoop().stop();
        status = CreatorStatus.EDITING;
        orb.setEffect(new DropShadow(orb.getRadius(), Color.web("#f00")));
        hint.setText("Deleting orb");
        setEditBox(orb);
    }

    private void setEditBox(Orb orb) {
        Button removeOrb = new Button("Remove orb");
        Button stopOrb = new Button("Stop orb");
        Button cancel = new Button("Cancel");
        removeOrb.setOnAction(event -> {
            sceneNodes.getChildren().remove(orb);
            orbsManager.addOrbsToRemove(orb);
            terminateEditingProcess(removeOrb, stopOrb, cancel);
        });

        stopOrb.setOnAction(event -> {
            orb.setEffect(new InnerShadow(orb.getRadius(), Color.web("#222")));
            orb.setVelX(0);
            orb.setVelY(0);
            terminateEditingProcess(removeOrb, stopOrb, cancel);
        });

        cancel.setOnAction(event -> {
            orb.setEffect(new InnerShadow(orb.getRadius(), Color.web("#222")));
            terminateEditingProcess(removeOrb, stopOrb, cancel);
        });
        editBox.getChildren().addAll(removeOrb, stopOrb, cancel);
        sceneNodes.getChildren().add(editBox);
    }

    private void terminateEditingProcess(Button... buttons) {
        editBox.getChildren().removeAll(buttons);
        sceneNodes.getChildren().removeAll(editBox);
        status = CreatorStatus.COORDINATES;
        hint.setText(null);
        if(isPausing)
            MainLoop.getMainLoop().play();
        isPausing = false;
    }

    private void createOrb(Point2D clickedPoint, int clickCount) {
        double clickedX = clickedPoint.getX();
        double clickedY = clickedPoint.getY();

        if(status == CreatorStatus.COORDINATES && clickCount == 2) {
            isPausing = (MainLoop.getMainLoop().getStatus() == Timeline.Status.RUNNING);
            MainLoop.getMainLoop().stop();
            setCoordinates(clickedX, clickedY);
        }
        else if(status == CreatorStatus.SET_PROPERTIES) {
            setProperties(clickedX, clickedY);
        }
        else if(status == CreatorStatus.VELOCITIES) {
            setVelocitites(clickedX, clickedY);
        }
    }

    private void setCoordinates(double clickedX, double clickedY) {
        hint.setText("Set radius");
        tempOrb.setCenterX(clickedX);
        tempOrb.setCenterY(clickedY);
        tempOrb.setRadius(2);
        tempOrb.setFill(Color.web("#fff", 0.5));
        status = CreatorStatus.SET_PROPERTIES;
    }

    private void setProperties(double clickedX, double clickedY) {
        setRadius(clickedX, clickedY);
    }

    private void setRadius(double clickedX, double clickedY) {
        if(isMassPicker || isColorPicker) return;
        tempOrb.setRadius(calculateRadius(clickedX, clickedY));
        hint.setText("Set mass");
        setMass(clickedX, clickedY);
    }

    private double calculateRadius(double x, double y) {
        return Math.sqrt((x - tempOrb.getCenterX())*(x - tempOrb.getCenterX()) +
                (y - tempOrb.getCenterY()) * (y - tempOrb.getCenterY()));
    }

    private void setMass(double clickedX, double clickedY) {
        if(isMassPicker) return;
        isMassPicker = true;
        setMassBox(clickedX, clickedY);
    }

    private void setMassBox(double clickedX, double clickedY) {
        VBox massBox = new VBox();
        massBox.setTranslateX(clickedX);
        massBox.setTranslateY(clickedY);
        TextField massField = new TextField();
        massBox.getChildren().add(massField);
        sceneNodes.getChildren().add(massBox);
        massField.setOnAction(event -> {
            String massDoubleValue = massField
                    .getCharacters()
                    .toString()
                    .replaceAll("/D||^\\.", "");
            if(massDoubleValue != "" && Double.valueOf(massDoubleValue) != 0) {
                tempOrb.setMass(Double.valueOf(massDoubleValue));
                massBox.getChildren().remove(massField);
                sceneNodes.getChildren().removeAll(massBox);
                isMassPicker = false;
                hint.setText("Set color");
                setColor(clickedX, clickedY);
            }
        });
    }

    private void setColor(double clickedX, double clickedY) {
        if(isColorPicker) return;
        isColorPicker = true;
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setTranslateX(clickedX);
        colorPicker.setTranslateY(clickedY);
        sceneNodes.getChildren().add(colorPicker);
        colorPicker.setOnAction(event -> {
            tempOrb.setFill(colorPicker.getValue());
            sceneNodes.getChildren().removeAll(colorPicker);
            isColorPicker = false;
            hint.setText("Set velocity");
            status = CreatorStatus.VELOCITIES;
        });
    }

    private void setVelocitites(double clickedX, double clickedY) {
        hint.setText(null);
        tempOrb.setVelX(calculateVelX(clickedX));
        tempOrb.setVelY(calculateVelY(clickedY));

        Orb newOrb = new Orb(tempOrb);

        sceneNodes.getChildren().add(newOrb);
        orbsManager.addOrbs(newOrb);
        tempOrb.setRadius(0);
        status = CreatorStatus.COORDINATES;
        if(isPausing)
            MainLoop.getMainLoop().play();
        isPausing = false;
        sceneNodes.getChildren().removeAll(orbsOnPath);
    }

    private double calculateVelX(double x) {
        return (tempOrb.getCenterX() - x) / (200 * Main.scala);
    }

    public double calculateVelY(double y) {
        return (tempOrb.getCenterY() - y) / (200 * Main.scala);
    }

    private void setMouseMoveEvent() {
        gameScene.setOnMouseMoved(event -> {
            double cursorX = event.getX();
            double cursorY = event.getY();
            if(status == CreatorStatus.SET_PROPERTIES) {
                if(!isMassPicker && !isColorPicker) {
                    tempOrb.setRadius(calculateRadius(cursorX, cursorY));
                }
            }
            if(status == CreatorStatus.VELOCITIES) {
                orbsPathCreator.setNewPath(calculateVelX(cursorX), calculateVelY(cursorY));
            }
            if(status == CreatorStatus.COORDINATES) {
                for(Orb orb : orbsManager.getAllOrbs()) {
                    if(orb.contains(cursorX, cursorY)) {
                        hint.setText("Mass: " + String.valueOf(new BigDecimal(orb.getMass())));
                        break;
                    }else {
                        hint.setText(null);
                    }
                }
            }
        });
    }


}
