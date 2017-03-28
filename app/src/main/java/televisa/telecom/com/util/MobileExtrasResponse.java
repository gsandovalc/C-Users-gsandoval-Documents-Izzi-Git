package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.util.List;

import televisa.telecom.com.model.Notification;

/**
 * Created by cevelez on 27/03/17.
 */

public class MobileExtrasResponse {
    @Expose
    private String izziError="";
    @Expose
    private String izziErrorCode="";
    @Expose
    private HelperClass response;

    public HelperClass getResponse() {
        return response;
    }

    public void setResponse(HelperClass response) {
        this.response = response;
    }

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

    public class HelperClass{
        @Expose
        private String response ="";

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

}
