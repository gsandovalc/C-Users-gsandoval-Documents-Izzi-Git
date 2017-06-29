package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

/**
 * Created by cevelez on 31/03/17.
 */

public class MobileStatusResponse {
    @Expose
    protected String izziError="";
    @Expose
    protected String izziErrorCode="";
    @Expose
    private HelperClass response;
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
    public HelperClass getResponse() {
        return response;
    }
    public void setResponse(HelperClass response) {
        this.response = response;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public class HelperClass{
        @Expose
        private String response ="";
        @Expose
        private String extras="";

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getExtras() {
            return extras;
        }

        public void setExtras(String extras) {
            this.extras = extras;
        }
    }
}
