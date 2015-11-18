package net.unikit.result_determination.models.implementations.dummys;

import net.unikit.database.interfaces.entities.Appointment;
import net.unikit.database.interfaces.ids.AppointmentId;

import java.util.Date;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyAppointmentImpl implements Appointment {

    DummyDate startDate;
    DummyDate endDate;

    public DummyAppointmentImpl(DummyDate startDate, DummyDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DummyDate getDummyStartDate(){
        return startDate;
    }

    public DummyDate getDummyEndDate(){
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DummyAppointmentImpl that = (DummyAppointmentImpl) o;

        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        return !(endDate != null ? !endDate.equals(that.endDate) : that.endDate != null);

    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public AppointmentId getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getStartDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStartDate(Date date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getEndDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEndDate(Date date) {
        throw new UnsupportedOperationException();
    }
}
