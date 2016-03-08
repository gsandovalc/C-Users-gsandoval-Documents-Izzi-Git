package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by cevelez on 11/05/2015.
 */
public class izziGuideResponse {
    @Expose
    private String izziError="";
    @Expose
    private String izziErrorCode="";
    @Expose
    private MobileGuideResponse response;
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

    public MobileGuideResponse getResponse() {
        return response;
    }

    public void setLineUp(MobileGuideResponse response) {
        this.response = response;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public class MobileGuideResponse {
        @Expose
        private List<TVStation> lineUp;
        @Expose
        private List<String> banners;
        public List<TVStation> getLineUp() {
            return lineUp;
        }

        public void setLineUp(List<TVStation> lineUp) {
            this.lineUp = lineUp;
        }


        public List<String> getBanners() {
            return banners;
        }

        public void setBanners(List<String> banners) {
            this.banners = banners;
        }
    }
    public class TVStation {
        @Expose
        private String nombre;
        @Expose
        private String canal;
        @Expose
        private String thumb;
        @Expose
        private String descripcion;
        @Expose
        private List<String> categorias;
        @Expose
        private List<StationRecord> programas;

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getCanal() {
            return canal;
        }
        public void setCanal(String canal) {
            this.canal = canal;
        }
        public String getThumb() {
            return thumb;
        }
        public void setThumb(String thumb) {
            this.thumb = thumb;
        }
        public String getDescripcion() {
            return descripcion;
        }
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        public List<String> getCategorias() {
            return categorias;
        }
        public void setCategorias(List<String> categorias) {
            this.categorias = categorias;
        }
        public List<StationRecord> getProgramas() {
            return programas;
        }
        public void setProgramas(List<StationRecord> programas) {
            this.programas = programas;
        }
    }
    public class StationRecord {
        @Expose
        private String titulo;
        @Expose
        private String descripcion;
        @Expose
        private String portada;
        @Expose
        private String  pais;
        @Expose
        private String actores;
        @Expose
        private ScheduleRecord horario;
        @Expose
        private String duracion;
        @Expose
        private  List<String> idiomas;
        @Expose
        private List<String> categorias;
        public String getTitulo() {
            return titulo;
        }
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }
        public String getDescripcion() {
            return descripcion;
        }
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        public String getPortada() {
            return portada;
        }
        public void setPortada(String portada) {
            this.portada = portada;
        }
        public String getPais() {
            return pais;
        }
        public void setPais(String pais) {
            this.pais = pais;
        }
        public String getActores() {
            return actores;
        }
        public void setActores(String actores) {
            this.actores = actores;
        }
        public ScheduleRecord getHorario() {
            return horario;
        }
        public void setHorario(ScheduleRecord horario) {
            this.horario = horario;
        }
        public String getDuracion() {
            return duracion;
        }
        public void setDuracion(String duracion) {
            this.duracion = duracion;
        }
        public  List<String> getIdiomas() {
            return idiomas;
        }
        public void setIdiomas( List<String> idiomas) {
            this.idiomas = idiomas;
        }
        public List<String> getCategorias() {
            return categorias;
        }
        public void setCategorias(List<String> categorias) {
            this.categorias = categorias;
        }
    }
    public class ScheduleRecord {
        @Expose
        public String inicial;
        @Expose
        public String end;

        public String getInicial() {
            return inicial;
        }
        public void setInicial(String inicial) {
            this.inicial = inicial;
        }
        public String getEnd() {
            return end;
        }
        public void setEnd(String end) {
            this.end = end;
        }
    }
}
