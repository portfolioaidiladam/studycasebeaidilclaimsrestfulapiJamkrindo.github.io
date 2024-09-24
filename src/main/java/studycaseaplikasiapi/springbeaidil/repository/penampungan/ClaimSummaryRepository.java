package studycaseaplikasiapi.springbeaidil.repository.penampungan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studycaseaplikasiapi.springbeaidil.entity.penampungan.ClaimSummary;

@Repository("claimSummaryRepository")
public interface ClaimSummaryRepository extends JpaRepository<ClaimSummary, Long> {
}