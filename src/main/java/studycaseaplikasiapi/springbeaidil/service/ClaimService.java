package studycaseaplikasiapi.springbeaidil.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studycaseaplikasiapi.springbeaidil.entity.penampungan.ActivityLog;
import studycaseaplikasiapi.springbeaidil.entity.aplikasi.Claim;
import studycaseaplikasiapi.springbeaidil.entity.penampungan.ClaimSummary;
import studycaseaplikasiapi.springbeaidil.model.ClaimDTO;
import studycaseaplikasiapi.springbeaidil.repository.penampungan.ActivityLogRepository;
import studycaseaplikasiapi.springbeaidil.repository.aplikasi.ClaimRepository;
import studycaseaplikasiapi.springbeaidil.repository.penampungan.ClaimSummaryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClaimService {
    @Autowired
    private final ClaimRepository claimRepository;
    @Autowired
    private final ClaimSummaryRepository claimSummaryRepository;
    @Autowired
    private final ActivityLogRepository activityLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public List<Claim> getKURAndPENClaims() {
        return claimRepository.findByLobIn(Arrays.asList("KUR", "PEN"));
    }
    @Transactional
    public void transferKURAndPENClaimsToPenampungan() {
        List<Claim> claims = getKURAndPENClaims();
        for (Claim claim : claims) {
            ClaimSummary summary = new ClaimSummary();
            summary.setLob(claim.getLob());
            summary.setPenyebabKlaim(claim.getPenyebabKlaim());
            /*summary.setPeriode(LocalDate.now().atStartOfDay())*/;
            summary.setPeriode(LocalDateTime.now());
            summary.setBebanKlaim(claim.getBebanKlaim());
            claimSummaryRepository.save(summary);
        }

        ActivityLog log = new ActivityLog();
        log.setActivityType("Data Transfer");
        log.setDescription("Transferred " + claims.size() + " KUR and PEN claims to penampungan database");
        log.setTimestamp(LocalDateTime.now());
        activityLogRepository.save(log);

        logger.info("Transferred {} KUR and PEN claims to penampungan database", claims.size());
    }

    @Transactional
    public void importClaimsFromExcel(MultipartFile file) throws IOException {
        logger.info("Starting import process for file: {}", file.getOriginalFilename());
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int importedCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                try {
                    Claim claim = new Claim();
                    claim.setLob(row.getCell(0).getStringCellValue());
                    claim.setPenyebabKlaim(row.getCell(1).getStringCellValue());
                    claim.setJumlahNasabah((int) row.getCell(2).getNumericCellValue());
                    claim.setBebanKlaim(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));

                    claimRepository.save(claim);
                    importedCount++;
                } catch (Exception e) {
                    logger.error("Error processing row {}: {}", row.getRowNum(), e.getMessage());
                }
            }

            workbook.close();

            logger.info("Import completed. {} claims imported successfully.", importedCount);
        } catch (Exception e) {
            logger.error("Error during import process: {}", e.getMessage());
            throw e;
        }
    }

    public ClaimService(
            @Qualifier("claimRepository") ClaimRepository claimRepository,
            @Qualifier("claimSummaryRepository") ClaimSummaryRepository claimSummaryRepository,
            @Qualifier("activityLogRepository") ActivityLogRepository activityLogRepository
    ) {
        this.claimRepository = claimRepository;
        this.claimSummaryRepository = claimSummaryRepository;
        this.activityLogRepository = activityLogRepository;
    }

    public List<ClaimDTO> getClaimSummaryByLOB() {
        List<Claim> claims = claimRepository.findAll();
        Map<String, List<ClaimDTO>> groupedClaims = new HashMap<>();

        for (Claim claim : claims) {
            ClaimDTO dto = new ClaimDTO(claim.getLob(), claim.getPenyebabKlaim(),
                    claim.getJumlahNasabah(), claim.getBebanKlaim());
            groupedClaims.computeIfAbsent(claim.getLob(), k -> new ArrayList<>()).add(dto);
        }

        List<ClaimDTO> result = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;
        int grandTotalNasabah = 0;

        for (Map.Entry<String, List<ClaimDTO>> entry : groupedClaims.entrySet()) {
            result.addAll(entry.getValue());

            int subtotalNasabah = entry.getValue().stream()
                    .mapToInt(ClaimDTO::getJumlahNasabah)
                    .sum();
            BigDecimal subtotalBeban = entry.getValue().stream()
                    .map(ClaimDTO::getBebanKlaim)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            result.add(new ClaimDTO("Subtotal " + entry.getKey(), "", subtotalNasabah, subtotalBeban));

            grandTotal = grandTotal.add(subtotalBeban);
            grandTotalNasabah += subtotalNasabah;
        }

        result.add(new ClaimDTO("Total", "", grandTotalNasabah, grandTotal));

        return result;
    }
}