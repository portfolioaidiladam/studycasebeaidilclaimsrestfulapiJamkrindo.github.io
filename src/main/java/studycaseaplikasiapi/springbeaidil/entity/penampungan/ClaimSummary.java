package studycaseaplikasiapi.springbeaidil.entity.penampungan;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "claim_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lob;
    @Column(name = "penyebab_klaim", nullable = false)
    private String penyebabKlaim;
    private LocalDateTime periode;
    @Column(name = "beban_klaim", nullable = false)
    private BigDecimal bebanKlaim;
}
