package com.spring.start.springProjekt.Admin;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUpload {

    private MultipartFile filename;

}

