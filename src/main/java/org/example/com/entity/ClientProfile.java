package org.example.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * Represents a client profile entity.
 *
 * @author Natalie Werch
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_profile")
public class ClientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public ClientProfile(String login, String password, UUID clientId) {
        this.login = login;
        this.password = password;
        if (clientId != null) {
            this.client = new Client();
            this.client.setId(clientId);
        }
    }
}
