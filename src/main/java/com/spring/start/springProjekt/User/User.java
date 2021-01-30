package com.spring.start.springProjekt.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "YOUR_ENTITY_SEQ_User")
    @SequenceGenerator(name = "YOUR_ENTITY_SEQ_User", sequenceName = "YOUR_ENTITY_SEQ_User", allocationSize = 1)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "email")
    @NotNull
    @NotEmpty(message = "Pole nie może być puste")
    private String email;

    @Column(name = "password")
    @NotNull
    @Length(min = 5, message = "minimalna długość hasła to 5 znaków")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "active")
    private int active;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "activation_code")
    private String activationCode;

    @Transient
    private int nrRoli;

    @Transient
    private String newPassword;

    @Transient
    private String passwordConfirm;

}
