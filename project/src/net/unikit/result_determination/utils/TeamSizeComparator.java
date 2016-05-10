package net.unikit.result_determination.utils;

import net.unikit.database.interfaces.entities.Team;

import java.util.Comparator;

/**
 * Created by Jones on 10.12.2015.
 */
public class TeamSizeComparator implements Comparator<Team>{

    @Override
    public int compare(Team o1, Team o2) {
        Integer o1Val = o1.getTeamRegistrations().size();
        Integer o2Val = o2.getTeamRegistrations().size();
        return o1Val.compareTo(o2Val);
    }
}
