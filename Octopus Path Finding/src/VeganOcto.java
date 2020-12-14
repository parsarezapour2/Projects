import processing.core.PImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class VeganOcto extends Moveable {
    private static final String QUAKE_KEY = "quake";

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    //comment and uncomment to use a different pathing strategy!
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();


    public VeganOcto(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod,animationPeriod,resourceLimit,resourceCount);

    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, 0),
                this.getAnimationPeriod());
    }



    public Point nextPosition(WorldModel world,
                              Point destPos) {
        List<Point> points;
        points = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);//implements the diagonal movement
        if (points.size() != 0)
            return points.get(0);
        return getPosition();
    }


    public boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            setresourceCount(1);
            world.removeEntity(target);

            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }




    public Optional<Entity> findNearest(WorldModel world,
                                        Class Cl)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.entities)
        {
            if (Cl.isInstance(entity))
            {
                ofType.add(entity);
            }
        }

        return this.nearestEntity(ofType, this.getPosition());
    }




    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> veganOctoTarget = this.findNearest(
                world, Sgrass.class);
        long nextPeriod = QUAKE_ACTION_PERIOD;


        if (veganOctoTarget.isPresent()) {
            Point tgtPos = veganOctoTarget.get().getPosition();

            if (moveToNotFull(world, veganOctoTarget.get(), scheduler)) {
                Entity quake = new Quake(QUAKE_ID, tgtPos, imageStore.getImageList(QUAKE_KEY), QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);


                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }
}
