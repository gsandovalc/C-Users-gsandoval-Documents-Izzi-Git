package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Carlos on 26/03/17.
 */

public class ExtrasTv extends Model {
    @Column
    @Expose
    private String name;
    @Column
    @Expose
    private String description;
    @Column
    @Expose
    private String price;
    @Column
    @Expose
    private String ex_id;
    @Column
    @Expose
    private String speed="";
    @Column
    @Expose
    private boolean tienePromo=false;
    @Column
    @Expose
    private String promo="";
    @Expose
    private List<String> exclude;

    public List<String> getExcludeUtil() {
        return Arrays.asList(excludeMe.split(","));
    }

    public void setExcludeUtil(List<String> excludeUtil) {
        this.excludeUtil = excludeUtil;
    }

    private List<String> excludeUtil;
    @Column
    @Expose
    private String ajuste="0.0";
    @Column
    private String excludeMe="";

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Column
    @Expose
    private boolean actualExtra=false;

    private boolean selected=false;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getEx_id() {
        return ex_id;
    }
    public void setEx_id(String ex_id) {
        this.ex_id = ex_id;
    }
    @Override
    public String toString() {
        return "Extras [name=" + name + ", description=" + description + ", price=" + price + ", ex_id=" + ex_id
                + ", speed=" + speed + ", tienePromo=" + tienePromo + ", promo=" + promo + "]";
    }
    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public String getPromo() {
        return promo;
    }
    public void setPromo(String promo) {
        this.promo = promo;
    }
    public boolean isTienePromo() {
        return tienePromo;
    }
    public void setTienePromo(boolean tienePromo) {
        this.tienePromo = tienePromo;
    }
    public boolean isActualExtra() {
        return actualExtra;
    }
    public void setActualExtra(boolean actualExtra) {
        this.actualExtra = actualExtra;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public String getAjuste() {
        return ajuste;
    }

    public void setAjuste(String ajuste) {
        this.ajuste = ajuste;
    }

    public String getExcludeMe() {
        return excludeMe;
    }

    public void setExcludeMe(String excludeMe) {
        this.excludeMe = excludeMe;
    }
}
