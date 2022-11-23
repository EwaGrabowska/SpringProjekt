package com.spring.start.springProjekt.user.DTO;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.NONE)
public class UserSimpleQueryDTO {

    @Expose
    @XmlElement(name = "id")
    @Column(name = "user_id")
    private Integer id;

    @Expose
    @XmlElement(name = "name")
    @Column(name = "name")
    private String name;

    @Expose
    @XmlElement(name = "lastName")
    @Column(name = "lastName")
    private String lastName;

    @Expose
    @XmlElement(name = "email")
    @Column(name = "email")
    private String email;

    public UserSimpleQueryDTO(final Integer id, final String name, final String lastName, final String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSimpleQueryDTO)) return false;
        final UserSimpleQueryDTO that = (UserSimpleQueryDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, email);
    }
}
