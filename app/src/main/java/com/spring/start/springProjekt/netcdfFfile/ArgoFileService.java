package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public interface ArgoFileService {

    ArgoFileSnapshot addFile(File file, String keyName);

    void deleteFile(Long id);

    boolean validateFile(MultipartFile mFile, File file) throws IOException;

    File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException;

    ArgoFileDataBaseEventSnapshot saveFileEvent(ArgoFileEvent argoFileEvent);

    ArgoFileDataBaseEventSnapshot findByOccurredOn(Instant occurredOn);
}
