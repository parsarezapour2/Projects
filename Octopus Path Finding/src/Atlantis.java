import processing.core.PImage;
import java.util.List;

public class Atlantis extends Entity {


    private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;


    public  Atlantis(String id, Point position,
                 List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);

    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Animation(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }

    public void executeActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
