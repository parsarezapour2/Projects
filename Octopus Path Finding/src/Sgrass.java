import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class Sgrass extends Actionable {


    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;
    private static final String FISH_KEY = "fish";



    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

    public void executeActivity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent()) {
            Fish fish = new Fish(FISH_ID_PREFIX + this.getid(), openPt.get(), imageStore.getImageList(FISH_KEY), FISH_CORRUPT_MIN +
                    Entity.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN), 0);

            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());

    }
    public  Sgrass(String id, Point position,
                 List<PImage> images,
                 int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);

    }

}