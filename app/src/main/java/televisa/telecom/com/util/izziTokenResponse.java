package televisa.telecom.com.util;

import java.util.List;

/**
 * Created by cevelez on 22/06/2015.
 */
public class izziTokenResponse {
    private String izziError="";
    private String izziErrorCode="";
    private MobileTokenResponse response;
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
