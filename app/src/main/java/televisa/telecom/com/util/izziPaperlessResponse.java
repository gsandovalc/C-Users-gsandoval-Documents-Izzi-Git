package televisa.telecom.com.util;

import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 05/05/2015.
 */
public class izziPaperlessResponse {

        private String izziError="";
        private String izziErrorCode="";
        private Paperless response;
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
        private String paperlessResponse="";

        public String getPaperlessResponse() {
            return paperlessResponse;
        }

        public void setPaperlessResponse(String paperlessResponse) {
            this.paperlessResponse = paperlessResponse;
        }
    }
}
