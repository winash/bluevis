package barneshut;

import common.Point;

import java.util.Collection;
import java.util.List;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public class BHTree {

    private static final double MINIMUM_MASS = 1e1;
    private static final double MAXIMUM_MASS = 1e2;
    private static final double G = 6.673e-25;

    private double maxX;
    private double maxY;

    public Collection<ExternalNode> getExternalNodeList() {
        return externalNodeList;
    }

    private Collection<ExternalNode> externalNodeList;

    private static final double THETA = 0.8d;

    public BHTree(double maxX, double maxY, Collection<ExternalNode> externalNodeList) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.externalNodeList = externalNodeList;
        buildTree(true);
    }

    private InternalNode root;

    public void buildTree(boolean randomized) {
        root = null;
        for (ExternalNode node : externalNodeList) {
            if (null == root) {
                root = new InternalNode();
                root.setBox(new Box(0.0, 0.0, maxX, maxY));
            }
            if (randomized) {
                final Point point = randomLoc(maxX, maxY);
                node.setMass(Math.random() * (MAXIMUM_MASS - MINIMUM_MASS) + MINIMUM_MASS);
                node.setLocation(point);
                node.setAcceleration(new Point(0, 0));
                node.setVelocity(randomVelocity());
            }
            clearNode(node);
            doInsert(root, node);
        }

        calculateMetrics(root);

    }

    private void clearNode(ExternalNode node) {
        node.setParent(null);
    }

    private Point randomVelocity() {
        return new Point(Math.random() * 10 - 5, Math.random() * 10 - 5);
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
        if (hasLeftArea(node))
            return;
        InternalNode temp = (InternalNode) inNode;
        final int quad = getQuadrant(node, temp.getBox());
        BHNode n = temp.getExternalNodes()[quad];
        if (null != n) {
            // do an insert - check if insert position has an internal node
            if (n.isInternalNode()) {
                doInsert(n, node);
            } else {

                final ExternalNode externalNode = (ExternalNode) n;
                final InternalNode subdivided = subdivide(externalNode);
                doInsert(subdivided, externalNode);
                doInsert(subdivided, node);
            }
        } else {
            temp.getExternalNodes()[quad] = node;
            node.setParent(temp);
        }


    }

    private boolean hasLeftArea(ExternalNode node) {
        final Box box = root.getBox();
        final Point location = node.getLocation();
        if (location.getX() >= box.getX1() && location.getX() <= box.getX2() && location.getY() >= box.getY1() && location.getY() <= box.getY2()) {
            return false;
        }
        return true;
    }

    private InternalNode subdivide(ExternalNode externalNode) {
        final InternalNode internalNode = new InternalNode();
        final InternalNode parent = externalNode.getParent();
        final Box box = parent.getBox();
        final int quadrant = getQuadrant(externalNode, box);
        //for this quadrant split parents box
        Box dividedBox = getBoxFor(box, quadrant);

        internalNode.setBox(dividedBox);

        parent.getExternalNodes()[quadrant] = internalNode;
        return internalNode;

    }

    private Box getBoxFor(Box box, int quadrant) {
        switch (quadrant) {
            case 0: {
                return new Box(box.getX1(), box.getY1(), (box.getX1() + box.getX2()) / 2, (box.getY1() + box.getY2()) / 2);
            }
            case 1: {
                return new Box(box.getX1(), (box.getY1() + box.getY2()) / 2, (box.getX1() + box.getX2()) / 2, box.getY2());
            }
            case 2: {
                return new Box((box.getX1() + box.getX2()) / 2, box.getY1(), box.getX2(), (box.getY1() + box.getY2()) / 2);

            }
            case 3: {
                return new Box((box.getX1() + box.getX2()) / 2, (box.getY1() + box.getY2()) / 2, box.getX2(), box.getY2());

            }

        }
        return null;
    }

    private int getQuadrant(ExternalNode externalNode, Box box) {
        final double mx = (box.getX1() + box.getX2()) / 2;
        final double my = (box.getY1() + box.getY2()) / 2;
        if (externalNode.getLocation().getX() < mx) {
            if (externalNode.getLocation().getY() < my) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (externalNode.getLocation().getY() < my) {
                return 2;
            } else {
                return 3;
            }
        }
    }


    private Point randomLoc(double maxX, double maxY) {
        return new Point(Math.random() * maxX, Math.random() * maxY);
    }

    public void updateForces() {
        buildTree(false);
        for (ExternalNode externalNode : externalNodeList) {
            getForce(root, externalNode);
        }
    }

    private void getForce(BHNode root, ExternalNode externalNode) {
        if (root.isInternalNode()) {
            final InternalNode internalNode = (InternalNode) root;
            // calculate s/d and compare with theta
            final double d = getDistanceBetween(externalNode, internalNode);
            final double s = Math.min(internalNode.getBox().getX2() - internalNode.getBox().getX1(), internalNode.getBox().getY2() - internalNode.getBox().getY1());
            if (s / d < THETA) {
                final Point force = getForceAsPoint(externalNode, internalNode);
                updateAcceleration(force, externalNode);
            } else {
                //recursive call
                final BHNode[] externalNodes = internalNode.getExternalNodes();
                for (BHNode node : externalNodes) {
                    if (null != node) {
                        getForce(node, externalNode);
                    }

                }

            }
        } else {
            final ExternalNode node = (ExternalNode) root;
            if (!node.equals(externalNode)) {
                final Point force = getForceAsPoint(node, externalNode);
                updateAcceleration(force, externalNode);
            }
        }


    }

    private Point getForceAsPoint(ExternalNode node, ExternalNode externalNode) {
        final double dx = externalNode.getLocation().getX() - node.getLocation().getX();
        final double dy = externalNode.getLocation().getY() - node.getLocation().getY();
        final double distance = getDistanceBetween(node, externalNode);
        final double force = G * externalNode.getMass() * node.getMass() / Math.pow(distance, 2);
        return new Point(force * dx / distance, force * dy / distance);


    }

    private double getDistanceBetween(ExternalNode node, ExternalNode externalNode) {
        final double dx = node.getLocation().getX() - externalNode.getLocation().getX();
        final double dy = node.getLocation().getY() - externalNode.getLocation().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void updateAcceleration(Point force, ExternalNode externalNode) {
        final double accX = force.getX() / externalNode.getMass();
        final double accY = force.getY() / externalNode.getMass();
        externalNode.setAcceleration(new Point(accX, accY));

    }

    private Point getForceAsPoint(ExternalNode externalNode, InternalNode internalNode) {
        final double dx = internalNode.getCenterOfMass().getX() - externalNode.getLocation().getX();
        final double dy = internalNode.getCenterOfMass().getY() - externalNode.getLocation().getY();
        final double distance = getDistanceBetween(externalNode, internalNode);
        final double force = G * externalNode.getMass() * internalNode.getMass() / Math.pow(distance, 2);
        return new Point(force * dx / distance, force * dy / distance);

    }

    private double getDistanceBetween(ExternalNode externalNode, InternalNode internalNode) {
        final double dx = internalNode.getCenterOfMass().getX() - externalNode.getLocation().getX();
        final double dy = internalNode.getCenterOfMass().getY() - externalNode.getLocation().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }


    public void updateVelocities(double step) {
        for (ExternalNode externalNode : externalNodeList) {
            externalNode.getVelocity().setX(externalNode.getVelocity().getX() + (externalNode.getAcceleration().getX() * step));
            externalNode.getVelocity().setY(externalNode.getVelocity().getY() + (externalNode.getAcceleration().getY() * step));
        }
    }

    public void updatePositions(double step) {
        for (ExternalNode externalNode : externalNodeList) {
            externalNode.getLocation().setX(externalNode.getLocation().getX() + (externalNode.getVelocity().getX() * step));
            externalNode.getLocation().setY(externalNode.getLocation().getY() + (externalNode.getVelocity().getY() * step));
        }
    }


}
