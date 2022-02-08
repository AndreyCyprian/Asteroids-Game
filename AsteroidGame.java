AsteroidGame.java

import blobmx.BlobGUI;
import blobmx.SandBox;
import blobmx.SandBoxMode;
import java.util.Random;

public class AsteroidGame implements BlobGUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AsteroidGame game = new AsteroidGame();
    }

    private final SandBox sb;
    private final Random random = new Random();
  
    public AsteroidGame(){
        sb = new SandBox();
        sb.setSandBoxMode(SandBoxMode.FLOW);
        sb.setFrameRate(66);
        sb.init(this);
    }

    @Override
    public void generate() {
        Rocket rocket = new Rocket(0,0, sb);
        sb.addBlob(rocket);
      
        for (int i = 0; i < 10; i++) {
            int ranDelx = 0;
            while (ranDelx == 0){
                ranDelx = -3 + random.nextInt(7);
            }
            int ranDely = 0;
            while (ranDely == 0){
                ranDely = -3 + random.nextInt(7);
            }
          
            double rot = .1;
            int b = random.nextInt(2);
            if (b == 0){
                rot = -rot;
            }
          
            Asteroid asteroid = new Asteroid(ranDelx, ranDely, rot);
            sb.addBlob(asteroid);
        }
      
    }

  
}


//Asteroid.java
//
//import blobmx.BlobUtils;
//import blobmx.PolyBlob;
//import java.awt.Point;
//import java.util.Random;
//
//public class Asteroid extends PolyBlob {
//    private static final Random random = new Random();
//
//    public Asteroid(int idx, int jdx, double rot) {
//        super(-100, -100, rot);
//        setDelta(idx, jdx);
//      
//        int numSides = 5 + random.nextInt(5);
//      
//        int[] vertex = new int[numSides];
//        for (int i = 0; i < numSides; i++){
//            vertex[i] = 5 + random.nextInt(11);
//        }
//      
//        double region = (2 * Math.PI)/numSides;
//        double[] angle = new double[numSides];
//        for (int i = 0; i < numSides; i++){
//            angle[i] = (i * region) + (Math.random() * region);
//        }
//      
//        int[] x = new int [numSides];
//        int[] y = new int [numSides];
//        for (int i = 0; i < numSides; i++) {
//            Point p = BlobUtils.rotatePoint(vertex[i], angle[i]);
//            x[i] = p.x;
//            y[i] = p.y;
//        }
//      
//        setPolygon(x,y);
//    }
//  
//}


Missle.java

import blobmx.Blob;
import blobmx.BlobProximity;

public class Missle extends Blob implements BlobProximity {

    private final int speed = 5;
    private final int size = 5;

    public Missle(int x, int y, double theta) {
        super(0,0);
        this.setSize(size);
        this.setLoc(x,y);     
      
        int dx = (int) Math.round( speed * Math.cos(theta));
        int dy = (int) Math.round( speed * Math.sin(theta));
      
        this.setDelta(dx, dy);
    }
}


Rocket.java


import blobmx.BlobAction;
import blobmx.BlobProximity;
import blobmx.BlobUtils;
import blobmx.PolyBlob;
import blobmx.SandBox;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Rocket extends PolyBlob implements BlobAction, BlobProximity {

    private final int[] polygonx = new int[]{10, -10, -5, -10};
    private final int[] polygony = new int[]{0, -7, 0, 7};
  
    private final int[] referenceX;
    private final int[] referenceY;
  
    private double angle = 0.0;
    private final double delta = 0.15;
    private final double speed = 5.0;
  
    private final double twoTimesPi = 2 * Math.PI;
  
    private final SandBox sandBox;
  
    public Rocket(int x, int y, SandBox sb) {
      
        super(300, 300, 0);
      
        referenceX = polygonx;
        referenceY = polygony;
        sandBox = sb;
      
        this.setPolygon(polygonx, polygony);
    }

    @Override
    public void keyAction(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
      
        switch (keyCode){
            case KeyEvent.VK_UP:
                MoveForward();
                break;
            case KeyEvent.VK_LEFT:
                RotateLeft();
                break;
            case KeyEvent.VK_RIGHT:
                RotateRight();
                break;
            case KeyEvent.VK_SPACE:
                Launch(sandBox);
                BlobUtils.playSound();
                break;
        }
    }
  
    private void RotateRight(){
        this.angle += delta;
      
        if (angle > twoTimesPi){
            angle -= twoTimesPi;
        }
      
        turn();
    }

    private void RotateLeft(){
        this.angle -= delta;

        if (angle < twoTimesPi){
            angle += twoTimesPi;
        }

        turn();
    }
  
    private void turn(){      
        for (int i = 0; i < referenceX.length; i++) {
            Point p = BlobUtils.rotatePoint(referenceX[i], referenceY[i], angle);

            polygonx[i] = p.x;
            polygony[i] = p.y;
        }
    }
  
    public void MoveForward() {
        Point p = this.getLoc();
      
        int xloc = p.x;
        int yloc = p.y;
      
        xloc = xloc + (int) Math.round(speed * Math.cos(angle));
        yloc = yloc + (int) Math.round(speed * Math.sin(angle));
      
        this.setLoc(xloc, yloc);
      
    }
  
    public void Launch(SandBox sb) {
        int boundingRadius = this.getSize()/2;
     
        Point currentLocation = this.getLoc();
      
        Point launchPoint = BlobUtils.rotatePoint(boundingRadius + 5, angle);
      
        int distx = currentLocation.x + launchPoint.x;
        int disty = currentLocation.y + launchPoint.y;
      

        Missle missle = new Missle(distx, disty, angle);
      
        sb.addBlob(missle);
    }
}