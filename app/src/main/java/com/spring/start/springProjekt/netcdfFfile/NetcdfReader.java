package com.spring.start.springProjekt.netcdfFfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ucar.netcdf.Attribute;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


class NetcdfReader {

    private static final Logger LOG = LoggerFactory.getLogger(NetcdfReader.class);

    File convertMultiparFiletToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + "file.nc");
        multipart.transferTo(convFile);
        return convFile;
    }

    ArgoFile setArgoFileAtribute(File file) throws IOException {
        Netcdf nc;
        ArgoFile argoFile = new ArgoFile();
        try {
            nc = new NetcdfFile(file, true);
            String featureType = readFeatureType(nc);
            LocalDateTime creationDate = readCreationDate(nc);
            int platformNumber = readPlatformNumber(nc);
            String projectName = readProjectName(nc);
            String nameOfPrincipalInvestigator = readNameOfPrincipalInvestigator(nc);

            argoFile.setFileAttributes(platformNumber, featureType, creationDate, projectName, nameOfPrincipalInvestigator);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return argoFile;
    }

    String readTitle(File file) throws IOException {
        Netcdf nc;
        String title = "";
        try {
            nc = new NetcdfFile(file, true);
            Attribute titleAtribute = nc.getAttribute("title");
            title = titleAtribute.getStringValue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return title;
    }

    private static String readNameOfPrincipalInvestigator(Netcdf nc) throws IOException {
        Variable pi_name = nc.get("PI_NAME");
        int[] pi_nameShape = pi_name.getLengths();
        StringBuilder pi_nameStringBuilder = new StringBuilder();
        if (pi_nameShape.length > 1) {
            int columns2 = pi_nameShape[1];
            for (int i = 0; i < columns2; i++) {
                pi_nameStringBuilder.append(pi_name.getChar(new int[]{0, i}));
            }
        } else {
            int columns2 = pi_nameShape[0];
            for (int i = 0; i < columns2; i++) {
                pi_nameStringBuilder.append(pi_name.getChar(new int[]{i}));
            }
        }

        String pi_nameToString = pi_nameStringBuilder.toString().trim();
        return pi_nameToString;
    }

    private static String readProjectName(Netcdf nc) throws IOException {
        Variable projectName = nc.get("PROJECT_NAME");
        int[] projectNameShape = projectName.getLengths();
        StringBuilder projectNameStringBuilder = new StringBuilder();
        if (projectNameShape.length > 1) {
            int columns1 = projectNameShape[1];
            for (int i = 0; i < columns1; i++) {
                projectNameStringBuilder.append(projectName.getChar(new int[]{0, i}));
            }
        } else {
            int columns1 = projectNameShape[0];
            for (int i = 0; i < columns1; i++) {
                projectNameStringBuilder.append(projectName.getChar(new int[]{i}));
            }
        }

        String projectNameToString = projectNameStringBuilder.toString().trim();
        return projectNameToString;
    }

    private static int readPlatformNumber(Netcdf nc) throws IOException {
        Variable uniqueIdentifier = nc.get("PLATFORM_NUMBER");
        int[] uniqueIdentifierShape = uniqueIdentifier.getLengths();
        StringBuilder stringBuilder1 = new StringBuilder();
        if (uniqueIdentifierShape.length > 1) {
            int columns = uniqueIdentifierShape[1];
            for (int i = 0; i < columns; i++) {
                stringBuilder1.append(uniqueIdentifier.getChar(new int[]{0, i}));
            }
        } else {
            int columns = uniqueIdentifierShape[0];
            for (int i = 0; i < columns; i++) {
                stringBuilder1.append(uniqueIdentifier.getChar(new int[]{i}));
            }
        }

        return Integer.parseInt(stringBuilder1.toString().trim());
    }

    private static LocalDateTime readCreationDate(Netcdf nc) throws IOException {
        Variable creationDate = nc.get("DATE_CREATION");
        int nlats = creationDate.getLengths()[0];
        char[] lats = new char[nlats];
        int[] index = new int[1];
        StringBuilder stringBuilder = new StringBuilder();
        for (int ilat = 0; ilat < nlats; ilat++) {
            index[0] = ilat;
            lats[ilat] = creationDate.getChar(index);
            stringBuilder.append(lats[ilat]);
        }
        String str = stringBuilder.toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(str, formatter);
    }

    private static String readFeatureType(Netcdf nc) {
        Attribute featureType = nc.getAttribute("featureType");
        String featureTypeToString = featureType.getStringValue();
        return featureTypeToString;
    }
}
