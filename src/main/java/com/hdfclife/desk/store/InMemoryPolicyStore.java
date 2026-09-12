package com.hdfclife.desk.store;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class InMemoryPolicyStore implements PolicyStore {

    private final Map<String, Policy> policies = new LinkedHashMap<>();
    private final List<Claim> claims = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public List<Policy> findAll() {
        lock.lock();
        try {
            return new ArrayList<>(policies.values());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<Policy> findByPolicyNo(String policyNo) {
        lock.lock();
        try {
            return Optional.ofNullable(policies.get(policyNo));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(Policy policy) {
        lock.lock();
        try {
            policies.put(policy.getPolicyNo(), policy);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(Policy policy) {
        lock.lock();
        try {
            policies.put(policy.getPolicyNo(), policy);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void delete(String policyNo) {
        lock.lock();
        try {
            policies.remove(policyNo);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean exists(String policyNo) {
        lock.lock();
        try {
            return policies.containsKey(policyNo);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long count() {
        lock.lock();
        try {
            return policies.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Claim> findClaimsByPolicyNo(String policyNo) {
        lock.lock();
        try {
            List<Claim> result = new ArrayList<>();
            for (Claim claim : claims) {
                if (claim.getPolicyNo().equals(policyNo)) {
                    result.add(claim);
                }
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addClaim(Claim claim) {
        lock.lock();
        try {
            claims.add(claim);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<Claim> findClaimByClaimNo(String claimNo) {
        lock.lock();
        try {
            return claims.stream()
                    .filter(c -> c.getClaimNo().equals(claimNo))
                    .findFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long claimCount() {
        lock.lock();
        try {
            return claims.size();
        } finally {
            lock.unlock();
        }
    }
}
