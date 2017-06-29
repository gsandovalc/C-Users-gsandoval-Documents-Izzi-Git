package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by cevelez on 22/06/2015.
 */
public class izziTokenResponse {
    @Expose
    private String izziError="";
    @Expose
    private String izziErrorCode="";
    @Expose
    private MobileTokenResponse response;
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

    public MobileTokenResponse getResponse() {
        return response;
    }

    public void setResponse(MobileTokenResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
