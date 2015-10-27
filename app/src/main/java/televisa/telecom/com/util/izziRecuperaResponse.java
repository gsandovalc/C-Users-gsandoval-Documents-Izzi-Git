package televisa.telecom.com.util;

/**
 * Created by cevelez on 20/07/2015.
 */
public class izziRecuperaResponse {

    protected String izziError="";
    protected String izziErrorCode="";
    private MobileRecuperarResponse response;
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

    public MobileRecuperarResponse getResponse() {
        return response;
    }

    public void setResponse(MobileRecuperarResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class MobileRecuperarResponse{
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }


    }
}
