package net.unikit.result_determination.models.implementations.dummys;

/**
 * Created by abq307 on 18.11.2015.
 */
public class DummyDate {

    String kalenderWoche;
    String wochenTag;
    String uhrzeit;

    public DummyDate(String kalenderWoche, String wochenTag, String uhrzeit) {
        this.kalenderWoche = kalenderWoche;
        this.wochenTag = wochenTag;
        this.uhrzeit = uhrzeit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DummyDate dummyDate = (DummyDate) o;

        if (kalenderWoche != null ? !kalenderWoche.equals(dummyDate.kalenderWoche) : dummyDate.kalenderWoche != null)
            return false;
        if (wochenTag != null ? !wochenTag.equals(dummyDate.wochenTag) : dummyDate.wochenTag != null) return false;
        return !(uhrzeit != null ? !uhrzeit.equals(dummyDate.uhrzeit) : dummyDate.uhrzeit != null);

    }

    @Override
    public int hashCode() {
        int result = kalenderWoche != null ? kalenderWoche.hashCode() : 0;
        result = 31 * result + (wochenTag != null ? wochenTag.hashCode() : 0);
        result = 31 * result + (uhrzeit != null ? uhrzeit.hashCode() : 0);
        return result;
    }

    public String toString(){
        return "KW"+kalenderWoche+" Day:" + wochenTag + " Time:"+uhrzeit;
    }
    public String getWochenTag() {
        return wochenTag;
    }

    public String getKalenderWoche() {
        return kalenderWoche;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }
}
