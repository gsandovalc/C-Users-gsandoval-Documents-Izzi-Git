package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

import televisa.telecom.com.util.MobileResponse;

/**
 * Created by cevelez on 21/04/2015.
 */

@Table(name = "Usuario")
public class Usuario extends Model implements MobileResponse {
    @Column
    public String userName;
    @Column
    public String password;
    @Column
    public String apellidoMaterno;
    @Column
    public String apellidoPaterno;
    @Column
    public String correoContacto;
    @Column
    public String cvEmail;
    @Column
    public String cvLastBalance;
    @Column
    public String cvMailAddres;
    @Column
    public String cvNameAccount;
    @Column
    public String cvNumberAccount;
    @Column
    public String fechaFactura ;
    @Column
    public String fechaLimite ;
    @Column
    public String ipCable;
    @Column
    public String nombreContacto;
    @Column
    public String telefonoPrincipal;
    @Column
    public String fechaUltimoPago;
    @Column
    public boolean tieneCuentaSiebel;
    @Column
    public String tipoSource;


    //Datos clientes migrados Cablemas
    @Column
    public String telMigrado;
    @Column
    public String ctaMigrada;

    // Propiedades comunes
    @Column
    public boolean digitalFlag;
    @Column
    public String latitud;
    @Column
    public String longitud;
    @Column
    public String nodo;
    @Column
    public String ramal;
    @Column
    public String hub;
    @Column
    public String retorno;
    @Column(name = "rpt")
    public String rpt;
    @Column
    public Integer antiguedad;
    @Column
    public String clienteDesde;
    @Column
    public String perfilPago;
    @Column
    private boolean legacy=false;
    @Column
    public String paquete;
    @Column
    private String token;
    @Column
    public String fotoPerfil;
    @Column
    public  TarjetaCreditoVL payment;
    public List<PagosList> pagos = null;

    //seccion de extras;
    @Column
    public boolean extrasInternet=false;
    @Column
    public boolean extrasVideo=false;
    @Column
    public boolean extrasTelefono=false;
    public List<String> dataExtrasInternet;
    public List<String> dataExtrasVideo;
    public List<String> dataExtrasTelefono;

    @Column
    public  String extraInternet;
    @Column
    public  String extraVideo;


    @Column
    public  String extraTelefono;

    @Column
    public String bankLineN1;
    @Column
    public String bankLineN2;
    @Column
    public String bankLineN3;
    @Column
    public String bankLineN4;
    @Column
    public String bankLineN5;
    @Column
    public String bankLineN6;
    @Column
    public String bankLine1;
    @Column
    public String bankLine2;
    @Column
    public String bankLine3;
    @Column
    public String bankLine4;
    @Column
    public String bankLine5;
    @Column
    public String bankLine6;
    @Column
    private String paqTotal;
    @Column
    private String otrosTotal;
    @Column
    private String telTotal;
    @Column
    private String intTotal;
    @Column
    private String vidTotal;
    @Column
    private String veoTotal;
    @Column
    private String bonTotal;
    @Column
    private String saldoMesAnterior;
    @Column
    private String saldoTotalCta;
    @Column
    private String edoDate;
    @Column
    private String edoDueDate;
    @Column
    private String paqName;
    @Column
    private boolean paperless=false;
    @Column
    private String barcode="";
    @Column
    private String extra1=null;
    @Column
    private String extra2=null;


    public Usuario() {
        super();
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getCvEmail() {
        return cvEmail;
    }

    public void setCvEmail(String cvEmail) {
        this.cvEmail = cvEmail;
    }

    public String getCvLastBalance() {
        return cvLastBalance;
    }

    public void setCvLastBalance(String cvLastBalance) {
        this.cvLastBalance = cvLastBalance;
    }

    public String getCvMailAddres() {
        return cvMailAddres;
    }

    public void setCvMailAddres(String cvMailAddres) {
        this.cvMailAddres = cvMailAddres;
    }

    public String getCvNameAccount() {
        return cvNameAccount;
    }

    public void setCvNameAccount(String cvNameAccount) {
        this.cvNameAccount = cvNameAccount;
    }

    public String getCvNumberAccount() {
        return cvNumberAccount;
    }

    public void setCvNumberAccount(String cvNumberAccount) {
        this.cvNumberAccount = cvNumberAccount;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getIpCable() {
        return ipCable;
    }

    public void setIpCable(String ipCable) {
        this.ipCable = ipCable;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public String getFechaUltimoPago() {
        return fechaUltimoPago;
    }

    public void setFechaUltimoPago(String fechaUltimoPago) {
        this.fechaUltimoPago = fechaUltimoPago;
    }

    public boolean isTieneCuentaSiebel() {
        return tieneCuentaSiebel;
    }

    public void setTieneCuentaSiebel(boolean tieneCuentaSiebel) {
        this.tieneCuentaSiebel = tieneCuentaSiebel;
    }

    public String getTipoSource() {
        return tipoSource;
    }

    public void setTipoSource(String tipoSource) {
        this.tipoSource = tipoSource;
    }

    public String getTelMigrado() {
        return telMigrado;
    }

    public void setTelMigrado(String telMigrado) {
        this.telMigrado = telMigrado;
    }

    public String getCtaMigrada() {
        return ctaMigrada;
    }

    public void setCtaMigrada(String ctaMigrada) {
        this.ctaMigrada = ctaMigrada;
    }

    public boolean isDigitalFlag() {
        return digitalFlag;
    }

    public void setDigitalFlag(boolean digitalFlag) {
        this.digitalFlag = digitalFlag;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public String getRamal() {
        return ramal;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getRpt() {
        return rpt;
    }

    public void setRpt(String rpt) {
        this.rpt = rpt;
    }

    public Integer getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    public String getClienteDesde() {
        return clienteDesde;
    }

    public void setClienteDesde(String clienteDesde) {
        this.clienteDesde = clienteDesde;
    }

    public String getPerfilPago() {
        return perfilPago;
    }

    public void setPerfilPago(String perfilPago) {
        this.perfilPago = perfilPago;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(boolean isLegacy) {
        this.legacy = isLegacy;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public TarjetaCreditoVL getPayment() {
        return payment;
    }

    public void setPayment(TarjetaCreditoVL payment) {
        this.payment = payment;
    }

    public List<PagosList> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagosList> pagos) {
        this.pagos = pagos;
    }

    public boolean isExtrasInternet() {
        return extrasInternet;
    }

    public void setExtrasInternet(boolean extrasInternet) {
        this.extrasInternet = extrasInternet;
    }

    public boolean isExtrasVideo() {
        return extrasVideo;
    }

    public void setExtrasVideo(boolean extrasVideo) {
        this.extrasVideo = extrasVideo;
    }

    public boolean isExtrasTelefono() {
        return extrasTelefono;
    }

    public void setExtrasTelefono(boolean extrasTelefono) {
        this.extrasTelefono = extrasTelefono;
    }

    public List<String> getDataExtrasInternet() {
        return dataExtrasInternet;
    }

    public void setDataExtrasInternet(List<String> dataExtrasInternet) {
        this.dataExtrasInternet = dataExtrasInternet;
    }

    public List<String> getDataExtrasVideo() {
        return dataExtrasVideo;
    }

    public void setDataExtrasVideo(List<String> dataExtrasVideo) {
        this.dataExtrasVideo = dataExtrasVideo;
    }

    public List<String> getDataExtrasTelefono() {
        return dataExtrasTelefono;
    }

    public void setDataExtrasTelefono(List<String> dataExtrasTelefono) {
        this.dataExtrasTelefono = dataExtrasTelefono;
    }

    public String getBankLineN1() {
        return bankLineN1;
    }

    public void setBankLineN1(String bankLineN1) {
        this.bankLineN1 = bankLineN1;
    }

    public String getBankLineN2() {
        return bankLineN2;
    }

    public void setBankLineN2(String bankLineN2) {
        this.bankLineN2 = bankLineN2;
    }

    public String getBankLineN3() {
        return bankLineN3;
    }

    public void setBankLineN3(String bankLineN3) {
        this.bankLineN3 = bankLineN3;
    }

    public String getBankLineN4() {
        return bankLineN4;
    }

    public void setBankLineN4(String bankLineN4) {
        this.bankLineN4 = bankLineN4;
    }

    public String getBankLineN5() {
        return bankLineN5;
    }

    public void setBankLineN5(String bankLineN5) {
        this.bankLineN5 = bankLineN5;
    }

    public String getBankLineN6() {
        return bankLineN6;
    }

    public void setBankLineN6(String bankLineN6) {
        this.bankLineN6 = bankLineN6;
    }

    public String getBankLine1() {
        return bankLine1;
    }

    public void setBankLine1(String bankLine1) {
        this.bankLine1 = bankLine1;
    }

    public String getBankLine2() {
        return bankLine2;
    }

    public void setBankLine2(String bankLine2) {
        this.bankLine2 = bankLine2;
    }

    public String getBankLine3() {
        return bankLine3;
    }

    public void setBankLine3(String bankLine3) {
        this.bankLine3 = bankLine3;
    }

    public String getBankLine4() {
        return bankLine4;
    }

    public void setBankLine4(String bankLine4) {
        this.bankLine4 = bankLine4;
    }

    public String getBankLine5() {
        return bankLine5;
    }

    public void setBankLine5(String bankLine5) {
        this.bankLine5 = bankLine5;
    }

    public String getBankLine6() {
        return bankLine6;
    }

    public void setBankLine6(String bankLine6) {
        this.bankLine6 = bankLine6;
    }
    public String getExtraInternet() {
        return extraInternet;
    }

    public void setExtraInternet(String extraInternet) {
        this.extraInternet = extraInternet;
    }

    public String getExtraVideo() {
        return extraVideo;
    }

    public void setExtraVideo(String extraVideo) {
        this.extraVideo = extraVideo;
    }

    public String getExtraTelefono() {
        return extraTelefono;
    }

    public void setExtraTelefono(String extraTelefono) {
        this.extraTelefono = extraTelefono;
    }
    public String getPaqTotal() {
        return paqTotal;
    }

    public void setPaqTotal(String paqTotal) {
        this.paqTotal = paqTotal;
    }

    public String getOtrosTotal() {
        return otrosTotal;
    }

    public void setOtrosTotal(String otrosTotal) {
        this.otrosTotal = otrosTotal;
    }

    public String getTelTotal() {
        return telTotal;
    }

    public void setTelTotal(String telTotal) {
        this.telTotal = telTotal;
    }

    public String getVidTotal() {
        return vidTotal;
    }

    public void setVidTotal(String vidTotal) {
        this.vidTotal = vidTotal;
    }

    public String getVeoTotal() {
        return veoTotal;
    }

    public void setVeoTotal(String veoTotal) {
        this.veoTotal = veoTotal;
    }

    public String getBonTotal() {
        return bonTotal;
    }

    public void setBonTotal(String bonTotal) {
        this.bonTotal = bonTotal;
    }

    public String getSaldoMesAnterior() {
        return saldoMesAnterior;
    }

    public void setSaldoMesAnterior(String saldoMesAnterior) {
        this.saldoMesAnterior = saldoMesAnterior;
    }

    public String getSaldoTotalCta() {
        return saldoTotalCta;
    }

    public void setSaldoTotalCta(String saldoTotalCta) {
        this.saldoTotalCta = saldoTotalCta;
    }

    public String getEdoDate() {
        return edoDate;
    }

    public void setEdoDate(String edoDate) {
        this.edoDate = edoDate;
    }

    public String getPaqName() {
        return paqName;
    }

    public void setPaqName(String paqName) {
        this.paqName = paqName;
    }


    public String getEdoDueDate() {
        return edoDueDate;
    }

    public void setEdoDueDate(String edoDueDate) {
        this.edoDueDate = edoDueDate;
    }

    public String getIntTotal() {
        return intTotal;
    }

    public void setIntTotal(String intTotal) {
        this.intTotal = intTotal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isPaperless() {
        return paperless;
    }

    public void setPaperless(boolean paperless) {
        this.paperless = paperless;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }
}
