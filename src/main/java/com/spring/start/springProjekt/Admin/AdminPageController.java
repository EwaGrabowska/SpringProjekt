package com.spring.start.springProjekt.Admin;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spring.start.springProjekt.NETCDFfile.ArgoFile;
import com.spring.start.springProjekt.NETCDFfile.ArgoFileService;
import com.spring.start.springProjekt.NETCDFfile.NetcdfReader;
import com.spring.start.springProjekt.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.util.StreamUtils.BUFFER_SIZE;

@Controller
public class AdminPageController {

    private static int ELEMENTS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(AdminPageController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ArgoFileService argoFileService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Environment environment;


    @GET
    @RequestMapping("/admin/users/export/pdf")
    @Secured(value = {"ROLE_ADMIN"})
    public void exportPDF(HttpServletResponse response) throws IOException {

        LOG.debug("**** WyWOŁANO > exportPDF()");

        UsersListGenerator usersListGenerator = new UsersListGenerator();
        List<User> userList = adminService.findAll();
        File file = usersListGenerator.generatePDF(userList);
        String fileName = "users.pdf";
        sendFile(response, file, fileName);

    }

    @GET
    @RequestMapping("/admin/users/export/xml")
    @Secured(value = {"ROLE_ADMIN"})
    public void exportXML(HttpServletResponse response) throws IOException {

        LOG.debug("**** WyWOŁANO > export()");

        UsersListGenerator usersListGenerator = new UsersListGenerator();
        List<User> userList = adminService.findAll();
        File file = usersListGenerator.generateXML(userList);
        String fileName = "users.xml";
        sendFile(response, file, fileName);

    }

    @GET
    @RequestMapping("/admin/users/export/json")
    @Secured(value = {"ROLE_ADMIN"})
    public void exportJSON(HttpServletResponse response) throws IOException {

        LOG.debug("**** WyWOŁANO > export()");

        UsersListGenerator usersListGenerator = new UsersListGenerator();
        List<User> userList = adminService.findAll();
        File file = usersListGenerator.generateJSON(userList);
        String fileName = "users.json";
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


    @GET
    @RequestMapping(value = "/admin")
    @Secured(value = {"ROLE_ADMIN"})
    public String openAdminMainPage() {

        LOG.debug("**** WyWOŁANO > openAdminMainPage()");

        return "admin/admin";
    }

    @GET
    @RequestMapping(value = "/admin/users/{page}")
    @Secured(value = {"ROLE_ADMIN"})
    public String openAdminAllUsersPage(@PathVariable("page") int page, Model model) {

        LOG.debug("**** WyWOŁANO > openAdminAllUsersPage(" + page + ", " + model + ")");

        Page<User> pages = getAllPageable(page - 1, false, null, User.class);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();

        List<User> userList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("userList", userList);
        return "admin/users";
    }

    @GET
    @RequestMapping(value = "/admin/files/{page}")
    public String openAdminAllFilesPage(@PathVariable("page") int page, Model model, Locale locale) {

        LOG.debug("**** WyWOŁANO > openAdminAllFilesPage(" + page + ", " + model + ")");

        Page<ArgoFile> pages = getAllPageable(page - 1, false, null, ArgoFile.class);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<ArgoFile> fileList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("fileList", fileList);
        return "admin/files";
    }


    @GET
    @RequestMapping(value = "/admin/users/edit/{id}")
    @Secured(value = {"ROLE_ADMIN"})
    public String getUserToEdit(@PathVariable("id") int id, Model model) {

        LOG.debug("**** WyWOŁANO > getUserToEdit(" + id + ", " + model + ")");

        User user = adminService.findUserById(id);
        Map<Integer, String> roleMap = prepareRoleMap();
        Map<Integer, String> activityMap = prepareActivityMap();
        int role = user.getRoles().iterator().next().getId();
        user.setNrRoli(role);

        model.addAttribute("roleMap", roleMap);
        model.addAttribute("activityMap", activityMap);
        model.addAttribute("user", user);

        return "admin/useredit";
    }

    @POST
    @RequestMapping(value = "/admin/updateuser/{id}")
    @Secured(value = "ROLE_ADMIN")
    public String updateUser(@PathVariable("id") int id, User user) {

        LOG.debug("**** WyWOŁANO > updateUser(" + id + ", " + user + ")");

        int nrRoli = user.getNrRoli();
        int czyActive = user.getActive();
        adminService.updateUser(id, nrRoli, czyActive);
        return "redirect:/admin/users/1";
    }

    @GET
    @RequestMapping(value = "/admin/users/search/{searchWord}/{page}")
    @Secured(value = "ROLE_ADMIN")
    public String openSearchUsersPage(@PathVariable("searchWord") String searchWord,
                                      @PathVariable("page") int page, Model model) {

        LOG.debug("**** WyWOŁANO > openSearchUsersPage(" + searchWord + ", " + page + "," + model + ")");

        Page<User> pages = getAllPageable(page - 1, true, searchWord, User.class);
        int totalPages = pages.getTotalPages();
        int currentPage = pages.getNumber();
        List<User> userList = pages.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("userList", userList);
        model.addAttribute("recordStartCounter", currentPage * ELEMENTS);
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("userList", userList);
        return "admin/usersearch";
    }

    @GET
    @RequestMapping(value = "/admin/users/importusers")
    @Secured(value = "ROLE_ADMIN")
    public String showUploadPageFromXML(Model model) {
        return "admin/importusers";
    }

    @POST
    @RequestMapping(value = "/admin/files/upload")
    @Secured(value = "ROLE_ADMIN")
    public String importFile(@RequestParam("filename") MultipartFile mFile, Model model, Locale locale) throws IOException {

        LOG.debug("**** WyWOŁANO > importFile(" + mFile + ")");

        if (!mFile.isEmpty()) {
            if (!validateFile(mFile)) {
                model.addAttribute("message", messageSource.getMessage("file.afterEdit.chosenInCorect", null, locale));
            } else {
                try {
                    String fileName = mFile.getOriginalFilename();
                    String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    UUID uuid = UUID.randomUUID();
                    String keyName = uuid.toString() + "." + fileExtension;
                    String bucketName = "myspring";

                    byte[] bytes = mFile.getBytes();
                    ObjectMetadata metaData = new ObjectMetadata();
                    metaData.setContentLength(bytes.length);
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, inputStream, metaData);

                    AmazonS3 s3client = getAccountClient();
                    s3client.putObject(putObjectRequest);

                    argoFileService.addFile(mFile, keyName);
                    model.addAttribute("message", messageSource.getMessage("file.afterEdit.chosenCorect", null, locale));

                    LOG.debug("**** WyWOŁANO > importFile(" + mFile + "). Plik dodany do bazy.");

                } catch (Exception e) {

                    LOG.error("**** WyWOŁANO > importFile(" + mFile + "). Plik nie został dodany. " + e.toString());

                    e.printStackTrace();
                }
            }

        } else {
            model.addAttribute("message", messageSource.getMessage("file.noFile", null, locale));
        }

        return "admin/afterUpload";
    }

    @DELETE
    @RequestMapping(value = "/admin/users/delete/{id}")
    @Secured(value = "ROLE_ADMIN")
    public String deleteUser(@PathVariable("id") int id) {

        LOG.debug("[WYWOŁANIE >>> AdminPageController.deleteUser > PARAMETR: " + id);

        adminService.deleteUserById(id);
        return "redirect:/admin/users/1";
    }

    @DELETE
    @RequestMapping(value = "/admin/files/delete/{id}")
    @Secured(value = "ROLE_ADMIN")
    public String deleteFile(@PathVariable("id") Long id) {

        LOG.debug("[WYWOŁANIE >>> AdminPageController.deleteFile > PARAMETR: " + id);

        ArgoFile argoFile = argoFileService.getFileById(id);
        AmazonS3 client = getAccountClient();
        client.deleteObject("myspring", argoFile.getKeyName());
        argoFileService.deleteFile(id);
        return "redirect:/admin/files/1";
    }

    @GET
    @RequestMapping(value = "/admin/files/download/{id}")
    @Secured(value = "ROLE_ADMIN")
    public void downloadFile(@PathVariable("id") Long id, HttpServletResponse response) {

        LOG.debug("[WYWOŁANIE >>> AdminPageController.downloadFile > PARAMETR: " + id);

        ArgoFile argoFile = argoFileService.getFileById(id);
        String keyName = argoFile.getKeyName();

        try {
            AmazonS3 client = getAccountClient();
            S3Object object = client.getObject(new GetObjectRequest("myspring", keyName));
            InputStream inputStream = object.getObjectContent();
            ObjectMetadata metaData = object.getObjectMetadata();
            response.setContentType(metaData.getContentType());
            response.setContentLength((int) metaData.getContentLength());

            String fileName = argoFile.getPlatformNumber() + ".nc";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {

            LOG.error("[WYWOŁANIE >>> AdminPageController.downloadFile > Błąd przy pobieraniu pliku", e);

            response.setStatus(404);
        }
    }

    private <T> Page<T> getAllPageable(int page, boolean search, String param, Class<T> Tclass) {
        Page<T> pages = null;
        if (!search) {
            if (Tclass.getSimpleName().equals("User")) {
                pages = (Page<T>) adminService.findAll(PageRequest.of(page, ELEMENTS));
            }
            if (Tclass.getSimpleName().equals("ArgoFile")) {
                pages = (Page<T>) argoFileService.findAll(PageRequest.of(page, ELEMENTS));
            }
        } else {
            if (Tclass.getSimpleName().equals("User")) {
                pages = (Page<T>) adminService.findAllSearch(param, PageRequest.of(page, ELEMENTS));
            }
            if (Tclass.getSimpleName().equals("ArgoFile")) {
                pages = (Page<T>) argoFileService.findAllSearch(param, PageRequest.of(page, ELEMENTS));
            }
        }
        if (Tclass.getSimpleName().equals("User")) {
            for (T tclass : pages) {
                User user = (User) tclass;
                int numerRoli = user.getRoles().iterator().next().getId();
                user.setNrRoli(numerRoli);
            }
        }
        return pages;
    }

    public Map<Integer, String> prepareRoleMap() {
        Locale locale = Locale.getDefault();
        Map<Integer, String> roleMap = new HashMap<Integer, String>();
        roleMap.put(1, messageSource.getMessage("word.admin", null, locale));
        roleMap.put(2, messageSource.getMessage("word.user", null, locale));
        return roleMap;
    }

    public Map<Integer, String> prepareActivityMap() {
        Locale locale = Locale.getDefault();
        Map<Integer, String> activityMap = new HashMap<Integer, String>();
        activityMap.put(0, messageSource.getMessage("word.nie", null, locale));
        activityMap.put(1, messageSource.getMessage("word.tak", null, locale));
        return activityMap;
    }

    public AmazonS3 getAccountClient() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        AWSCredentials credentials = new BasicAWSCredentials(
                environment.getProperty("amazon.S3.accessKey"),
                environment.getProperty("amazon.S3.secretKey")
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfig)
                .withRegion(Regions.EU_CENTRAL_1)
                .build();

        return s3client;
    }

    public File generatePDF() {
        Document document = new Document(PageSize.A4);
        File file = new File("users.pdf");

        try {
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font polishFont = new Font(baseFont);

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.add(new Paragraph("Lista użytkowników:", polishFont));
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            Stream.of("ID", "Imię", "Nazwisko", "Email", "Aktywny")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, polishFont));
                        table.addCell(header);
                    });

            adminService.findAll().stream()
                    .forEach(user -> {
                        table.addCell(user.getId() + "");
                        PdfPCell cellName = new PdfPCell();
                        cellName.setPhrase(new Phrase(user.getName(), polishFont));
                        table.addCell(cellName);
                        PdfPCell cellLastName = new PdfPCell();
                        cellLastName.setPhrase(new Phrase(user.getLastName(), polishFont));
                        table.addCell(cellLastName);
                        table.addCell(user.getEmail());
                        if (user.getActive() == 1) {
                            table.addCell("TAK");
                        } else {
                            table.addCell("NIE");
                        }

                    });

            document.add(table);
            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return file;
    }

    private boolean validateFile(MultipartFile mFile) {
        boolean fileIsOk = true;
        String fileExtension = "";
        try {
            String fileName = mFile.getOriginalFilename();
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!fileExtension.equals("nc")) {
                fileIsOk = false;
            } else {
                String title = NetcdfReader.readTitle(mFile);
                if (!title.toLowerCase().contains("argo")) {
                    fileIsOk = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileIsOk;
    }

}
