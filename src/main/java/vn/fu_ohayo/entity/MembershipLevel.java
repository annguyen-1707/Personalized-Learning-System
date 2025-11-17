package vn.fu_ohayo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name ="membership_level")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level_name", nullable = false, unique = true)
    vn.fu_ohayo.enums.MembershipLevel name;

    @Column(name = "duration", nullable = true)
    Integer durationInDays;

    private double price;

}
