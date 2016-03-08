package televisa.telecom.com.model;

import com.google.gson.annotations.Expose;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by cevelez on 22/06/2015.
 */
public class Tokens {
    @Expose
    private String reasonCode;
    @Expose
    private String accountNumber;
    @Expose
    private String requestId;
    @Expose
    private String reconciliationId;
    @Expose
    private String subscriptionId;
    @Expose
    private String requestToken;
    @Expose
    private String createdDate;
    @Expose
    private String cardDigits;
    @Expose
    private String cardType;
    @Expose
    private String cardMonth;
    @Expose
    private String cardYear;
    @Expose
    private String active;

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReconciliationId() {
        return reconciliationId;
    }

    public void setReconciliationId(String reconciliationId) {
        this.reconciliationId = reconciliationId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCardDigits() {
        return cardDigits;
    }

    public void setCardDigits(String cardDigits) {
        this.cardDigits = cardDigits;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(String cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardYear() {
        return cardYear;
    }

    public void setCardYear(String cardYear) {
        this.cardYear = cardYear;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
