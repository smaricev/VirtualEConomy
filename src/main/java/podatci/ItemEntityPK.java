package podatci;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by stjep on 27/08/2017.
 */
public class ItemEntityPK implements Serializable {
    private int id;
    private int petid;

    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "petid")
    @Id
    public int getPetid() {
        return petid;
    }

    public void setPetid(int petid) {
        this.petid = petid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEntityPK that = (ItemEntityPK) o;

        if (id != that.id) return false;
        if (petid != that.petid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + petid;
        return result;
    }
}
