package net.unikit.result_determination.models.implementations.graphs;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.interfaces.graphs.Vertex;

/**
 * Created by abq307 on 09.12.2015.
 */
public class VertexImpl implements Vertex {

    Team t;

    public VertexImpl(Team t){
            this.t = t;
    }

    @Override
    public Team getTeam() {
        return this.t;
    }

    @Override
    public Course getCourse() {
        return this.t.getCourse();
    }
}
