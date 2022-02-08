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