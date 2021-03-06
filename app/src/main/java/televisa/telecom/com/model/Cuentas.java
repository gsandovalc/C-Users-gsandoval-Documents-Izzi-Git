package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;

/**
 * Created by cevelez on 23/11/2015.
 */
public class Cuentas extends Model {
    @Column
    @Expose
    private String cuentaNombre="";
    @Column
    @Expose
    private boolean cuentaTipo = false;
    @Column
    @Expose
    private String cuentaNumero="";
    public String getCuentaNombre() {
        return cuentaNombre;
    }
    public void setCuentaNombre(String cuentaNombre) {
        this.cuentaNombre = cuentaNombre;
    }
    public boolean isCuentaTipo() {
        return cuentaTipo;
    }
    public void setCuentaTipo(boolean cuentaTipo) {
        this.cuentaTipo = cuentaTipo;
    }
    public String getCuentaNumero() {
        return cuentaNumero;
    }
    public void setCuentaNumero(String cuentaNumero) {
        this.cuentaNumero = cuentaNumero;
    }
}
