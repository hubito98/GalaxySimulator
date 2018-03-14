package sample;

import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Orb extends Circle{

    protected double velX, velY;
    protected double mass;

    public Orb(double centerX, double centerY, double radius,
               double velX, double velY, double mass, Color color) {
        this.setCenterX(centerX);
        this.setCenterY(centerY);
        this.setRadius(radius);
        this.setFill(color);
        this.velX = velX;
        this.velY = velY;
        this.mass = mass;
        this.setEffect(new InnerShadow(radius / 2, Color.web("#222")));
    }

    public Orb(Orb orb) {
        this.setCenterX(orb.getCenterX());
        this.setCenterY(orb.getCenterY());
        this.setRadius(orb.getRadius());
        this.setFill(orb.getFill());
        this.velX = orb.getVelX();
        this.velY = orb.getVelY();
        this.mass = orb.getMass();
        this.setEffect(new InnerShadow(orb.getRadius(), Color.web("#222")));
    }

    public void updateGravity(Orb other){
        if(getCenterX() != other.getCenterX() && getCenterY() != other.getCenterY()) {
            double deltaX = other.getCenterX() - getCenterX();
            double deltaY = other.getCenterY() - getCenterY();

            double force = (Main.G * other.getMass() * getMass() * 1000) /
                    (deltaX * deltaX + deltaY * deltaY) * Main.scala * Main.scala;

            double forceX = deltaX / Math.sqrt(deltaX * deltaX + deltaY * deltaY) * force;
            double forceY = deltaY / Math.sqrt(deltaX * deltaX + deltaY * deltaY) * force;

            velX += forceX / getMass() / Main.FPS * 60;
            velY += forceY / getMass() / Main.FPS * 60;

            other.addVelX(-(forceX / other.getMass()) / Main.FPS * 60);
            other.addVelY(-(forceY / other.getMass()) / Main.FPS * 60);
        }
    }

    public void update() {
        setCenterX(getCenterX() + velX * Main.scala);
        setCenterY(getCenterY() + velY * Main.scala);
    }

    public boolean checkCollision(Orb other){
        if(this == other) return false;
        double deltaX = this.getCenterX() - other.getCenterX();
        double deltaY = this.getCenterY() - other.getCenterY();

        return (Math.sqrt(deltaX*deltaX + deltaY*deltaY) < this.getRadius()+other.getRadius());
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void addVelX(double velX) {
        this.velX += velX;
    }

    public void addVelY(double velY) {
        this.velY += velY;
    }


}
