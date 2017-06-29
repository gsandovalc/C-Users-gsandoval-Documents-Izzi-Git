package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

/**
 * Created by cevelez on 05/05/2015.
 */
public class izziPaymentResponse {
    @Expose
    private String izziError="";
    @Expose
    private String izziErrorCode="";
    @Expose
    private MobilePaymentResponse response;
    @Expose
    private String token="";

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

    public MobilePaymentResponse getResponse() {
        return response;
    }

    public void setResponse(MobilePaymentResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class MobilePaymentResponse{
        @Expose
        private String autorizacion="";
        @Expose
        private String referencia="";
        @Expose
        private String paymentError="";


        public String getAutorizacion() {
            return autorizacion;
        }
        public void setAutorizacion(String autorizacion) {
            this.autorizacion = autorizacion;
        }
        public String getReferencia() {
            return referencia;
        }
        public void setReferencia(String referencia) {
            this.referencia = referencia;
        }
        public String getPaymentError() {
            return paymentError;
        }
        public void setPaymentError(String paymentError) {
            this.paymentError = paymentError;
        }



    }

}
