package televisa.telecom.com.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by cevelez on 24/04/2015.
 */
public class PagosList  extends Model {
    @Column
    private String pinfldamount;
    @Column
    private String pinfldauthdate;
    @Column
    private String pinflddescr;
    @Column
    private String pinfldreceiptno;
    @Column
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
