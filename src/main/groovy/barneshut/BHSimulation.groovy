package barneshut

import processing.core.PApplet
import common.VGraph

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.Edge
import visualizer.force.VNode

/**
 * @author Ashwin Rajeev
 * @since 1/26/12
 */
class BHSimulation extends PApplet {

    Graph g
    int width
    int height
    VGraph vgraph
    BHTree tree
    double step
    String nodeLabel = "name"

    BHSimulation(int width, int height) {
        this.step = 0.5
        this.width = width
        this.height = height
        this.vgraph = new VGraph();
    }


    @Override
    public void setup() {
        size(width, height)
        createNodes()

    }

    private void createNodes() {
        def vertices = g.getVertices()
        vertices.each {
            def node = new ExternalNode()
            node.underlying = it
            vgraph.vertices.put(it.getId().toString(), node)
        }
        this.tree = new BHTree(width, height, vgraph.vertices.values())
    }


    @Override
    public void draw() {

        background(150, 203, 0)
        tree.updateForces()
        tree.updatePositions(step)
        tree.getExternalNodeList().each {ExternalNode externalNode ->
            vgraph.vertices.put(externalNode.underlying.getId().toString(), externalNode)
            def x = externalNode.getLocation().getX().toFloat()
            def y = externalNode.getLocation().getY().toFloat()
            stroke(126)
            fill(126)
            ellipse(x, y.toFloat(), 10, 10)
        }

        //draw lines
        tree.getExternalNodeList().each {ExternalNode externalNode ->
            ExternalNode v = externalNode
            def inEdges = v.underlying.getInEdges([] as String[])
            inEdges.each { Edge e ->
                def vertex = e.outVertex
                ExternalNode get = vgraph.vertices.get(vertex.getId())
                def x = get.location.x
                def y = get.location.y
                stroke(126)
                line(x.toFloat(), y.toFloat(), v.location.x.toFloat(), v.location.y.toFloat())


            }

            def outEdges = v.underlying.getOutEdges([] as String[])
            outEdges.each { Edge e ->
                def vertex = e.inVertex
                ExternalNode get = vgraph.vertices.get(vertex.getId())
                def x = get.location.x
                def y = get.location.y
                stroke(255)
                line(x.toFloat(), y.toFloat(), v.location.x.toFloat(), v.location.y.toFloat())


            }
        }

        tree.updateVelocities(step)

    }


}
