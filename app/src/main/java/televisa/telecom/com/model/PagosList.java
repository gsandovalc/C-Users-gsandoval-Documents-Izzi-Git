package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;

/**
 * Created by cevelez on 24/04/2015.
 */
public class PagosList  extends Model {
    @Column
    @Expose
    private String pinfldamount;
    @Column
    @Expose
    private String pinfldauthdate;
    @Column
    @Expose
    private String pinflddescr;
    @Column
    @Expose
    private String pinfldreceiptno;
    @Column
    @Expose
    private String pinfldtransid;

    public String getPinfldamount() {
        return pinfldamount;
    }

    public void setPinfldamount(String pinfldamount) {
        this.pinfldamount = pinfldamount;
    }

    public String getPinfldauthdate() {
        return pinfldauthdate;
    }

    public void setPinfldauthdate(String pinfldauthdate) {
        this.pinfldauthdate = pinfldauthdate;
    }

    public String getPinflddescr() {
        return pinflddescr;
    }

    public void setPinflddescr(String pinflddescr) {
        this.pinflddescr = pinflddescr;
    }

    public String getPinfldreceiptno() {
        return pinfldreceiptno;
    }

    public void setPinfldreceiptno(String pinfldreceiptno) {
        this.pinfldreceiptno = pinfldreceiptno;
    }

    public String getPinfldtransid() {
        return pinfldtransid;
    }

    public void setPinfldtransid(String pinfldtransid) {
        this.pinfldtransid = pinfldtransid;
    }
}
