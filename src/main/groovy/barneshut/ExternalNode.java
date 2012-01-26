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

    private Point acceleration;

    private Point velocity;

    private int quad;

    public void setUnderlying(Vertex underlying) {
        this.underlying = underlying;
    }

    public Vertex getUnderlying() {
        return underlying;
    }

    public Point getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Point acceleration) {
        this.acceleration = acceleration;
    }

    public Point getVelocity() {
        return velocity;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public ExternalNode() {
        this.mass = 1.0d;
    }


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternalNode that = (ExternalNode) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }
}
