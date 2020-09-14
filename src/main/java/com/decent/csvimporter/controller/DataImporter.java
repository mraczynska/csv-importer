package com.decent.csvimporter.controller;

import org.springframework.web.multipart.MultipartFile;

public interface DataImporter {

    void makeImport(MultipartFile file);
}
