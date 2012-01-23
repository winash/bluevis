package barneshut;

import common.Point;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public abstract class BHNode {


    private double mass;
    private InternalNode parent;

    public double getMass() {
        return mass;
    }

    public InternalNode getParent() {
        return parent;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public abstract boolean isInternalNode();
}
