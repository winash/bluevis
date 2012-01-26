package viz.tests.bh

import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.blueprints.pgm.util.graphml.GraphMLReader
import org.junit.After
import org.junit.Before
import org.junit.Test
import processing.core.PApplet
import visualizer.force.ForceDirectedLayout
import barneshut.BHSimulation

/**
 * @author Ashwin Rajeev
 * @since 1/22/12
 */
class BHVisualizerTests {

    TinkerGraph graph
    BHSimulation bhSimulation
    GraphMLReader reader

    @Before
    public void start() {
        graph = TinkerGraphFactory.createTinkerGraph()
        graph.clear()
        reader = new GraphMLReader(graph)
    }

    @Test
    public void testVisualizingNodes() {
        reader.inputGraph(new FileInputStream(new File("testdata/graph2.graphml")))
        bhSimulation = new BHSimulation(1366, 768)
        bhSimulation.g = graph
        new DisplayFrame(bhSimulation).setVisible(true)
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
            this.setSize(1366, 768)//The window Dimensions
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
            javax.swing.JPanel panel = new javax.swing.JPanel()
            panel.setBounds(0, 0, 1366, 768)
            PApplet sketch = applet
            panel.add(sketch)
            this.add(panel)
            sketch.init()
            this.setVisible(true)

        }
    }


}
