package studycaseaplikasiapi.springbeaidil.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studycaseaplikasiapi.springbeaidil.entity.aplikasi.Claim;
import studycaseaplikasiapi.springbeaidil.model.ClaimDTO;
import studycaseaplikasiapi.springbeaidil.repository.aplikasi.ClaimRepository;
import studycaseaplikasiapi.springbeaidil.repository.penampungan.ClaimSummaryRepository;
import studycaseaplikasiapi.springbeaidil.service.ClaimService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
@Slf4j
public class ClaimController {
    @Autowired
    private final ClaimService claimService;
    @Autowired
    private final ClaimRepository claimRepository;
    @Autowired
    private final ClaimSummaryRepository claimSummaryRepository;

    @GetMapping
    public ResponseEntity<List<Claim>> getAllClaims() {
        List<Claim> claims = claimService.getAllClaims();
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/kur-and-pen")
    public ResponseEntity<List<Claim>> getKURAndPENClaims() {
        List<Claim> claims = claimService.getKURAndPENClaims();
        return ResponseEntity.ok(claims);
    }

    @PostMapping("/transfer-to-penampungan")
    public ResponseEntity<String> transferToPenampungan() {
        log.info("Menerima permintaan untuk mentransfer klaim ke penampungan");
        try {
            claimService.transferKURAndPENClaimsToPenampungan();
            log.info("Berhasil mentransfer klaim ke penampungan");
            return ResponseEntity.ok("Data berhasil ditransfer");
        } catch (Exception e) {
            log.error("Error saat mentransfer klaim ke penampungan", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saat mentransfer data: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    public ResponseEntity<String> importClaims(@RequestParam("file") MultipartFile file) {
        try {
            claimService.importClaimsFromExcel(file);
            return ResponseEntity.ok("Data imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing data: " + e.getMessage());
        }
    }

    @GetMapping("/verify-import")
    public ResponseEntity<String> verifyTransfer() {
        long countInPenampungan = claimSummaryRepository.count();
        return ResponseEntity.ok("Total claims in penampungan: " + countInPenampungan);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<ClaimDTO>> getClaimSummary() {
        List<ClaimDTO> summary = claimService.getClaimSummaryByLOB();
        return ResponseEntity.ok(summary);
    }

}