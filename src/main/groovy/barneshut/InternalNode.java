package barneshut;

import common.Point;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public class InternalNode extends BHNode {

    BHNode[] externalNodes = new BHNode[4];

    private Point centerOfMass;

    public double getTotalMass() {
        double total = 0;
        for (BHNode node : externalNodes) {
            total += node.getMass();
        }
        return total;

    }

    public BHNode[] getExternalNodes() {
        return externalNodes;
    }


    public Point getCenterOfMass() {
        return centerOfMass;
    }

    @Override
    public boolean isInternalNode() {
        return true;
    }
}
