package org.example.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents a manager_profile entity.
 *
 * @author Natalie Werch
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manager_profile")
public class ManagerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    public ManagerProfile(String login, String password, Long managerId) {
        this.login = login;
        this.password = password;
        if (managerId != null) {
            this.manager = new Manager();
            this.manager.setId(managerId);
        }
    }
}
