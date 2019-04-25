package byow.Core;

import java.util.List;

/** @source KDTree code copied from KDTree implementation in proj 2b */

public class KDTree {
    private Node root;

    public KDTree(List<Position> positions) {
        for (Position p : positions) {
            root = insertToConstructor(p, root, true);
        }
    }
    /* Used to test constructor using IntelliJ debugger
    public static void main(String[] args) {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));

        System.out.println(Double.compare(12, 6));
    }*/

    private Node insertToConstructor(Position p, Node n, boolean b) {
        if (n == null) {
            return new Node(p, b);
        }
        int cmp = comparePointToNode(p, n);
        if (cmp < 0) {
            n.left = insertToConstructor(p, n.left, !b);
        } else if (cmp > 0) {
            n.right = insertToConstructor(p, n.right, !b);
        } else {
            if (!p.equals(n.point)) {
                n.right = insertToConstructor(p, n.right, !b);
            }
        }
        return n;
    }
    /* Compares a new point "a" to a Node "b" based on what level
    node b is on (x-split or y-split). If a is towards the negative side
    of b (left or down), the method returns -1, and if a is towards
    the positive side (right or up), the method returns 1.
    Returns 0 if both points are on same axis.
     */
    private int comparePointToNode(Position a, Node b) {
        if (b.xsplit) {
            return Double.compare(a.getX(), b.point.getX());
        }
        return Double.compare(a.getY(), b.point.getY());
    }
    /* Creates a point on the axis of Node n that is the closest to the
    goal point and returns the squared-distance from the that point to
    the goal point.
     */
    private double findBadSideClosestPoint(Position goal, Node n) {
        Position onNodeLine;
        if (n.xsplit) {
            onNodeLine = new Position(n.point.getX(), goal.getY());
        } else {
            onNodeLine = new Position(goal.getX(), n.point.getY());
        }
        return Position.distance(onNodeLine, goal);
    }

    public Position nearest(int x, int y) {
        Position cmp = new Position(x, y);
        return nearestHelper(root, cmp, root).point;
    }

    /**
     * @source I got a significant amount of conceptual help from the
     * pseudocode provided in the KD-Tree lecture slides.
     * https://docs.google.com/presentation/d/1lsbD88IP3XzrPkWMQ_SfueEgf
     * iUbxdpo-90Xu_mih5U/edit#slide=id.g54b6d82fee_297_0
     */
    private Node nearestHelper(Node n, Position p, Node best) {
        Node goodSide;
        Node badSide;
        if (n == null) {
            return best;
        }
        if (Position.distance(p, n.point) < Position.distance(p, best.point)) {
            best = n;
        }
        if (comparePointToNode(p, n) < 0) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }
        best = nearestHelper(goodSide, p, best);
        if (findBadSideClosestPoint(p, n) < Position.distance(p, best.point)) {
            best = nearestHelper(badSide, p, best);
        }
        return best;
    }

    private class Node {
        Position point;
        Node left;
        Node right;
        boolean xsplit;

        Node(Position p, boolean b) {
            point = p;
            xsplit = b;
        }
    }
}
