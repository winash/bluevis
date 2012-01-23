package visualizer.force


import com.tinkerpop.blueprints.pgm.Edge
import com.tinkerpop.blueprints.pgm.Vertex
import processing.core.PApplet
import com.tinkerpop.blueprints.pgm.Graph

/**
 * author: ashwin
 * Date: 1/20/12
 */
class ForceDirectedLayout extends PApplet {

    Graph g
    String nodeLabel
    VGraph graph


    @Override
    public void setup() {


        this.graph = new VGraph()
        size(1024, 768)
        createNodes()


    }



    private void createNodes() {
        def vertices = g.getVertices()
        vertices.each {
            def node = new VNode(underlying: it)
            node.pos.x = 300 + Math.random() * 300
            node.pos.y = 200 + Math.random() * 200
            graph.vertices[node.underlying.getId().toString()] = node
        }
    }

    @Override
    public void draw() {

        def count = 0
        def values = graph.vertices.values()
        def size = values.size()
        values.each { VNode vNode ->
            VNode u
            vNode.netForce.x = vNode.netForce.y = 0
            values.each { VNode iNode ->



                if (iNode.underlying.getId() != vNode.underlying.getId()) {

                    u = iNode
                    def rsq = ((vNode.pos.x - u.pos.x) * (vNode.pos.x - u.pos.x) + (vNode.pos.y - u.pos.y) * (vNode.pos.y - u.pos.y))
                    vNode.netForce.x += 300 * (vNode.pos.x - u.pos.x) / rsq
                    vNode.netForce.y += 300 * (vNode.pos.y - u.pos.y) / rsq
                }


            }
            vNode.underlying.getOutEdges([] as String[]).each { Edge e ->
                Vertex vertex = e.getInVertex()
                VNode sameV = graph.vertices.get(vertex.getId().toString())
                vNode.netForce.x += 0.05 * (sameV.pos.x - vNode.pos.x)
                vNode.netForce.y += 0.05 * (sameV.pos.y - vNode.pos.y)


            }

            vNode.underlying.getInEdges([] as String[]).each { Edge e ->
                Vertex vertex = e.getOutVertex()
                VNode sameV = graph.vertices.get(vertex.getId().toString())
                vNode.netForce.x += 0.05 * (sameV.pos.x - vNode.pos.x)
                vNode.netForce.y += 0.05 * (sameV.pos.y - vNode.pos.y)
            }

            vNode.velocity.x = (vNode.velocity.x + vNode.netForce.x) * 0.85
            vNode.velocity.y = (vNode.velocity.y + vNode.netForce.y) * 0.85


        }

        def c = 0
        background(150, 203, 0)
        values.each { VNode v ->
            c++

            v.pos.x += v.velocity.x
            v.pos.y += v.velocity.y
            println "drawing at ${v.pos.x}"
            println "drawing at ${v.pos.y}"
            println "----------------------"
            stroke(1)
            fill(1)
            ellipse(v.pos.x, v.pos.y, 20, 20);
            fill(250)
            text(v.underlying.getProperty(nodeLabel), v.pos.x, v.pos.y)
            def inEdges = v.underlying.getInEdges([] as String[])
            inEdges.each { Edge e ->
                def vertex = e.outVertex
                VNode get = graph.vertices.get(vertex.getId())
                def x = get.pos.x
                def y = get.pos.y
                stroke(126)
                line(x, y, v.pos.x, v.pos.y)
                writeText(x, y, v.pos.x, v.pos.y, e.label)


            }

            def outEdges = v.underlying.getOutEdges([] as String[])
            outEdges.each { Edge e ->
                def vertex = e.inVertex
                VNode get = graph.vertices.get(vertex.getId())
                def x = get.pos.x
                def y = get.pos.y
                stroke(255)
                line(x, y, v.pos.x, v.pos.y)
                writeText(x, y, v.pos.x, v.pos.y, e.label)


            }


        }


    }

    def writeText(float x, float y, float x1, float y1, String label) {
        def theX = x > x1 ? x - (x - x1) / 2 : x1 - (x1 - x) / 2
        def theY = y > y1 ? y - (y - y1) / 2 : y1 - (y1 - y) / 2
        text(label, (float) theX, (float) theY)


    }

}
