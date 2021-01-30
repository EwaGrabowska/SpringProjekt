package com.spring.start.springProjekt.User;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "Users")
public class Users {

    private List<User> users = new ArrayList<>();
}
