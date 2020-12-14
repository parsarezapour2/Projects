//Joe and Brenden

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.*;
import java.util.stream.*;

public class AStarPathingStrategy
        implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        List<Point> path = new LinkedList<>();

        Comparator<EntityNode> comparing = Comparator.comparing(EntityNode::getF); //compares the current entity node through point, to the F Value. computing and soting the shortest path
        Map<Point, EntityNode> closed = new HashMap<>();//Given a key and a value, you can store the value in a Map object. After the value is stored, you can retrieve it by using its key.
        PriorityQueue<EntityNode> open = new PriorityQueue<>(comparing);//selects the smallest f value in the list and sets as target

        int G_VALUE = 0;//the cost of moving from the initial cell to the current cell. Basically, it is the sum of all the cells that have been visited since leaving the first cell.
        int H_VALUE = getHeuristic(start, end);//the estimated movement cost to move from that given square on the grid to the final destination.
        int F_VALUE = G_VALUE + H_VALUE;//takes f value and selects the smallest f value and sets it as the target node

        EntityNode nodeNow = new EntityNode(start, G_VALUE, H_VALUE, F_VALUE, null);//new entity node current that has parameters start, gvalue, h, f and prior. Prior is null because there is no prior cell when they are first spawned

        open.add(nodeNow);

        while (open.size() > 0) {

            nodeNow = open.poll();//selects the value at the top of the open list (priority queue)

            if (withinReach.test(nodeNow.getPos(), end))
            {
                mapPathing(path, nodeNow);
                break;
            }

            closed.put(nodeNow.getPos(), nodeNow);

            List<Point> neighbors = potentialNeighbors

                    .apply(nodeNow.getPos())

                    .filter(canPassThrough)

                    .filter(p -> !closed.containsKey(p))

                    .collect(Collectors.toList());


            for (Point neighbor : neighbors) {

                G_VALUE = nodeNow.getG() + 1;

                H_VALUE = getHeuristic(neighbor, end);

                F_VALUE = H_VALUE + G_VALUE;


                EntityNode neigh = new EntityNode(neighbor, G_VALUE, H_VALUE, F_VALUE, nodeNow);

                if (!open.contains(neigh))
                    open.add(neigh);

            }

        }

        return path;

    }private void mapPathing(List<Point> path, EntityNode node) {
        if (node.getPrior() == null)
            return;
        path.add(0, node.getPos());
        if (node.getPrior().getPrior() != null){
            mapPathing(path, node.getPrior());
        }
    }

    private int getHeuristic(Point p1, Point p2) {
        return Math.abs((p1.getY() - p2.getY()) + (p1.getX() - p2.getX()));
    }

    private class EntityNode {
        private Point position;
        private int g;
        private int h;
        private int f;
        private EntityNode prior;


        public EntityNode(Point position, int g, int h, int f, EntityNode prior) {
            this.position = position;
            this.g = g;
            this.h = h;
            this.f = f;
            this.prior = prior;
        }

        public boolean equals(Object other) {
            if (other == null) {
                return false;
            }
            if (other.getClass() != getClass()) {
                return false;
            }
            return ((EntityNode)other).position.equals(this.position);
        }


        public int getF() { return f; }
        public int getG() { return g; }
        public int getH() { return h; }
        public Point getPos() { return position; }
        public EntityNode getPrior() { return prior; }
    }
}