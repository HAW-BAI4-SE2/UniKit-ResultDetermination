package net.unikit.result_determination.utils;

import net.unikit.database.interfaces.entities.Team;
import net.unikit.result_determination.models.implementations.ExtendedTeam;

import java.util.Comparator;

/**
 * Created by Jones on 03.01.2016.
 */
public class NewTeamSizeComparator implements Comparator<ExtendedTeam> {

    @Override
    public int compare(ExtendedTeam o1, ExtendedTeam o2) {
        Integer o1Val = o1.getTeamRegistrations().size();
        Integer o2Val = o2.getTeamRegistrations().size();
        return o1Val.compareTo(o2Val);
    }

}
