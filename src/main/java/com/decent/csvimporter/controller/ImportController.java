package com.decent.csvimporter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Profile({"csveed", "commoncsv", "flatpack", "opencsv", "supercsv", "univocity", "jackson"})
public class ImportController {

    @Autowired
    private DataImporter importer;

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadData(@RequestParam("file") MultipartFile file) {
        importer.makeImport(file);
        return ResponseEntity.ok().build();
    }
}