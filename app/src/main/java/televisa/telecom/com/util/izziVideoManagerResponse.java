package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.util.List;

import televisa.telecom.com.model.EquiposVideo;
import televisa.telecom.com.model.Usuario;

/**
 * Created by carlos on 11/09/17.
 */

public class izziVideoManagerResponse {
    @Expose
    private String izziError="";
    @Expose
    private String izziErrorCode="";

    @Expose
    private AssetVideoResponse response;

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

    public AssetVideoResponse getResponse() {
        return response;
    }

    public void setResponse(AssetVideoResponse response) {
        this.response = response;
    }

    public class AssetVideoResponse {
        @Expose
        private List<EquiposVideo> equipos;

        public List<EquiposVideo> getEquipos() {
            return equipos;
        }

        public void setEquipos(List<EquiposVideo> equipos) {
            this.equipos = equipos;
        }
    }
}
