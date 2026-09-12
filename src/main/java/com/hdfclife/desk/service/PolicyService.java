package com.hdfclife.desk.service;

import com.hdfclife.desk.exception.DuplicatePolicyException;
import com.hdfclife.desk.exception.PolicyNotFoundException;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.store.PolicyStore;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PolicyService {

    private final PolicyStore policyStore;

    public PolicyService(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    public List<Policy> findAll() {
        return policyStore.findAll();
    }

    public Policy findByPolicyNo(String policyNo) {
        return policyStore.findByPolicyNo(policyNo)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNo));
    }

    public List<Policy> findByFilters(String status, String type) {
        List<Policy> all = policyStore.findAll();
        return all.stream()
                .filter(p -> status == null || p.getStatus().equals(status))
                .filter(p -> type == null || p.getType().equals(type))
                .toList();
    }

    public Policy create(Policy policy) {
        if (policyStore.exists(policy.getPolicyNo())) {
            throw new DuplicatePolicyException("Policy already exists: " + policy.getPolicyNo());
        }
        policyStore.add(policy);
        return policy;
    }

    public Policy replace(String policyNo, Policy update) {
        if (!policyStore.exists(policyNo)) {
            throw new PolicyNotFoundException("Policy not found: " + policyNo);
        }
        Policy updated = new Policy(policyNo, update.getCustomer(), update.getType(),
                update.getBasePremium(), update.getStatus());
        policyStore.update(updated);
        return updated;
    }

    public void delete(String policyNo) {
        if (!policyStore.exists(policyNo)) {
            throw new PolicyNotFoundException("Policy not found: " + policyNo);
        }
        policyStore.delete(policyNo);
    }

    public long activeCount() {
        return policyStore.findAll().stream()
                .filter(p -> "Active".equals(p.getStatus()))
                .count();
    }

    public long termCount() {
        return policyStore.findAll().stream()
                .filter(p -> "TERM".equals(p.getType()))
                .count();
    }

    public long uniqueCustomerCount() {
        Set<String> names = new HashSet<>();
        for (Policy p : policyStore.findAll()) {
            names.add(p.getCustomer());
        }
        return names.size();
    }
}
