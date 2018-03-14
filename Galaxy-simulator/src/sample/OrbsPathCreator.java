package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class OrbsPathCreator {

    private List<Orb> orbsOnPath;
    private List<Orb> allOrbsClone;
    private Group sceneNodes;
    private OrbsManager orbsManager;
    private Orb mainOrb, pathOrb;

    public OrbsPathCreator(Group sceneNodes,OrbsManager orbsManager, List<Orb> orbsOnPath, Orb mainOrb) {
        this.orbsOnPath = orbsOnPath;
        this.allOrbsClone = new LinkedList<>();
        this.sceneNodes = sceneNodes;
        this.orbsManager = orbsManager;
        this.mainOrb = mainOrb;
    }

    public void setNewPath(double velX, double velY) {
        clearPreviewPath();
        setMainOrbClone(velX, velY);
        cloneAllOrbs();
        createPath();
        sceneNodes.getChildren().addAll(orbsOnPath);
    }

    private void clearPreviewPath() {
        sceneNodes.getChildren().removeAll(orbsOnPath);
        orbsOnPath.clear();
        allOrbsClone.clear();
    }

    private void setMainOrbClone(double velX, double velY) {
        pathOrb = new Orb(mainOrb);
        pathOrb.setVelX(velX);
        pathOrb.setVelY(velY);
        pathOrb.setRadius(2);
    }

    private void cloneAllOrbs() {
        for(Orb orb : orbsManager.getAllOrbs()) {
            if(orb != mainOrb)
                allOrbsClone.add(new Orb(orb));
        }
    }

    private void createPath() {
        for (int i = 0; i < 50; i++) {
            createPathElement();
            addPathElement(i);
        }
    }

    private void createPathElement() {
        for(int j = 0; j < 10; j++) {
            updateAllClonedOrbs();
        }
    }

    private void updateAllClonedOrbs() {
        for(Orb orb : allOrbsClone) {
            pathOrb.updateGravity(orb);
            orb.update();
        }
        pathOrb.update();
    }

    private void addPathElement(int elementNumber) {
        Orb newOrb = new Orb(pathOrb);
        newOrb.setFill(Color.web("#fff", 1 - elementNumber / 50d));
        orbsOnPath.add(newOrb);
    }
}
