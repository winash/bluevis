package viz.tests.force

import org.junit.After
import org.junit.Before
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLReader
import visualizer.force.ForceDirectedLayout
import org.junit.Test
import processing.core.PApplet
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory

/**
 * @author Ashwin Rajeev
 * @since 1/22/12
 */
class VisualizerTests {

    TinkerGraph graph
    ForceDirectedLayout visualizer
    GraphMLReader reader

    @Before
    public void start() {
        graph = TinkerGraphFactory.createTinkerGraph()
        graph.clear()
        reader = new GraphMLReader(graph)
    }

    @Test
    public void testVisualizingNodes() {
        reader.inputGraph(new FileInputStream(new File("testdata/graph1.graphml")))
        visualizer = new ForceDirectedLayout(g: graph, nodeLabel: "name")
        new DisplayFrame(visualizer).setVisible(true)
        while (1) {
            ;
        }
    }




    @After
    public void tearDown() {
        graph.clear()
    }

    public class DisplayFrame extends javax.swing.JFrame {
        public DisplayFrame(PApplet applet) {
            this.setSize(1024, 768)//The window Dimensions
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
            javax.swing.JPanel panel = new javax.swing.JPanel()
            panel.setBounds(20, 20, 600, 600)
            PApplet sketch = applet
            panel.add(sketch)
            this.add(panel)
            sketch.init()
            this.setVisible(true)
        }
    }


}
