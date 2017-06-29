package televisa.telecom.com.util;

import com.google.gson.annotations.Expose;

import java.util.List;

import televisa.telecom.com.model.Tokens;

/**
 * Created by cevelez on 22/06/2015.
 */
public class MobileTokenResponse {
    @Expose
    private List<Tokens> pidt;

    public List<Tokens> getPidt() {
        return pidt;
    }

    public void setPidt(List<Tokens> pidt) {
        this.pidt = pidt;
    }
}
