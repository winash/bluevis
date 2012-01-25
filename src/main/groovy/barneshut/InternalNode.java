package barneshut;

import common.Point;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public class InternalNode extends BHNode {

    BHNode[] externalNodes = new BHNode[4];

    private Point centerOfMass;

    private double width;

    public void setCenterOfMass(Point centerOfMass) {
        this.centerOfMass = centerOfMass;
    }

    public BHNode[] getExternalNodes() {
        return externalNodes;
    }


    public Point getCenterOfMass() {
        return centerOfMass;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public boolean isInternalNode() {
        return true;
    }


    public double getWidth() {
        return width;
    }
}
