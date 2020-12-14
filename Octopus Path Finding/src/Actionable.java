import processing.core.PImage;
import java.util.List;

abstract public class Actionable extends Entity{

    private int actionPeriod;

    public Actionable( String id, Point position,
                       List<PImage> images, int actionPeriod, int animationPeriod){

        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    abstract void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore);

    public int getActionPeriod() {
        return actionPeriod;
    }
    public void setActionPeriod(int actionPeriod) {
        this.actionPeriod = actionPeriod;
    }
}
