package net.unikit.result_determination.models.implementations.graphs;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.interfaces.graphs.Vertex;
import net.unikit.result_determination.models.interfaces.graphs.WeightedEdge;
import net.unikit.result_determination.utils.CourseSemesterComparator;
import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedPseudograph;

import java.util.Collections;
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
    public Graph<Team, WeightedEdge> createTeamgraph(List<Course> courses){
        WeightedPseudograph<Team,WeightedEdge> returnGraph = new WeightedPseudograph<>( WeightedEdge.class );
        // Sortiert die Course nach den Semestern und start in Semester 1
//        Collections.sort(courses,new CourseSemesterComparator());
        for(Course c : courses){

            for(Course c2 : courses){
                for(Team t : c.getTeams()){

                }
            }

        }

        return returnGraph;
    }
}
