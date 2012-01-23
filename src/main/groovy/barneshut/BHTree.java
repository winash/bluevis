package barneshut;

import common.Point;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public class BHTree {

    private double maxX;
    private double maxY;

    public BHTree(double maxX, double maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    private BHNode root;

    private void insert(ExternalNode externalNode) {
        if (null == root)
            root = new InternalNode();
        final Point point = randomLoc();
        final int quad = randomQuadrant();
        final ExternalNode node = new ExternalNode();
        node.setLocation(point);
        node.setQuad(quad);
        doInsert(node);
    }

    private void doInsert(ExternalNode node) {
        InternalNode temp = (InternalNode) root;
        final int quad = node.getQuad();
        BHNode n = temp.getExternalNodes()[quad];
        if (null == n) {
            if(n.isInternalNode()){

            }
        }


    }

    private Point randomLoc() {
        return new Point(Math.random() * maxX, Math.random() * maxY);
    }


    private int randomQuadrant() {
        return (int) (Math.random() * 4);
    }


}
