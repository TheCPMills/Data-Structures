// package test;
// import main.graphs.*;

// import java.io.File;
// import java.io.IOException;

// import javax.imageio.ImageIO;
// import java.awt.image.BufferedImage;

// import com.mxgraph.layout.mxCircleLayout;
// import com.mxgraph.layout.mxIGraphLayout;
// import com.mxgraph.util.mxCellRenderer;

// import org.jgrapht.Graph;
// import org.jgrapht.ext.JGraphXAdapter;
// import org.jgrapht.graph.DefaultEdge;

// import org.junit.*;

// import java.awt.Color;

// public class GraphDrawer {
//     Graph<Integer, DefaultEdge> sG;

//     @Before
//     public void createGraph() throws IOException {
//         File imgFile = new File("C:/Users/cpmil/OneDrive/Desktop/Code/Data Structures/src/test/graph.png");
//         imgFile.createNewFile();

//         sG = new SimpleGraph<>();

//         sG.addVertex(1);
//         sG.addVertex(2);
//         sG.addVertex(3);
//         sG.addVertex(4);
//         sG.addVertex(5);
//         sG.addVertex(6);
//         sG.addVertex(7);

//         sG.addEdge(4, 3);
//         sG.addEdge(4, 6);

//         sG.addEdge(3, 1);
//         sG.addEdge(3, 2);

//         sG.addEdge(6, 5);
//         sG.addEdge(6, 7);
//     }

//     @Test
//     public void drawGraph() throws IOException {

//         JGraphXAdapter<Integer, DefaultEdge> graphAdapter = new JGraphXAdapter<Integer, DefaultEdge>(sG);
//         mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
//         layout.execute(graphAdapter.getDefaultParent());

//         BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2.0, Color.WHITE, true, null);
//         File imgFile = new File("C:/Users/cpmil/OneDrive/Desktop/Code/Data Structures/src/test/graph.png");
//         ImageIO.write(image, "PNG", imgFile);

//         Assert.assertTrue(imgFile.exists());
//     }
// }
