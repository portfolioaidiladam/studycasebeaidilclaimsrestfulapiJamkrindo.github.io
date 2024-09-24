package studycaseaplikasiapi.springbeaidil.entity.aplikasi;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lob;

    @Column(name = "penyebab_klaim", nullable = false)
    private String penyebabKlaim;

    @Column(name = "jumlah_nasabah", nullable = false)
    private Integer jumlahNasabah;

    @Column(name = "beban_klaim", nullable = false, precision = 20, scale = 2)
    private BigDecimal bebanKlaim;
}