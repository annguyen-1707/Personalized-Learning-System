package vn.fu_ohayo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import vn.fu_ohayo.enums.RoleEnum;

import java.util.Set;

@Entity
@Table(name = "Roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "roleId")
public class Role implements GrantedAuthority {

    @Id @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "role_id")
    private int roleId;

    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Admin> admins;

    @ManyToMany()
    @JoinTable(
            name = "Role_Permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )

    private Set<Permission> permissions;

    @Override
    public String getAuthority() {
        return "ROLE_" + name.name();
    }
}
