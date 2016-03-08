package izzi.connect.controls;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Carlos on 08/11/15.
 */
public class IzziConnectResponse implements Serializable {
    public String expires_in="";
    public String token_type="";
    public String access_token="";
    public String refresh_token="";
    public String error="";
    public String error_description="";
    public String uid="";
    public String Region="";
    public String Paquetes="";
    public String lastname="";
    public String firstname="";
    public String uri="";
    public String Status="";
    public String SubscriberID="";

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getPaquetes() {
        return Paquetes;
    }

    public void setPaquetes(String paquetes) {
        Paquetes = paquetes;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSubscriberID() {
        return SubscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        SubscriberID = subscriberID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getExpires_in() {

        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

}
