package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by cevelez on 23/11/2015.
 */
public class Cuentas extends Model {
    @Column
    private String cuentaNombre="";
    @Column
    private boolean cuentaTipo = false;
    @Column
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
