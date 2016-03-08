package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by cevelez on 22/07/2015.
 */
public class izziDondePagarResponse {
    @Expose
    protected String izziError="";
    @Expose
    protected String izziErrorCode="";
    @Expose
    private MobileDondePagarResponse response;
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

    public MobileDondePagarResponse getResponse() {
        return response;
    }

    public void setResponse(MobileDondePagarResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class MobileDondePagarResponse implements MobileResponse {
        @Expose
        private List<String> tiendas;
        @Expose
        private List<Bancos> bancos;

        public List<String> getTiendas() {
            return tiendas;
        }

        public void setTiendas(List<String> tiendas) {
            this.tiendas = tiendas;
        }

        public List<Bancos> getBancos() {
            return bancos;
        }

        public void setBancos(List<Bancos> bancos) {
            this.bancos = bancos;
        }
    }
    public class Bancos {
        @Expose
        private String nombre;
        @Expose
        private String referencia;

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getReferencia() {
            return referencia;
        }
        public void setReferencia(String referencia) {
            this.referencia = referencia;
        }
        public Bancos(String nombre, String referencia) {
            super();
            this.nombre = nombre;
            this.referencia = referencia;
        }

    }
}
