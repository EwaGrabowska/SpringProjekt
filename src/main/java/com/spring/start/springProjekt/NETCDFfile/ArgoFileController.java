package com.spring.start.springProjekt.NETCDFfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.GET;
import java.util.List;

@Controller
public class ArgoFileController {

    private static int ELEMENTS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(ArgoFileController.class);

    @Autowired
    private ArgoFileService argoFileService;

    @GET
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/files")
    public String showUserProfilePage() {

        LOG.debug("**** WyWOŁANO > showUserProfilePage()");

        return "argoFiles";
    }

    @GET
    @RequestMapping(value = "/files/{page}")
    public String openAllFilesPage(@PathVariable("page") int page, Model model) {

        LOG.debug("**** WyWOŁANO > openAllFilesPage(" + page + ")");

        Page<ArgoFile> pages = getAllPageable(page - 1, false, null, ArgoFile.class);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<ArgoFile> fileList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("fileList", fileList);
        return "argoFiles";
    }

    private <T> Page<T> getAllPageable(int page, boolean search, String param, Class<T> Tclass) {
        Page<T> pages = null;
        if (!search) {
            pages = (Page<T>) argoFileService.findAll(PageRequest.of(page, ELEMENTS));
        } else {
            pages = (Page<T>) argoFileService.findAllSearch(param, PageRequest.of(page, ELEMENTS));
        }
        return pages;
    }
}
