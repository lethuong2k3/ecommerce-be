package net.fpoly.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "token")
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_logged_out", nullable = false)
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
}
