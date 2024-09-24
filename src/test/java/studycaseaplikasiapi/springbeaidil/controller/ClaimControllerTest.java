package studycaseaplikasiapi.springbeaidil.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import studycaseaplikasiapi.springbeaidil.entity.aplikasi.Claim;
import studycaseaplikasiapi.springbeaidil.repository.aplikasi.ClaimRepository;
import studycaseaplikasiapi.springbeaidil.repository.penampungan.ClaimSummaryRepository;
import studycaseaplikasiapi.springbeaidil.service.ClaimService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@Slf4j

class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaimService claimService;
    @Mock
    private ClaimRepository claimRepository;
    @Mock
    private ClaimSummaryRepository claimSummaryRepository;

    @InjectMocks
    private ClaimController claimController;

    @Test
    void testGetAllClaims() throws Exception {
        Claim claim1 = new Claim();
        claim1.setLob("KUR");
        claim1.setPenyebabKlaim("Macet");
        claim1.setJumlahNasabah(100);
        claim1.setBebanKlaim(new BigDecimal("1000000.00"));

        Claim claim2 = new Claim();
        claim2.setLob("PEN");
        claim2.setPenyebabKlaim("Macet");
        claim2.setJumlahNasabah(50);
        claim2.setBebanKlaim(new BigDecimal("500000.00"));

        List<Claim> claims = Arrays.asList(claim1, claim2);

        when(claimService.getAllClaims()).thenReturn(claims);

        mockMvc.perform(get("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lob").value("KUR"))
                .andExpect(jsonPath("$[1].lob").value("PEN"));
    }

    @Test
    void testGetKURAndPENClaims() throws Exception {
        Claim kurClaim = new Claim();
        kurClaim.setLob("KUR");
        kurClaim.setPenyebabKlaim("Macet");
        kurClaim.setJumlahNasabah(100);
        kurClaim.setBebanKlaim(new BigDecimal("1000000.00"));

        Claim penClaim = new Claim();
        penClaim.setLob("PEN");
        penClaim.setPenyebabKlaim("Macet");
        penClaim.setJumlahNasabah(50);
        penClaim.setBebanKlaim(new BigDecimal("500000.00"));

        List<Claim> claims = Arrays.asList(kurClaim, penClaim);

        when(claimService.getKURAndPENClaims()).thenReturn(claims);

        mockMvc.perform(get("/api/claims/kur-and-pen")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lob").value("KUR"))
                .andExpect(jsonPath("$[1].lob").value("PEN"));
    }

    @Test
    void testTransferToPenampungan() throws Exception {
        doNothing().when(claimService).transferKURAndPENClaimsToPenampungan();

        mockMvc.perform(post("/api/claims/transfer-to-penampungan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Data transferred successfully"));

        verify(claimService, times(1)).transferKURAndPENClaimsToPenampungan();
    }

    @Test
    void testImportClaims() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[0]
        );

        doNothing().when(claimService).importClaimsFromExcel(any(MultipartFile.class));

        mockMvc.perform(multipart("/api/claims/import").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Data imported successfully"));

        verify(claimService, times(1)).importClaimsFromExcel(any(MultipartFile.class));
    }
    @Test
    void testVerifyTransfer() {
        when(claimSummaryRepository.count()).thenReturn(5L);

        ResponseEntity<String> response = claimController.verifyTransfer();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Total claims in penampungan: 5", response.getBody());
    }

}