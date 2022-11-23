package com.spring.start.springProjekt.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spring.start.springProjekt.user.DTO.UserSimpleQueryDTO;
import com.spring.start.springProjekt.user.DTO.UsersSimpleQueryDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class UsersListGenerator {

    File generatePDF(List<UserSimpleQueryDTO> userList) {
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

            PdfPTable table = new PdfPTable(4);
            Stream.of("ID", "Imię", "Nazwisko", "Email")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, polishFont));
                        table.addCell(header);
                    });

            userList
                    .forEach(user -> {
                        table.addCell(user.getId() + "");
                        PdfPCell cellName = new PdfPCell();
                        cellName.setPhrase(new Phrase(user.getName(), polishFont));
                        table.addCell(cellName);
                        PdfPCell cellLastName = new PdfPCell();
                        cellLastName.setPhrase(new Phrase(user.getLastName(), polishFont));
                        table.addCell(cellLastName);
                        table.addCell(user.getEmail());
                    });

            document.add(table);
            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return file;
    }

    File generateXML(List<UserSimpleQueryDTO> userList) {

        UsersSimpleQueryDTO users = new UsersSimpleQueryDTO();
        users.setUsers(userList);
        File file = new File("users.xml");

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(UsersSimpleQueryDTO.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(users, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return file;
    }

    File generateJSON(List<UserSimpleQueryDTO> userList) {

        File file = new File("users.json");
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("users.json"));
            gson.toJson(userList, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;

    }
}
