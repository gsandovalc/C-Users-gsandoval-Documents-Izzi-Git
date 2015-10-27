package televisa.telecom.com.util;

/**
 * Created by cevelez on 22/07/2015.
 */
public class izziEdoCuentaResponse {
    protected String izziError="";
    protected String izziErrorCode="";
    private MobileEdoCuentaResponse response;
    protected String token="";

    public String getIzziError() {
        return izziError;
    }

    public void setIzziError(String izziError) {
        this.izziError = izziError;
    }

    public String getIzziErrorCode() {
        return izziErrorCode;
    }

    public void setIzziErrorCode(String izziErrorCode) {
        this.izziErrorCode = izziErrorCode;
    }

    public MobileEdoCuentaResponse getResponse() {
        return response;
    }

    public void setResponse(MobileEdoCuentaResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class MobileEdoCuentaResponse implements MobileResponse{
        private boolean extraInt;
        private boolean extraTV;
        private boolean extraTel;
        private boolean extraVeo;
        private boolean extraOtros;
        private boolean extraBonus;

        private String mes;
        private String periodo;
        private String fechaLimite;
        private String saldoAnterior;

        //Cargos del mes
        private String paquete;
        private String totalPaquete;

        private String totalInt="";
        private String totalTV="";
        private String totalTel="";
        private String totalVeo="";
        private String totalOtros="";
        private String totalBonus="";

        private String subTotal;
        private String pagos="0";
        private String pagoTexto;

        private String total;

        private String contrato;
        private String url;


        public boolean isExtraInt() {
            return extraInt;
        }

        public void setExtraInt(boolean extraInt) {
            this.extraInt = extraInt;
        }

        public boolean isExtraTV() {
            return extraTV;
        }

        public void setExtraTV(boolean extraTV) {
            this.extraTV = extraTV;
        }

        public boolean isExtraTel() {
            return extraTel;
        }

        public void setExtraTel(boolean extraTel) {
            this.extraTel = extraTel;
        }

        public boolean isExtraVeo() {
            return extraVeo;
        }

        public void setExtraVeo(boolean extraVeo) {
            this.extraVeo = extraVeo;
        }

        public boolean isExtraOtros() {
            return extraOtros;
        }

        public void setExtraOtros(boolean extraOtros) {
            this.extraOtros = extraOtros;
        }

        public boolean isExtraBonus() {
            return extraBonus;
        }

        public void setExtraBonus(boolean extraBonus) {
            this.extraBonus = extraBonus;
        }

        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }

        public String getPeriodo() {
            return periodo;
        }

        public void setPeriodo(String periodo) {
            this.periodo = periodo;
        }

        public String getFechaLimite() {
            return fechaLimite;
        }

        public void setFechaLimite(String fechaLimite) {
            this.fechaLimite = fechaLimite;
        }

        public String getSaldoAnterior() {
            return saldoAnterior;
        }

        public void setSaldoAnterior(String saldoAnterior) {
            this.saldoAnterior = saldoAnterior;
        }

        public String getPaquete() {
            return paquete;
        }

        public void setPaquete(String paquete) {
            this.paquete = paquete;
        }

        public String getTotalPaquete() {
            return totalPaquete;
        }

        public void setTotalPaquete(String totalPaquete) {
            this.totalPaquete = totalPaquete;
        }

        public String getTotalInt() {
            return totalInt;
        }

        public void setTotalInt(String totalInt) {
            this.totalInt = totalInt;
        }

        public String getTotalTV() {
            return totalTV;
        }

        public void setTotalTV(String totalTV) {
            this.totalTV = totalTV;
        }

        public String getTotalTel() {
            return totalTel;
        }

        public void setTotalTel(String totalTel) {
            this.totalTel = totalTel;
        }

        public String getTotalVeo() {
            return totalVeo;
        }

        public void setTotalVeo(String totalVeo) {
            this.totalVeo = totalVeo;
        }

        public String getTotalOtros() {
            return totalOtros;
        }

        public void setTotalOtros(String totalOtros) {
            this.totalOtros = totalOtros;
        }

        public String getTotalBonus() {
            return totalBonus;
        }

        public void setTotalBonus(String totalBonus) {
            this.totalBonus = totalBonus;
        }

        public String getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(String subTotal) {
            this.subTotal = subTotal;
        }

        public String getPagos() {
            return pagos;
        }

        public void setPagos(String pagos) {
            this.pagos = pagos;
        }

        public String getPagoTexto() {
            return pagoTexto;
        }

        public void setPagoTexto(String pagoTexto) {
            this.pagoTexto = pagoTexto;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getContrato() {
            return contrato;
        }

        public void setContrato(String contrato) {
            this.contrato = contrato;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

}
