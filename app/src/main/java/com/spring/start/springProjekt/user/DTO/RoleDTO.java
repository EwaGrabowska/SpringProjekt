package com.spring.start.springProjekt.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role")
    private String role;

    static public Builder builder(){
        return new Builder();
    }
    public RoleDTO(final Builder builder) {
        this.id = builder.id;
        this.role = builder.role;
    }

    public static class Builder{
        private Integer id;
        private String role;

        public Builder withId(Integer id){
            this.id = id;
            return this;
        }
        public Builder withRole(String role){
            this.role = role;
            return this;
        }
        public RoleDTO build(){
            return new RoleDTO(this);
        }
    }
}
