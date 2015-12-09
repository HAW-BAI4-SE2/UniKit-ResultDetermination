package net.unikit.result_determination.models.implementations.graphs;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.result_determination.models.interfaces.graphs.Vertex;
import net.unikit.result_determination.models.interfaces.graphs.WeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.List;

/**
 * Created by abq307 on 09.12.2015.
 */
public class GraphBuilder {

    /**
     * Creates a graph for team conflicts
     * @param courses
     * @return
     */
    public Graph<Vertex, WeightedEdge> createTeamgraph(List<Course> courses){
        WeightedPseudograph<Vertex,WeightedEdge> returnGraph = new WeightedPseudograph<Vertex,WeightedEdge>( WeightedEdge.class );

        for(Course c : courses){

        }

        return returnGraph;
    }
}
