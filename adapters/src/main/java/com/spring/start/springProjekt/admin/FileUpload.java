package com.spring.start.springProjekt.admin;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
class FileUpload {

    private MultipartFile filename;

}

