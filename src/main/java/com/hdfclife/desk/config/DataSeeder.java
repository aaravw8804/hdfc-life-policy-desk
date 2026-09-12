package com.hdfclife.desk.config;

import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.PolicyService;
import com.hdfclife.desk.store.PolicyStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PolicyStore policyStore;
    private final PolicyService policyService;
    private final HdfcProperties hdfcProperties;
    private final Environment environment;

    public DataSeeder(PolicyStore policyStore, PolicyService policyService,
                       HdfcProperties hdfcProperties, Environment environment) {
        this.policyStore = policyStore;
        this.policyService = policyService;
        this.hdfcProperties = hdfcProperties;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        if (policyStore.count() == 0) {
            policyStore.add(new Policy("HDFC-LIFE-1001", "Anita Sharma", "TERM", 18500, "Active"));
            policyStore.add(new Policy("HDFC-LIFE-1002", "Rahul Mehta", "ULIP", 42000, "Active"));
            policyStore.add(new Policy("HDFC-LIFE-1003", "Priya Nair", "ENDOWMENT", 27000, "Lapsed"));
            policyStore.add(new Policy("HDFC-LIFE-1004", "Vikram Singh", "TERM", 15200, "Active"));
            policyStore.add(new Policy("HDFC-LIFE-1005", "Sneha Patel", "ULIP", 36000, "Active"));
            policyStore.add(new Policy("HDFC-LIFE-1006", "Anita Sharma", "ENDOWMENT", 22000, "Pending"));
        }

        String[] activeProfiles = environment.getActiveProfiles();
        String profile = activeProfiles.length > 0 ? activeProfiles[0] : "default";

        System.out.println("Active profile -> " + profile);
        System.out.println("Company name -> " + hdfcProperties.getCompanyName());
        System.out.println("Max claim amount -> " + hdfcProperties.getMaxClaimAmount());
        System.out.println("Seeded policy count -> " + policyStore.count());
        System.out.println("Lookup HDFC-LIFE-1004 customer -> "
                + policyStore.findByPolicyNo("HDFC-LIFE-1004").map(Policy::getCustomer).orElse("N/A"));
        System.out.println("Active policy count -> " + policyService.activeCount());
        System.out.println("TERM policy count -> " + policyService.termCount());
        System.out.println("Unique customer count -> " + policyService.uniqueCustomerCount());
        System.out.println("Injected PolicyStore class -> " + policyStore.getClass().getSimpleName());
    }
}
