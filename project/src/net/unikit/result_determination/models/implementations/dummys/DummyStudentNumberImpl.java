package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.ids.StudentNumber;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyStudentNumberImpl implements StudentNumber {

    String s;

    public DummyStudentNumberImpl(String studentNumber){
        this.s = studentNumber;
    }

    @Override
    public String getValue() {
        return s;
    }
}
