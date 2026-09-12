package com.hdfclife.desk.web;

import com.hdfclife.desk.model.Claim;
import com.hdfclife.desk.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claims", description = "HDFC Life claim filing")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @Operation(summary = "File a claim against a policy")
    @ApiResponse(responseCode = "201", description = "Claim filed")
    @ApiResponse(responseCode = "400", description = "Invalid claim amount")
    @ApiResponse(responseCode = "404", description = "Policy not found")
    public ResponseEntity<Claim> fileClaim(@Valid @RequestBody Claim request) {
        Claim created = claimService.fileClaim(request.getPolicyNo(), request.getClaimAmount(), request.getUrgency());
        return ResponseEntity.created(URI.create("/api/claims/" + created.getClaimNo())).body(created);
    }

    @GetMapping("/{claimNo}")
    @Operation(summary = "Get one claim by claim number")
    @ApiResponse(responseCode = "200", description = "Claim found")
    @ApiResponse(responseCode = "404", description = "Claim not found")
    public ResponseEntity<Claim> findOne(@PathVariable String claimNo) {
        return ResponseEntity.ok(claimService.findByClaimNo(claimNo));
    }
}
