package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cevelez on 24/04/2015.
 */
public class TarjetaCreditoVL extends Model
{
    @Expose
    private String nombre;
    @Expose
    private String numero;
    @Expose
    private String tipo;
    @Expose
    private String codigoSeguridad;
    @Expose
    private String mesExpiracion;
    @Expose
    private String anioExpiracion;
    @Expose
    private String banco;
    @Expose
    private String formaPago;
    @Expose
    private boolean efectivo;

    public boolean isEfectivo() {
        return efectivo;
    }

    public void setEfectivo(boolean efectivo) {
        this.efectivo = efectivo;
    }
    public interface Tipo {
        String VISA = "V";
        String MASTERCARD = "MC";
        String AMERICANEXPRESS = "AMEX";
        String SEARS = "SEARS";
    }

    public interface FormaPago {
        String DEBITO = "D";
        String CREDITO = "C";
    }

    public boolean isVisa() {
        return Tipo.VISA.equals(tipo);
    }

    public boolean isMasterCard() {
        return Tipo.MASTERCARD.equals(tipo);
    }

    public boolean isAmericanExpress() {
        return Tipo.AMERICANEXPRESS.equals(tipo);
    }

    public boolean isSears() {
        return Tipo.SEARS.equals(tipo);
    }

    public boolean isDebito() {
        return FormaPago.DEBITO.equals(formaPago);
    }

    public boolean isCredito() {
        return FormaPago.CREDITO.equals(formaPago);
    }

    public Date getFechaExpiracion() {
        Calendar calendar = GregorianCalendar.getInstance();
        String anio = anioExpiracion;
        if(anioExpiracion.length()==2) {
            //TODO Optimizar esto para que no quede fijo el 20;cuando
            anio = "20"+anioExpiracion;
        }

        calendar.set(Integer.parseInt(anio), Integer.parseInt(mesExpiracion)-1, 1);
        int ultimoDiaMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH,ultimoDiaMes);
        return calendar.getTime();
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }
    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    public String getMesExpiracion() {
        return mesExpiracion;
    }
    public void setMesExpiracion(String mesExpiracion) {
        this.mesExpiracion = mesExpiracion;
    }

    public String getAnioExpiracion() {
        return anioExpiracion;
    }
    public void setAnioExpiracion(String anioExpiracion) {
        this.anioExpiracion = anioExpiracion;
    }

    public String getBanco() {
        return banco;
    }
    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getFormaPago() {
        return formaPago;
    }
    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }
}
