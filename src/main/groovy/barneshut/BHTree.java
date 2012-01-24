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

    public BHTree(double maxX, double maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    private BHNode root;

    public void insert(List<ExternalNode> externalNodeList) {
        for (ExternalNode externalNode : externalNodeList) {
            if (null == root)
                root = new InternalNode();
            final Point point = randomLoc();
            final int quad = randomQuadrant();
            final ExternalNode node = new ExternalNode();
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
                final InternalNode internalNode = (InternalNode) n;
                doInsert(n, node);
            } else {
                final ExternalNode externalNode = (ExternalNode) n;
                final InternalNode subdivided = subdivide(externalNode);
                doInsert(subdivided, externalNode);
                node.setQuad(nextAvailableQuad(node.getQuad()));
                doInsert(subdivided, node);
            }
        } else {
            temp.getExternalNodes()[quad] = node;
            node.setParent(temp);
        }


    }

    private InternalNode subdivide(ExternalNode externalNode) {
        final InternalNode internalNode = new InternalNode();
        final InternalNode parent = externalNode.getParent();
        parent.getExternalNodes()[externalNode.getQuad()] = internalNode;
        return internalNode;

    }


    private Point randomLoc() {
        return new Point(Math.random() * maxX, Math.random() * maxY);
    }

    private int nextAvailableQuad(int num) {
        if (num == 3)
            return 0;
        else return num + 1;


    }


    private int randomQuadrant() {
        return (int) (Math.random() * 4);
    }


}
