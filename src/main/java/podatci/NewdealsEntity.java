package podatci;

import javax.persistence.*;

/**
 * Created by stjep on 27/08/2017.
 */
@Entity
@Table(name = "newdeals", schema = "zavrsni", catalog = "")
public class NewdealsEntity {
    private int id;
    private Integer price;
    private Integer avgprice;
    private String lastseen;
    private String name;
    private Integer server;
    private String sname;
    private Integer razlika;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "avgprice")
    public Integer getAvgprice() {
        return avgprice;
    }

    public void setAvgprice(Integer avgprice) {
        this.avgprice = avgprice;
    }

    @Basic
    @Column(name = "lastseen")
    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "server")
    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    @Basic
    @Column(name = "sname")
    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    @Basic
    @Column(name = "razlika")
    public Integer getRazlika() {
        return razlika;
    }

    public void setRazlika(Integer razlika) {
        this.razlika = razlika;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewdealsEntity that = (NewdealsEntity) o;

        if (id != that.id) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (avgprice != null ? !avgprice.equals(that.avgprice) : that.avgprice != null) return false;
        if (lastseen != null ? !lastseen.equals(that.lastseen) : that.lastseen != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (server != null ? !server.equals(that.server) : that.server != null) return false;
        if (sname != null ? !sname.equals(that.sname) : that.sname != null) return false;
        if (razlika != null ? !razlika.equals(that.razlika) : that.razlika != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (avgprice != null ? avgprice.hashCode() : 0);
        result = 31 * result + (lastseen != null ? lastseen.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (server != null ? server.hashCode() : 0);
        result = 31 * result + (sname != null ? sname.hashCode() : 0);
        result = 31 * result + (razlika != null ? razlika.hashCode() : 0);
        return result;
    }
}
