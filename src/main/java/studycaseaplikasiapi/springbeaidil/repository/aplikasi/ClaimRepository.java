package studycaseaplikasiapi.springbeaidil.repository.aplikasi;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studycaseaplikasiapi.springbeaidil.entity.aplikasi.Claim;

import java.util.List;


public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByLobIn(List<String> lobs);
}