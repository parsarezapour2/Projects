import processing.core.PImage;
import java.util.List;

public class Obstacle extends Entity {

    public Obstacle(String id, Point position,
                    List<PImage> images, int animationPeriod) {
        super(id, position, images, animationPeriod);

    }

    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

}