package com.hdfclife.desk.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Policy {

    @NotBlank
    private String policyNo;

    @NotBlank
    private String customer;

    @NotBlank
    @Pattern(regexp = "TERM|ULIP|ENDOWMENT")
    private String type;

    @Min(1)
    private int basePremium;

    @NotBlank
    @Pattern(regexp = "Active|Lapsed|Pending")
    private String status;

    public Policy() {
    }

    public Policy(String policyNo, String customer, String type, int basePremium, String status) {
        this.policyNo = policyNo;
        this.customer = customer;
        this.type = type;
        this.basePremium = basePremium;
        this.status = status;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBasePremium() {
        return basePremium;
    }

    public void setBasePremium(int basePremium) {
        this.basePremium = basePremium;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
