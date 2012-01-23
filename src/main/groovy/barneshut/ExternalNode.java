package barneshut;

import com.tinkerpop.blueprints.pgm.Vertex;
import common.Point;

/**
 * @author Ashwin Rajeev
 * @since 1/23/12
 */
public class ExternalNode extends BHNode {

    private Vertex underlying;

    private Point location;

    private Point netForce;
    private int quad;

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getNetForce() {
        return netForce;
    }

    public void setNetForce(Point netForce) {
        this.netForce = netForce;
    }

    public Point getLocation() {
        return location;
    }

    public void setQuad(int quad) {
        this.quad = quad;
    }

    public int getQuad() {
        return quad;
    }

    @Override
    public boolean isInternalNode() {
        return false;
    }
}
