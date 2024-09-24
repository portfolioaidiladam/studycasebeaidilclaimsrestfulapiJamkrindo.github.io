package studycaseaplikasiapi.springbeaidil.entity.penampungan;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "activity_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // activity_type
    @Column(name = "activity_type", nullable = false)
    private String activityType;
    private String description;
    private LocalDateTime timestamp;
}