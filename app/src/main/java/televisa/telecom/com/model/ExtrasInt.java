package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;

/**
 * Created by Carlos on 26/03/17.
 */

public class ExtrasInt extends Model{
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

}
