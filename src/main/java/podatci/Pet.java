package podatci;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pet {

    @SerializedName("speciesId")
    @Expose
    private Integer speciesId;
    @SerializedName("petTypeId")
    @Expose
    private Integer petTypeId;
    @SerializedName("creatureId")
    @Expose
    private Integer creatureId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("canBattle")
    @Expose
    private Boolean canBattle;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("abilities")
    @Expose
    private List<Ability> abilities = null;

    public Integer getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }

    public Integer getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(Integer petTypeId) {
        this.petTypeId = petTypeId;
    }

    public Integer getCreatureId() {
        return creatureId;
    }

    public void setCreatureId(Integer creatureId) {
        this.creatureId = creatureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanBattle() {
        return canBattle;
    }

    public void setCanBattle(Boolean canBattle) {
        this.canBattle = canBattle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

}