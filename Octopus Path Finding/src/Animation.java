public class Animation implements Action {

    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;


    public void executeAction(EventScheduler scheduler) {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                   new Animation(this.entity,
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }


    public Animation(Entity entity, int repeatCount) {

        this.entity = entity;
        this.repeatCount = repeatCount;
        this.world = null;
        this.imageStore = null;
 }
}
