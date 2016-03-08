package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 05/05/2015.
 */
public class izziPaperlessResponse {
    @Expose
        private String izziError="";
    @Expose
    private String izziErrorCode="";
    @Expose
    private Paperless response;
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
        public Paperless getResponse() {
            return response;
        }
        public void setResponse(Paperless response) {
            this.response = response;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }

    public class Paperless{
        @Expose
        private String paperlessResponse="";

        public String getPaperlessResponse() {
            return paperlessResponse;
        }

        public void setPaperlessResponse(String paperlessResponse) {
            this.paperlessResponse = paperlessResponse;
        }
    }
}
