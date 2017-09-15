package televisa.telecom.com.model;

import com.google.gson.annotations.Expose;

/**
 * Created by carlos on 11/09/17.
 */

public class EquiposVideo {
    @Expose
    private String serialNumber;
    @Expose
    private String serviceType;
    @Expose
    private String alias;
    @Expose
    private String partNumber;
    @Expose
    private String productPartNumber;
    @Expose
    private String family;
    @Expose
    private String model;
    @Expose
    private String hubRamalNodo;
    @Expose
    private String type;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getProductPartNumber() {
        return productPartNumber;
    }

    public void setProductPartNumber(String productPartNumber) {
        this.productPartNumber = productPartNumber;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHubRamalNodo() {
        return hubRamalNodo;
    }

    public void setHubRamalNodo(String hubRamalNodo) {
        this.hubRamalNodo = hubRamalNodo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
