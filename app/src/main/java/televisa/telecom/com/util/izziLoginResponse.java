package televisa.telecom.com.util;

import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 24/04/2015.
 */
public class izziLoginResponse {
    private String izziError="";
    private String izziErrorCode="";
    private Usuario response;
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
    public Usuario getResponse() {
        return response;
    }
    public void setResponse(Usuario response) {
        this.response = response;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
