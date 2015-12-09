package net.unikit.result_determination.models.interfaces.graphs;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Created by abq307 on 09.12.2015.
 */
public class WeightedEdge extends DefaultWeightedEdge implements Comparable<WeightedEdge> {

    int numberOfConflictingStudents;

    /**
     *
     * @param numberOfConflictingStudents
     */
    public WeightedEdge(int numberOfConflictingStudents){
        this.numberOfConflictingStudents = numberOfConflictingStudents;
    }

    @Override
    public int compareTo(WeightedEdge weightedEdge) {
        return ((Integer)numberOfConflictingStudents).compareTo((Integer)numberOfConflictingStudents);
    }
}
