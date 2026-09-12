package com.hdfclife.desk.store;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;

import java.util.List;
import java.util.Optional;

public interface PolicyStore {

    List<Policy> findAll();

    Optional<Policy> findByPolicyNo(String policyNo);

    void add(Policy policy);

    void update(Policy policy);

    void delete(String policyNo);

    boolean exists(String policyNo);

    long count();

    List<Claim> findClaimsByPolicyNo(String policyNo);

    void addClaim(Claim claim);

    Optional<Claim> findClaimByClaimNo(String claimNo);

    long claimCount();
}
