package sample;

public class Navigation {
    private final OrbsManager orbsManager;

    public Navigation(final OrbsManager orbsManager) {
        this.orbsManager = orbsManager;
    }

    public void scaleOrbs(double scala) {
        Main.scala *= scala;

        moveOrbsVertically((1 - scala) * Main.HEIGHT / 2d);
        moveOrbsHorizontally((1 - scala) * Main.WIDTH / 2d);

        for(Orb orb : orbsManager.getAllOrbs()) {
            orb.setCenterX(orb.getCenterX() *  scala);
            orb.setCenterY(orb.getCenterY() * scala);
            orb.setRadius(orb.getRadius() * scala);
        }
    }

    public void moveOrbsHorizontally(double translate) {
        for(Orb orb : orbsManager.getAllOrbs()) {
            orb.setCenterX(orb.getCenterX() + translate);
        }
    }

    public void moveOrbsVertically(double translate) {
        for(Orb orb : orbsManager.getAllOrbs()) {
            orb.setCenterY(orb.getCenterY() + translate);
        }
    }
}
