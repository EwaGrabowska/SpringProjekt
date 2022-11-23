package com.spring.start.springProjekt.netcdfFfile;

import com.spring.start.springProjekt.DomainEventPublisher;
import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Transactional
class ArgoFileServiceImp implements ArgoFileService {

    private static final Logger LOG = LoggerFactory.getLogger(ArgoFileServiceImp.class);
    private final ArgoFileRepository argoFileRepository;
    private NetcdfReader netcdfReader;
    private final ArgoFileDataBaseEventRepository argoFileDataBaseEventRepository;

    ArgoFileServiceImp(ArgoFileRepository argoFileRepository, final ArgoFileDataBaseEventRepository argoFileDataBaseEventRepository, final DomainEventPublisher publisher) {
        this.argoFileRepository = argoFileRepository;
        this.argoFileDataBaseEventRepository = argoFileDataBaseEventRepository;
    }

    @Override
    public ArgoFileSnapshot addFile(File file, String keyName) {
        ArgoFile argoFile = null;
        try {
            argoFile = netcdfReader.setArgoFileAtribute(file);
            argoFile.addKeyName(keyName);

            LOG.debug("[INVOKED >>> ArgoFileServiceImp.addFile > keyName: " + keyName + ", file has been successfully added");
        } catch (IOException e) {
            e.printStackTrace();

            LOG.error("[INVOKED >>> ArgoFileServiceImp.addFile > keyName: " + keyName + ", file has not been successfully added");
        }
        return argoFileRepository.save(argoFile).getSnapshot();
    }

    @Override
    public boolean validateFile(MultipartFile mFile, File file) throws IOException {
        var isArgoFile = true;
        String fileExtension;
        var fileName = mFile.getOriginalFilename();
        assert fileName != null;
        fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equals("nc")) {
            LOG.error("[INVOKED >>> ArgoFileServiceImp.validateFile(), it is not netcdf file");
            isArgoFile = false;
        } else {
            String title = netcdfReader.readTitle(file);
            if (!title.toLowerCase().contains("argo")) {
                LOG.error("[INVOKED >>> ArgoFileServiceImp.validateFile(), it is netcdf file, but not project argo file.");
                isArgoFile = false;
            }
        }

        LOG.debug("[INVOKED >>> ArgoFileServiceImp.validateFile(), file validation success");
        return isArgoFile;
    }

    @Override
    public File convertMultipartFileToFile(final MultipartFile multipartFile) throws IOException {
        netcdfReader = new NetcdfReader();
        return netcdfReader.convertMultiparFiletToFile(multipartFile);
    }

    @Override
    public ArgoFileDataBaseEventSnapshot saveFileEvent(final ArgoFileEvent argoFileEvent) {
        ArgoFileDataBaseEvent event = argoFileDataBaseEventRepository.save(new ArgoFileDataBaseEvent(argoFileEvent.getOccurredOn(), argoFileEvent.getSourceId(), argoFileEvent.getEventType().name()));
        return event.getSnapshot();
    }

    @Override
    public ArgoFileDataBaseEventSnapshot findByOccurredOn(final Instant occurredOn) {
        return argoFileDataBaseEventRepository.findByOccurredOn(occurredOn).getSnapshot();
    }

    @Override
    public void deleteFile(Long id) {
        argoFileRepository.deleteById(id);
    }
}
