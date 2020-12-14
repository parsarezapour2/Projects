import processing.core.PImage;
import java.util.List;

abstract public class Moveable extends Actionable {

    protected int resourceLimit;
    protected int resourceCount;

    public Moveable(String id, Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount) {

        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public int getresourceLimit() {
        return resourceLimit;
    }

    public int getresourceCount() {
        return resourceCount;
    }

    public int setresourceCount(int count) {
        this.resourceCount = this.resourceCount + count;
        return count;
    }

}

