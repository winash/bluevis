package viz.tests.bh

import org.junit.After
import org.junit.Before
import org.junit.Test
import barneshut.BHTree
import barneshut.ExternalNode

/**
 * @author Ashwin Rajeev
 * @since 1/24/12
 */
class BHTreeTests {

    BHTree tree

    @Before
    public void setUp() {
        tree = new BHTree(1027, 768)

    }

    @Test
    public void testInserts() {
        def nodes = []
        def list = 1..10
        list.each {
            nodes << new ExternalNode()

        }

        tree.insert(nodes)

    }


    @After
    public void tearDown() {
    }
}
