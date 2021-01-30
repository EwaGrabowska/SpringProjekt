package com.spring.start.springProjekt.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "YOUR_ENTITY_SEQ_Role")
    @SequenceGenerator(name = "YOUR_ENTITY_SEQ_Role", sequenceName = "YOUR_ENTITY_SEQ_Role", allocationSize = 1)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role")
    @NotNull
    private String role;

}
