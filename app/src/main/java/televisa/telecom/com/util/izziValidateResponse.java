package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by cevelez on 30/03/2016.
 */
public class izziValidateResponse implements Serializable{
    @Expose
    protected String izziError="";
    @Expose
    protected String izziErrorCode="";
    @Expose
    private MobileValidateResponse response;
    @Expose
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

    public MobileValidateResponse getResponse() {
        return response;
    }

    public void setResponse(MobileValidateResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class MobileValidateResponse implements Serializable{
        @Expose
        private String code="";
        @Expose
        private String smsEnabled="";
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSmsEnabled() {
            return smsEnabled;
        }

        public void setSmsEnabled(String smsEnabled) {
            this.smsEnabled = smsEnabled;
        }


    }
}
