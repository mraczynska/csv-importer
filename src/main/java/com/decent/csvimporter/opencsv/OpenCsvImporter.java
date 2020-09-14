package com.decent.csvimporter.opencsv;

import com.decent.csvimporter.controller.DataImporter;
import com.decent.csvimporter.generator.model.Account;
import com.opencsv.bean.CsvToBeanBuilder;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@Profile("opencsv")
public class OpenCsvImporter implements DataImporter {

  private OutputStream outputStream = OutputStream.nullOutputStream();

  @Timed(value = "import.time")
  public void makeImport(MultipartFile file) {
    log.info("Import data using opencsv library");

    try (InputStream inputStream = file.getInputStream()) {
      List<Account> accounts =
          new CsvToBeanBuilder(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
              .withIgnoreEmptyLine(true)
              .withType(Account.class)
              .build()
              .parse();
      accounts.forEach(
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
