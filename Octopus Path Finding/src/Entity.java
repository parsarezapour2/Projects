import processing.core.PImage;
import java.util.List;
import java.util.*;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */
abstract public class Entity {

    private Point position;
    private List<PImage> images;
    private int animationPeriod;
    private int imageIndex;
    private String id;

    public Entity( String id, Point position,
                   List<PImage> images, int animationPeriod)
    {
        this.id=id;
        this.position = position;
        this.images = images;
        this.animationPeriod = animationPeriod;
        this.imageIndex = 0;

    }
    public static final Random rand = new Random();

    public String getid() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public PImage getCurrentImage()
    {
        return this.images.get(this.imageIndex);
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public void setPosition(Point point){
        this.position = point;
    }

    public Point getPosition(){
        return this.position;
    }


    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public  Optional<Entity> nearestEntity(List<Entity> entities,
                                           Point pos){
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);
            for (Entity other : entities) {
                int otherDistance = Point.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

}