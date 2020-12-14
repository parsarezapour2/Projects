import processing.core.PImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class Octo extends Moveable {
    //comment and uncomment to use a different pathing strategy!
    //private PathingStrategy strategy = new SingleStepPathingStrategy();
    private PathingStrategy strategy = new AStarPathingStrategy();


    public Octo(String id, Point position,
                List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount)
    {
        super(id, position, images, actionPeriod,animationPeriod,resourceLimit,resourceCount);

    }
    public void changeBehavior(){
        this.setActionPeriod(10);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, 0),
                this.getAnimationPeriod());
    }

    public void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore) {
        Octo octo = new Octo(this.getid(), this.getPosition(), this.getImages(),  this.getActionPeriod(), this.getAnimationPeriod(), this.getresourceLimit(), 0);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);


        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
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



    public boolean moveToFull(WorldModel world,
                              Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

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

    public boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            setresourceCount(1);
           // System.out.println(this.getresourceCount());
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


    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore) {
        if (this.getresourceCount() >= this.getresourceLimit()) {
            Octo octo = new Octo (this.getid(), this.getPosition(), this.getImages(),  this.getActionPeriod(), this.getAnimationPeriod(), this.getresourceLimit(), this.getresourceCount());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
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

        if(this.getresourceCount() >= this.getresourceLimit()) {


            Optional<Entity> fullTarget = findNearest(world, Atlantis.class);

            if (fullTarget.isPresent() &&
                    moveToFull(world, fullTarget.get(), scheduler)) {
                //at atlantis trigger animation
                fullTarget.get().scheduleActions(scheduler, world, imageStore);

                //transform to unfull
                transformFull(world, scheduler, imageStore);
            } else {
                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        this.getActionPeriod());//this
            }
        }
        else{
            Optional<Entity> notFullTarget = findNearest(world,
                    Fish.class);

            if (!notFullTarget.isPresent() ||
                    !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                    !transformNotFull(world, scheduler, imageStore)) {
                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        this.getActionPeriod());//this
            }
        }
    }
}
