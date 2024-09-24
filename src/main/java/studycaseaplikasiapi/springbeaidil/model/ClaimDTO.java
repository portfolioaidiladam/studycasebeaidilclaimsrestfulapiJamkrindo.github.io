package studycaseaplikasiapi.springbeaidil.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDTO {
    //data transfer object
    private String lob;
    @Size(max = 200)
    private String penyebabKlaim;
    @Size(max = 200)
    private Integer jumlahNasabah;
    @Size(max = 200)
    private BigDecimal bebanKlaim;
}
