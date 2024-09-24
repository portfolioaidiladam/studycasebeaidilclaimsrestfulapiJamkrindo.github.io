package studycaseaplikasiapi.springbeaidil.repository.penampungan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studycaseaplikasiapi.springbeaidil.entity.penampungan.ActivityLog;

@Repository("activityLogRepository")
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}