import processing.core.PImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Crab extends Moveable {
    //comment and uncomment to use a different pathing strategy!
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();

    private static final String QUAKE_KEY = "quake";

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Crab(String id, Point position,
                List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceCount);


    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation(this, 0), getAnimationPeriod());
    }


    public boolean moveToCrab(Entity crab, WorldModel world,
                              Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = nextPositionCrab(world, target.getPosition());

            if (!crab.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(crab, nextPos);
            }
            return false;
        }
    }
/*
    public static Point nextPositionCrab(Entity entity, WorldModel world,
                                         Point destPos) {
        int horiz = Integer.signum(destPos.x - entity.getPosition().x);
        Point newPos = new Point(entity.getPosition().x + horiz,
                entity.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass().equals(Fish.class)))) {
            int vert = Integer.signum(destPos.y - entity.getPosition().y);
            newPos = new Point(entity.getPosition().x, entity.getPosition().y + vert);
            occupant = world.getOccupant(newPos);
            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass().equals(Fish.class)))) {
                newPos = entity.getPosition();
            }
        }

        return newPos;
    }
 */

    public Point nextPositionCrab(WorldModel world,
                              Point destPos) {
        List<Point> points;
        points = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
        if (points.size() != 0)
            return points.get(0);
        return getPosition();
    }


    public Optional<Entity> findNearest(WorldModel world,
                                        Class Cl) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.entities) {
            if (Cl.isInstance(entity)) {
                ofType.add(entity);
            }
        }

        return this.nearestEntity(ofType, this.getPosition());
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = this.findNearest(
                world, Sgrass.class);
        long nextPeriod = QUAKE_ACTION_PERIOD;


        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveToCrab(this, world, crabTarget.get(), scheduler)) {
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