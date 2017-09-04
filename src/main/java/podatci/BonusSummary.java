
package podatci;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BonusSummary {

    @SerializedName("defaultBonusLists")
    @Expose
    private List<Object> defaultBonusLists = null;
    @SerializedName("chanceBonusLists")
    @Expose
    private List<Object> chanceBonusLists = null;
    @SerializedName("bonusChances")
    @Expose
    private List<Object> bonusChances = null;

    public List<Object> getDefaultBonusLists() {
        return defaultBonusLists;
    }

    public void setDefaultBonusLists(List<Object> defaultBonusLists) {
        this.defaultBonusLists = defaultBonusLists;
    }

    public List<Object> getChanceBonusLists() {
        return chanceBonusLists;
    }

    public void setChanceBonusLists(List<Object> chanceBonusLists) {
        this.chanceBonusLists = chanceBonusLists;
    }

    public List<Object> getBonusChances() {
        return bonusChances;
    }

    public void setBonusChances(List<Object> bonusChances) {
        this.bonusChances = bonusChances;
    }

}