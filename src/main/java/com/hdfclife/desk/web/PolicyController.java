package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.model.Policy;
import com.hdfclife.desk.service.ClaimService;
import com.hdfclife.desk.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies", description = "HDFC Life policy management")
public class PolicyController {

    private final PolicyService policyService;
    private final ClaimService claimService;

    public PolicyController(PolicyService policyService, ClaimService claimService) {
        this.policyService = policyService;
        this.claimService = claimService;
    }

    @GetMapping
    @Operation(summary = "List policies, optionally filtered by status and/or type")
    @ApiResponse(responseCode = "200", description = "Policies returned")
    public ResponseEntity<List<Policy>> findAll(
            @Parameter(description = "Filter by exact status (Active, Lapsed, Pending)")
            @RequestParam(required = false) String status,
            @Parameter(description = "Filter by exact type (TERM, ULIP, ENDOWMENT)")
            @RequestParam(required = false) String type) {
        List<Policy> result = policyService.findByFilters(status, type);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{policyNo}")
    @Operation(summary = "Get one policy by policy number")
    @ApiResponse(responseCode = "200", description = "Policy found")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<Policy> findOne(@PathVariable String policyNo) {
        return ResponseEntity.ok(policyService.findByPolicyNo(policyNo));
    }

    @PostMapping
    @Operation(summary = "Create a new policy")
    @ApiResponse(responseCode = "201", description = "Policy created")
    @ApiResponse(responseCode = "409", description = "Policy number already exists")
    public ResponseEntity<Policy> create(@Valid @RequestBody Policy policy) {
        Policy created = policyService.create(policy);
        return ResponseEntity.created(URI.create("/api/policies/" + created.getPolicyNo())).body(created);
    }

    @PutMapping("/{policyNo}")
    @Operation(summary = "Replace an existing policy's details")
    @ApiResponse(responseCode = "200", description = "Policy updated")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<Policy> replace(@PathVariable String policyNo, @Valid @RequestBody Policy policy) {
        Policy updated = policyService.replace(policyNo, policy);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{policyNo}")
    @Operation(summary = "Delete a policy")
    @ApiResponse(responseCode = "204", description = "Policy deleted")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<Void> delete(@PathVariable String policyNo) {
        policyService.delete(policyNo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{policyNo}/claims")
    @Operation(summary = "List claims filed against a policy, oldest first")
    @ApiResponse(responseCode = "200", description = "Claims returned")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<List<Claim>> findClaims(@PathVariable String policyNo) {
        return ResponseEntity.ok(claimService.findByPolicyNo(policyNo));
    }
}
