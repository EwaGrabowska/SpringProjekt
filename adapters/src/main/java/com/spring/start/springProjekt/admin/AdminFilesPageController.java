package com.spring.start.springProjekt.admin;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.spring.start.springProjekt.DomainEventPublisher;
import com.spring.start.springProjekt.amazonAWS.AmazonAWSFacade;
import com.spring.start.springProjekt.netcdfFfile.ArgoFileQueryRepository;
import com.spring.start.springProjekt.netcdfFfile.ArgoFileService;
import com.spring.start.springProjekt.netcdfFfile.ArgoFileSnapshot;
import com.spring.start.springProjekt.netcdfFfile.dto.ArgoFileDTO;
import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileEvent;
import com.spring.start.springProjekt.netcdfFfile.vo.ArgoFileSourceId;
import com.spring.start.springProjekt.utilities.UserUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/admin/files/")
class AdminFilesPageController {
    @Value("${amazon.S3.buckedname}")
    private String buckedName;
    private final static int ELEMENTS = 10;
    private final static Logger LOG = LoggerFactory.getLogger(AdminPageController.class);
    private final ArgoFileService argoFileService;
    private final ArgoFileQueryRepository argoFileQueryRepository;
    private final AmazonAWSFacade amazonAWSFacade;
    private final MessageSource messageSource;
    private final DomainEventPublisher publisher;

    AdminFilesPageController(ArgoFileService argoFileService, final ArgoFileQueryRepository argoFileQueryRepository, final AmazonAWSFacade amazonAWSFacade, MessageSource messageSource, final DomainEventPublisher publisher) {
        this.argoFileService = argoFileService;
        this.argoFileQueryRepository = argoFileQueryRepository;
        this.amazonAWSFacade = amazonAWSFacade;
        this.messageSource = messageSource;
        this.publisher = publisher;
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("{page}")
    String openAdminAllFilesPage(@PathVariable("page") int page, Model model, Locale locale) {

        LOG.debug("**** INVOKED > openAdminAllFilesPage(" + page + ", " + model + ")");

        Page<ArgoFileDTO> pages = getAllPageable(page - 1, false, null);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<ArgoFileDTO> fileList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("fileList", fileList);
        model.addAttribute("recordStartCounter", currentPage * ELEMENTS);
        return "admin/files";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("search/{searchWord}/{page}")
    String openSearchFilesPage(@PathVariable("searchWord") String searchWord,
                               @PathVariable("page") int page, Model model) {

        LOG.debug("**** INVOKED > openSearchFilesPage(" + searchWord + ", " + page + "," + model + ")");

        Page<ArgoFileDTO> pages = getAllPageable(page - 1, true, searchWord);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<ArgoFileDTO> fileList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("fileList", fileList);
        model.addAttribute("recordStartCounter", currentPage * ELEMENTS);
        model.addAttribute("searchWord", searchWord);
        return "admin/filessearch";
    }

    @PostMapping("upload")
    @Transactional
    @Secured(value = {"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    String importFile(@RequestParam("filename") MultipartFile mFile, Model model, Locale locale, HttpServletRequest request) throws IOException {


        if(request.isUserInRole("ROLE_ADMIN")){
            LOG.debug("**** INVOKED > importFile(" + mFile + "). User with role admin.");
            model.addAttribute("message", messageSource.getMessage("fili.import.accessDenied", null, locale));
            return openAdminAllFilesPage(1, model,locale);
        }
        LOG.debug("**** INVOKED > importFile(" + mFile + ")");

        if (!mFile.isEmpty()) {
            byte[] bytes = mFile.getBytes();
            File file = argoFileService.convertMultipartFileToFile(mFile);

            if (!argoFileService.validateFile(mFile, file)) {
                model.addAttribute("message", messageSource.getMessage("file.afterEdit.chosenInCorrect", null, locale));
            } else {
                try {
                    String fileName = mFile.getOriginalFilename();
                    String fileExtension = "";
                    if (fileName.contains(".")) {
                        fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    }
                    UUID uuid = UUID.randomUUID();
                    String keyName = uuid + "." + fileExtension;
                    ObjectMetadata metaData = new ObjectMetadata();
                    metaData.setContentLength(bytes.length);
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    amazonAWSFacade.putObject(buckedName, keyName, inputStream, metaData);
                    ArgoFileSnapshot argoFile = argoFileService.addFile(file, keyName);
                    ArgoFileEvent argoFileEvent = new ArgoFileEvent(ArgoFileEvent.EventType.SAVED, new ArgoFileSourceId(argoFile.getId()), UserUtilities.getLoggedUser());
                    argoFileService.saveFileEvent(argoFileEvent);
                    publisher.publish(argoFileEvent);
                    model.addAttribute("message", messageSource.getMessage("file.afterEdit.chosenCorrect", null, locale));
                    LOG.debug("**** INVOKED > importFile(" + mFile + "). The file has been added to the database.");

                } catch (Exception e) {

                    LOG.error("**** INVOKED > importFile(" + mFile + "). The file was not added to the database. " + e);

                    e.printStackTrace();
                }
            }

        } else {
            model.addAttribute("message", messageSource.getMessage("file.noFile", null, locale));

            LOG.error("**** INVOKED > importFile(" + mFile + "). The file is empty. ");
        }
        return openAdminAllFilesPage(1, model,locale);
    }

    @GetMapping("delete/{id}")
    @Transactional
    @Secured(value = {"ROLE_SUPERADMIN"})
    String deleteFile(@PathVariable("id") Long id) {

        LOG.debug("[INVOKED >>> AdminPageController.deleteFile > file id: " + id);
        ArgoFileDTO argoFileDTO = argoFileQueryRepository.findArgoFileSnapshotById(id);

        amazonAWSFacade.deleteObject(buckedName, argoFileDTO.getKeyName());
        argoFileService.deleteFile(id);

        var event = new ArgoFileEvent(ArgoFileEvent.EventType.DELETED, new ArgoFileSourceId(id), UserUtilities.getLoggedUser());
        argoFileService.saveFileEvent(event);
        publisher.publish(event);
        return "redirect:/admin/files/1";
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    @GetMapping("download/{id}")
    String downloadFile(@PathVariable("id") Long id, HttpServletResponse response) {

        LOG.debug("[INVOKED >>> ArgoFileController.downloadFile > file id: " + id);

        ArgoFileDTO argoFileDTO = argoFileQueryRepository.findArgoFileSnapshotById(id);
        String keyName = argoFileDTO.getKeyName();

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

            LOG.error("[INVOKED >>> AdminPageController.downloadFile > Error during downloading the file", e);

            response.setStatus(404);
        }

        return "redirect:/admin/files/1";
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
