package com.decent.csvimporter.commoncsv;

import com.decent.csvimporter.controller.DataImporter;
import com.decent.csvimporter.generator.EmailValidator;
import com.decent.csvimporter.generator.model.Account;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Slf4j
@Service
@Profile("commoncsv")
public class CommonCsvImporter implements DataImporter {

  private OutputStream outputStream = OutputStream.nullOutputStream();
  private EmailValidator emailValidator = new EmailValidator();

  @Timed(value = "import.time")
  public void makeImport(MultipartFile file) {
    log.info("Import data using commoncsv library");

    try (InputStream inputStream = file.getInputStream()) {
      CSVParser csvParser =
          CSVFormat.DEFAULT
              .withFirstRecordAsHeader()
              .parse(new InputStreamReader(file.getInputStream()));
      csvParser.getRecords().stream()
          .map(
              record -> {
                return new Account(
                    record.get("NAME"),
                    record.get(1),
                    Integer.valueOf(record.get("AGE")),
                    emailValidator.fromString(record.get("EMAIL")),
                    Double.valueOf(record.get("PRICE")));
              })
          .forEach(
              a -> {
                try {
                  outputStream.write(a.toString().getBytes());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

    } catch (IOException e) {
      log.error("Cannot read file");
    }
  }
}
