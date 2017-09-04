package podatci;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by stjep on 27/08/2017.
 */
@Entity
@Table(name = "profit", schema = "zavrsni", catalog = "")
public class ProfitEntity {
    private Long auction;
    private Long minprofit;
    private Long midprofit;
    private Long maxprofit;
    private Timestamp date;

    @Basic
    @Column(name = "auction")
    public Long getAuction() {
        return auction;
    }

    public void setAuction(Long auction) {
        this.auction = auction;
    }

    @Basic
    @Column(name = "minprofit")
    public Long getMinprofit() {
        return minprofit;
    }

    public void setMinprofit(Long minprofit) {
        this.minprofit = minprofit;
    }

    @Basic
    @Column(name = "midprofit")
    public Long getMidprofit() {
        return midprofit;
    }

    public void setMidprofit(Long midprofit) {
        this.midprofit = midprofit;
    }

    @Basic
    @Column(name = "maxprofit")
    public Long getMaxprofit() {
        return maxprofit;
    }

    public void setMaxprofit(Long maxprofit) {
        this.maxprofit = maxprofit;
    }

    @Basic
    @Column(name = "date")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfitEntity that = (ProfitEntity) o;

        if (auction != null ? !auction.equals(that.auction) : that.auction != null) return false;
        if (minprofit != null ? !minprofit.equals(that.minprofit) : that.minprofit != null) return false;
        if (midprofit != null ? !midprofit.equals(that.midprofit) : that.midprofit != null) return false;
        if (maxprofit != null ? !maxprofit.equals(that.maxprofit) : that.maxprofit != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = auction != null ? auction.hashCode() : 0;
        result = 31 * result + (minprofit != null ? minprofit.hashCode() : 0);
        result = 31 * result + (midprofit != null ? midprofit.hashCode() : 0);
        result = 31 * result + (maxprofit != null ? maxprofit.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
