package podatci;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by stjep on 27/08/2017.
 */
@Entity
@Table(name = "aukcije", schema = "zavrsni", catalog = "")
public class AukcijeEntity {
    private Integer item;
    private String owner;
    private String realm;
    private Long bid;
    private Long buyout;
    private Integer quantity;
    private Integer cheaper;
    private int id;
    private Long auc;
    private Timestamp date;
    private Integer petSpeciesId;
    private Integer petBreedId;
    private Integer petLevel;
    private Integer petQualityId;

    @Basic
    @Column(name = "item")
    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    @Basic
    @Column(name = "owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Basic
    @Column(name = "realm")
    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Basic
    @Column(name = "bid")
    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "buyout")
    public Long getBuyout() {
        return buyout;
    }

    public void setBuyout(Long buyout) {
        this.buyout = buyout;
    }

    @Basic
    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "cheaper")
    public Integer getCheaper() {
        return cheaper;
    }

    public void setCheaper(Integer cheaper) {
        this.cheaper = cheaper;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "auc")
    public Long getAuc() {
        return auc;
    }

    public void setAuc(Long auc) {
        this.auc = auc;
    }

    @Basic
    @Column(name = "date")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "petSpeciesId")
    public Integer getPetSpeciesId() {
        return petSpeciesId;
    }

    public void setPetSpeciesId(Integer petSpeciesId) {
        this.petSpeciesId = petSpeciesId;
    }

    @Basic
    @Column(name = "petBreedId")
    public Integer getPetBreedId() {
        return petBreedId;
    }

    public void setPetBreedId(Integer petBreedId) {
        this.petBreedId = petBreedId;
    }

    @Basic
    @Column(name = "petLevel")
    public Integer getPetLevel() {
        return petLevel;
    }

    public void setPetLevel(Integer petLevel) {
        this.petLevel = petLevel;
    }

    @Basic
    @Column(name = "petQualityId")
    public Integer getPetQualityId() {
        return petQualityId;
    }

    public void setPetQualityId(Integer petQualityId) {
        this.petQualityId = petQualityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AukcijeEntity that = (AukcijeEntity) o;

        if (id != that.id) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (realm != null ? !realm.equals(that.realm) : that.realm != null) return false;
        if (bid != null ? !bid.equals(that.bid) : that.bid != null) return false;
        if (buyout != null ? !buyout.equals(that.buyout) : that.buyout != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (cheaper != null ? !cheaper.equals(that.cheaper) : that.cheaper != null) return false;
        if (auc != null ? !auc.equals(that.auc) : that.auc != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (petSpeciesId != null ? !petSpeciesId.equals(that.petSpeciesId) : that.petSpeciesId != null) return false;
        if (petBreedId != null ? !petBreedId.equals(that.petBreedId) : that.petBreedId != null) return false;
        if (petLevel != null ? !petLevel.equals(that.petLevel) : that.petLevel != null) return false;
        if (petQualityId != null ? !petQualityId.equals(that.petQualityId) : that.petQualityId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (realm != null ? realm.hashCode() : 0);
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (buyout != null ? buyout.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (cheaper != null ? cheaper.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (auc != null ? auc.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (petSpeciesId != null ? petSpeciesId.hashCode() : 0);
        result = 31 * result + (petBreedId != null ? petBreedId.hashCode() : 0);
        result = 31 * result + (petLevel != null ? petLevel.hashCode() : 0);
        result = 31 * result + (petQualityId != null ? petQualityId.hashCode() : 0);
        return result;
    }
}
