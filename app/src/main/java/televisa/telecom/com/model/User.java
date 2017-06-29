package televisa.telecom.com.model;


import java.util.List;

import televisa.telecom.com.util.MobileResponse;

/**
 * Created by cevelez on 21/04/2015.
 */


public class User implements MobileResponse {
   
    public String userName;
   
    public String password;
   
    public String apellidoMaterno;
   
    public String apellidoPaterno;
   
    public String correoContacto;
   
    public String cvEmail;
   
    public String cvLastBalance;
   
    public String cvMailAddres;
   
    public String cvNameAccount;
   
    public String cvNumberAccount;
   
    public String fechaFactura ;
   
    public String fechaLimite ;
   
    public String ipCable;
   
    public String nombreContacto;
   
    public String telefonoPrincipal;
   
    public String fechaUltimoPago;
   
    public boolean tieneCuentaSiebel;
   
    public String tipoSource;


    //Datos clientes migrados Cablemas
   
    public String telMigrado;
   
    public String ctaMigrada;

    // Propiedades comunes
   
    public boolean digitalFlag;
   
    public String latitud;
   
    public String longitud;
   
    public String nodo;
   
    public String ramal;
   
    public String hub;
   
    public String retorno;

    public String rpt;
   
    public Integer antiguedad;
   
    public String clienteDesde;
   
    public String perfilPago;
   
    private boolean legacy=false;
   
    public String paquete;
   
    private String token;
   
    public String fotoPerfil;
   
    public  TarjetaCreditoVL payment;
    public List<PagosList> pagos = null;

    //seccion de extras;
   
    public boolean extrasInternet=false;
   
    public boolean extrasVideo=false;
   
    public boolean extrasTelefono=false;
    public List<String> dataExtrasInternet;
    public List<String> dataExtrasVideo;
    public List<String> dataExtrasTelefono;

   
    public  String extraInternet;
   
    public  String extraVideo;


   
    public  String extraTelefono;

   
    public String bankLineN1;
   
    public String bankLineN2;
   
    public String bankLineN3;
   
    public String bankLineN4;
   
    public String bankLineN5;
   
    public String bankLineN6;
   
    public String bankLine1;
   
    public String bankLine2;
   
    public String bankLine3;
   
    public String bankLine4;
   
    public String bankLine5;
   
    public String bankLine6;
   
    private String paqTotal;
   
    private String otrosTotal;
   
    private String telTotal;
   
    private String intTotal;
   
    private String vidTotal;
   
    private String veoTotal;
   
    private String bonTotal;
   
    private String saldoMesAnterior;
   
    private String saldoTotalCta;
   
    private String edoDate;
   
    private String edoDueDate;
   
    private String paqName;
   
    private boolean paperless=false;
   
    private String barcode="";
   
    private String extra1=null;
   
    private String extra2=null;
   
    private String ttAccount=null;

   
    private boolean esMigrado=false;

   
    private boolean esNegocios=false;

   
    private String mesFactura;

   
    private String fechaLimit;

   
    private String parentType="";

   
    private String parentAccount="";

    private List<Cuentas> cuentasAsociadas;

    public boolean isEsMigrado() {
        return esMigrado;
    }

    public void setEsMigrado(boolean esMigrado) {
        this.esMigrado = esMigrado;
    }

    public String getMesFactura() {
        return mesFactura;
    }

    public void setMesFactura(String mesFactura) {
        this.mesFactura = mesFactura;
    }

    public String getFechaLimit() {
        return fechaLimit;
    }

    public void setFechaLimit(String fechaLimit) {
        this.fechaLimit = fechaLimit;
    }

    public String getTtAccount() {
        return ttAccount;
    }

    public void setTtAccount(String ttAccount) {
        this.ttAccount = ttAccount;
    }

    public User() {
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

    public boolean isEsNegocios() {
        return esNegocios;
    }

    public void setEsNegocios(boolean esNegocios) {
        this.esNegocios = esNegocios;
    }

    public List<Cuentas> getCuentasAsociadas() {
        return cuentasAsociadas;
    }

    public void setCuentasAsociadas(List<Cuentas> cuentasAsociadas) {
        this.cuentasAsociadas = cuentasAsociadas;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
    }
}
