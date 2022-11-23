package com.spring.start.springProjekt.user.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "Users")
public class UsersSimpleQueryDTO {

    private List<UserSimpleQueryDTO> users = new ArrayList<>();
}
