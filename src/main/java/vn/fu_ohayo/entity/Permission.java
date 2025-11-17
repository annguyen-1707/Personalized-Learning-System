package vn.fu_ohayo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fu_ohayo.enums.PermissionEnum;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Permissions")
public class Permission {
    @Id @GeneratedValue(
            strategy = jakarta.persistence.GenerationType.IDENTITY
    )
    @Column(name = "permission_id")
    private int permissionId;

    @Enumerated(EnumType.STRING)
    private PermissionEnum name;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
