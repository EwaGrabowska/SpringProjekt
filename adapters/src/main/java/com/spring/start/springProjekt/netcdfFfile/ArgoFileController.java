package com.spring.start.springProjekt.netcdfFfile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.spring.start.springProjekt.DomainEventPublisher;
import com.spring.start.springProjekt.amazonAWS.AmazonAWSFacade;
import com.spring.start.springProjekt.netcdfFfile.dto.ArgoFileDTO;
import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;
import com.spring.start.springProjekt.utilities.UserUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping(value = "/files/")
class ArgoFileController {
    private static final int ELEMENTS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(ArgoFileController.class);
    private final ArgoFileQueryRepository argoFileQueryRepository;
    private final ArgoFileService argoFileService;
    private final AmazonAWSFacade amazonAWSFacade;
    private final DomainEventPublisher publisher;
    private Environment environment;


    ArgoFileController(Environment environment, final ArgoFileQueryRepository argoFileQueryRepository, final ArgoFileService argoFileService, final AmazonAWSFacade amazonAWSFacade, final DomainEventPublisher publisher) {
        this.environment = environment;
        this.argoFileQueryRepository = argoFileQueryRepository;
        this.argoFileService = argoFileService;
        this.amazonAWSFacade = amazonAWSFacade;
        this.publisher = publisher;
    }

    @GetMapping
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    String showUserProfilePage() {

        LOG.debug("**** WyWOŁANO > showUserProfilePage()");

        return "argoFiles";
    }

    @GetMapping("{page}")
    String openAllFilesPage(@PathVariable("page") int page, Model model) {

        LOG.debug("**** WyWOŁANO > openAllFilesPage(" + page + ")");

        Page<ArgoFileDTO> pages = getAllPageable(page - 1, false, null);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<ArgoFileDTO> fileList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("fileList", fileList);
        return "argoFiles";
    }


    @GetMapping("download/{id}")
    void downloadFile(@PathVariable("id") Long id, HttpServletResponse response) {

        LOG.debug("[WYWOŁANIE >>> ArgoFileController.downloadFile > PARAMETR: " + id);

        ArgoFileDTO argoFileDTO = argoFileQueryRepository.findArgoFileSnapshotById(id);
        String keyName = argoFileDTO.getKeyName();
        //AWS
        String buckedName = System.getProperty("AMAZON_S3_BUCKEDNAME");
        //localhost
//        String buckedName = environment.getProperty("AMAZON.S3.BUCKEDNAME");

        try {
            S3Object object = amazonAWSFacade.downloadObject(buckedName, keyName);
            InputStream inputStream = object.getObjectContent();
            ObjectMetadata metaData = object.getObjectMetadata();
            response.setContentType(metaData.getContentType());
            response.setContentLength((int) metaData.getContentLength());

            String fileName = argoFileDTO.getPlatformNumber() + ".nc";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            ArgoFileEvent argoFileEvent = new ArgoFileEvent(ArgoFileEvent.EventType.DOWNLOADED, new ArgoFileSourceId(id), UserUtilities.getLoggedUser());
            argoFileService.saveFileEvent(argoFileEvent);
            publisher.publish(argoFileEvent);
        } catch (IOException e) {

            LOG.error("[WYWOŁANIE >>> AdminPageController.downloadFile > Błąd przy pobieraniu pliku", e);

            response.setStatus(404);
        }
    }

    private Page<ArgoFileDTO> getAllPageable(int page, boolean search, String param) {
        Page<ArgoFileDTO> pages;
        if (!search) {
            pages = argoFileQueryRepository.findAllBy(PageRequest.of(page, ELEMENTS));
        } else {
            pages = argoFileQueryRepository.findallsearchBy(param, PageRequest.of(page, ELEMENTS));
        }
        return pages;
    }
}
