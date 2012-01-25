package barneshut;

import common.Point;

import java.util.List;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public class BHTree {

    private double maxX;
    private double maxY;

    private static final double THETA = 0.5d;

    public BHTree(double max) {
        this.maxX = max;
        this.maxY = max;
    }

    private InternalNode root;

    public void insert(List<ExternalNode> externalNodeList) {
        for (ExternalNode node : externalNodeList) {
            if (null == root) {
                root = new InternalNode();
                root.setWidth(maxX);
            }
            final Point point = randomLoc(maxX);
            final int quad = randomQuadrant();
            node.setLocation(point);
            node.setQuad(quad);
            doInsert(root, node);
        }

        calculateMetrics(root);
        System.out.println("blah");

    }

    private void calculateMetrics(BHNode node) {
        double x = 0, y = 0;
        InternalNode temp = (InternalNode) node;
        final BHNode[] externalNodes = temp.getExternalNodes();
        for (BHNode n : externalNodes) {
            if (n != null) {
                if (n.isInternalNode()) {

                    calculateMetrics(n);
                    node.setMass(node.getMass() + n.getMass());
                    x += n.getMass() * ((InternalNode) n).getCenterOfMass().getX();
                    x += n.getMass() * ((InternalNode) n).getCenterOfMass().getY();

                } else {
                    final ExternalNode externalNode = (ExternalNode) n;
                    node.setMass(node.getMass() + externalNode.getMass());
                    x += externalNode.getLocation().getX() * externalNode.getMass();
                    y += externalNode.getLocation().getY() * externalNode.getMass();
                }
            }
            temp.setCenterOfMass(new Point(x / node.getMass(), y / node.getMass()));
        }

    }


    private void doInsert(BHNode inNode, ExternalNode node) {
        InternalNode temp = (InternalNode) inNode;
        final int quad = node.getQuad();
        BHNode n = temp.getExternalNodes()[quad];
        if (null != n) {
            // do an insert - check if insert position has an internal node
            if (n.isInternalNode()) {
                doInsert(n, node);
            } else {
                final ExternalNode externalNode = (ExternalNode) n;
                final InternalNode subdivided = subdivide(externalNode);
                reassignLoc(subdivided.getWidth(), externalNode);
                reassignLoc(subdivided.getWidth(), node);
                doInsert(subdivided, externalNode);
                node.setQuad(nextAvailableQuad(node.getQuad()));
                doInsert(subdivided, node);
            }
        } else {
            temp.getExternalNodes()[quad] = node;
            node.setParent(temp);
        }


    }

    private void reassignLoc(double width, ExternalNode node) {
        final Point point = randomLoc(width);
        node.setLocation(point);

    }

    private InternalNode subdivide(ExternalNode externalNode) {
        final InternalNode internalNode = new InternalNode();
        final InternalNode parent = externalNode.getParent();
        internalNode.setWidth(parent.getWidth() / 2);
        parent.getExternalNodes()[externalNode.getQuad()] = internalNode;
        return internalNode;

    }


    private Point randomLoc(double size) {
        return new Point(Math.random() * size, Math.random() * size);
    }

    private int nextAvailableQuad(int num) {
        if (num == 3)
            return 0;
        else return num + 1;


    }

    public void getForce(ExternalNode externalNode) {
        getForce(root, externalNode);
    }

    private void getForce(InternalNode root, ExternalNode externalNode) {
        // since root is internal , calculate s/d and compare with theta
        final float dx = root.getCenterOfMass().getX() - externalNode.getLocation().getX();
        final float dy = root.getCenterOfMass().getY() - externalNode.getLocation().getY();
        final double d = Math.sqrt(dx * dx + dy * dy);
        final double s = root.getWidth();
        if (s / d > THETA) {
            //calculate force and add to node's net force
        }


    }


    private int randomQuadrant() {
        return (int) (Math.random() * 4);
    }


}
