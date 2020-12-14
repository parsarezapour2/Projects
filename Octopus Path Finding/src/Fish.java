import processing.core.PImage;
import java.util.List;

public class Fish extends Actionable{

    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;



    public  Fish(String id, Point position,
                 List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);

    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }



    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler) {


        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity crab = new Crab(this.getid() + CRAB_ID_SUFFIX, pos, imageStore.getImageList(CRAB_KEY), 0, 0,
                this.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        Entity.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN)
                );

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }
}
