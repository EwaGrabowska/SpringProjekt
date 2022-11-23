package com.spring.start.springProjekt.user.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Set;


@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    static public Builder builder(){
        return new Builder();
    };
    private UserDTO(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.active = builder.active;
        this.roles = builder.roles;
        this.password = builder.password;
        this.activationCode = builder.activationCode;
    }
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private int active;
    private Set<RoleDTO> roles;
    private String activationCode;

    private int roleNumber;

    @Transient
    private String newPassword;

    @Transient
    private String passwordConfirm;

    @Transient
    private String oldPasswordCheck;

        public Builder toBuilder(){
        return new Builder().withId(this.id)
                .withName(this.name)
                .withLastName(this.lastName)
                .withEmail(this.email)
                .withActive(this.active)
                .withRoles(this.roles)
                .withPassword(this.password)
                .withActivationCode(this.activationCode);

    }


    public static class Builder{
        private Integer id;
        private String name;
        private String lastName;
        private String email;
        private int active;
        private Set<RoleDTO> roles;
        private String password;
        private String activationCode;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }
        public Builder withName(String name) {
            this.name = name;
            return this;
        }
        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }
        public Builder withActive(int active) {
            this.active = active;
            return this;
        }
        public Builder withRoles(Set<RoleDTO> roles) {
            this.roles = roles;
            return this;
        }
        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }
        public Builder withActivationCode(String activationCode) {
            this.activationCode = activationCode;
            return this;
        }
        public UserDTO build(){
            return new UserDTO(this);
        }


    }
}
