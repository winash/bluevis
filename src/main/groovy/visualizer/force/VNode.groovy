package visualizer.force

import com.tinkerpop.blueprints.pgm.Vertex
import common.Point

/**
 * author: ashwin
 * Date: 1/20/12
 */
class VNode {


    Vertex underlying;

    Point pos = new Point()

    Point velocity = new Point()

    Point netForce = new Point()
}
