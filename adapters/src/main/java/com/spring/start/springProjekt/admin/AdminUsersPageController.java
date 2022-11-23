package com.spring.start.springProjekt.admin;

import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.user.DTO.UserQueryDTO;
import com.spring.start.springProjekt.user.UserQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@Controller
@RequestMapping("/admin/users/")
@Secured(value = {"ROLE_ADMIN", "ROLE_SUPERADMIN"})
class AdminUsersPageController {

    private final static int ELEMENTS = 10;
    private final static Logger LOG = LoggerFactory.getLogger(AdminPageController.class);

    private final AdminService adminService;
    private final UserQueryRepository userQueryRepository;
    private final MessageSource messageSource;

    AdminUsersPageController(final AdminService adminService, final UserQueryRepository userQueryRepository, MessageSource messageSource) {
        this.adminService = adminService;
        this.userQueryRepository = userQueryRepository;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "export/pdf")
    void exportPDF(HttpServletResponse response) throws IOException {

        LOG.debug("**** INVOKED > exportPDF()");

        File file = adminService.generatePDF();
        var fileName = "users.pdf";
        sendFile(response, file, fileName);

    }

    @GetMapping("export/xml")
    void exportXML(HttpServletResponse response) throws IOException {

        LOG.debug("**** INVOKED > export()");

        File file = adminService.generateXML();
        var fileName = "users.xml";
        sendFile(response, file, fileName);

    }

    @GetMapping("export/json")
    void exportJSON(HttpServletResponse response) throws IOException {

        LOG.debug("**** INVOKED > export()");

        File file = adminService.generateJSON();
        var fileName = "users.json";
        sendFile(response, file, fileName);

    }

    private void sendFile(HttpServletResponse response, File file, String fileName) throws IOException {
        OutputStream outStream;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            response.setContentType(Files.probeContentType(FileSystems.getDefault().getPath("springProjekt", fileName)));
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            outStream = response.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
        outStream.close();
    }

    @GetMapping("{page}")
    String openAdminAllUsersPage(@PathVariable("page") int page, Model model) {

        LOG.debug("**** INVOKED > openAdminAllUsersPage(" + page + ", " + model + ")");

        Page<UserQueryDTO> pages = getAllPageable(page - 1, false, null);

        var totalPages = pages.getTotalPages();
        var currentPage = pages.getNumber();

        var userList = pages.getContent();
        model.addAttribute("pages", pages);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("userList", userList);
        return "admin/users";
    }

    @GetMapping("edit/{id}")
    String getUserToEdit(@PathVariable("id") int id, Model model) {

        LOG.debug("**** INVOKED > getUserToEdit(" + id + ", " + model + ")");

        var user = adminService.findUserByIdWitchRole(id);
        if(user.getRoleNumber()==3){

            return "redirect:/admin/users/1";

        }else{

            model.addAttribute("user", user);

            return "admin/useredit";
        }

    }

    @PostMapping("updateuser/{id}")
    String updateUser(@PathVariable("id") int id, UserDTO user) {

        LOG.debug("**** INVOKED > updateUser(" + id + ", " + user + ")");

        var roleNumber = user.getRoleNumber();
        var isActiv = user.getActive();
        adminService.updateUser(id, roleNumber, isActiv);
        return "redirect:/admin/users/1";
    }

    @GetMapping("search/{searchWord}/{page}")
    String openSearchUsersPage(@PathVariable("searchWord") String searchWord,
                               @PathVariable("page") int page, Model model) {

        LOG.debug("**** INVOKED > openSearchUsersPage(" + searchWord + ", " + page + "," + model + ")");

        Page<UserQueryDTO> pages = getAllPageable(page - 1, true, searchWord);

        var totalPages = pages.getTotalPages();
        var currentPage = pages.getNumber();

        var userList = pages.getContent();
        model.addAttribute("pages", pages);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("userList", userList);
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("recordStartCounter", currentPage * ELEMENTS);

        return "admin/usersearch";
    }

    @GetMapping("delete/{id}")
    String deleteUser(@PathVariable("id") int id) {

        var user = adminService.findUserByIdWitchRole(id);
        if(user.getRoleNumber()!=3) {

            LOG.debug("[INVOKED >>> AdminPageController.deleteUser > user id: " + id);

            adminService.deleteUserById(id);
        }

        return "redirect:/admin/users/1";
    }

    private Page<UserQueryDTO> getAllPageable(int page, boolean search, String param) {
        Page<UserQueryDTO> pages;
        if (!search) {
            pages = adminService.findAllPage(PageRequest.of(page, ELEMENTS));
        } else {
            pages = adminService.findAllSearchPage(param, PageRequest.of(page, ELEMENTS));
        }
        pages = pages.map(DTO -> {
            var user = userQueryRepository.findUserByEmail(DTO.getEmail());
            var roleNumber = user.getRoles().iterator().next().getId();
            user.setRoleNumber(roleNumber);
            return user;
        });
        return pages;
    }
}
