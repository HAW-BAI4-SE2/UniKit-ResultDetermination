package net.unikit.result_determination.models.interfaces.graphs;

import net.unikit.database.interfaces.entities.Course;
import net.unikit.database.interfaces.entities.Team;

/**
 * Created by abq307 on 09.12.2015.
 */
public interface Vertex {

    /**
     *
     * @return the team
     */
    public Team getTeam();

    /**
     *
     * @return the course
     */
    public Course getCourse();

}
