package com.hdfclife.desk.service;

import com.hdfclife.desk.config.HdfcProperties;
import com.hdfclife.desk.exception.ClaimNotFoundException;
import com.hdfclife.desk.exception.InvalidClaimException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Urgency;
import com.hdfclife.desk.store.PolicyStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ClaimService {

    private final PolicyStore policyStore;
    private final HdfcProperties hdfcProperties;
    private final AtomicInteger claimSequence = new AtomicInteger(0);

    public ClaimService(PolicyStore policyStore, HdfcProperties hdfcProperties) {
        this.policyStore = policyStore;
        this.hdfcProperties = hdfcProperties;
    }

    public Claim fileClaim(String policyNo, int claimAmount, Urgency urgency) {
        if (!policyStore.exists(policyNo)) {
            throw new PolicyNotFoundException("Policy not found: " + policyNo);
        }
        if (claimAmount <= 0 || claimAmount > hdfcProperties.getMaxClaimAmount()) {
            throw new InvalidClaimException(
                    "Claim amount must be between 1 and " + hdfcProperties.getMaxClaimAmount());
        }
        String claimNo = nextClaimNo();
        Claim claim = new Claim(claimNo, policyNo, claimAmount, urgency, "SUBMITTED");
        policyStore.addClaim(claim);
        return claim;
    }

    public Claim findByClaimNo(String claimNo) {
        return policyStore.findClaimByClaimNo(claimNo)
                .orElseThrow(() -> new ClaimNotFoundException("Claim not found: " + claimNo));
    }

    public List<Claim> findByPolicyNo(String policyNo) {
        if (!policyStore.exists(policyNo)) {
            throw new PolicyNotFoundException("Policy not found: " + policyNo);
        }
        return policyStore.findClaimsByPolicyNo(policyNo);
    }

    private String nextClaimNo() {
        int next = claimSequence.incrementAndGet();
        return String.format("CLM-%02d", next);
    }
}
