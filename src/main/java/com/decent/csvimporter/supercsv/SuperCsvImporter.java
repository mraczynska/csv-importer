package com.decent.csvimporter.supercsv;

import com.decent.csvimporter.controller.DataImporter;
import com.decent.csvimporter.generator.model.Account;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Profile("supercsv")
public class SuperCsvImporter implements DataImporter {

  private OutputStream outputStream = OutputStream.nullOutputStream();

  @Timed(value = "import.time")
  public void makeImport(MultipartFile file) {

    log.info("Import data using supercsv library");

    List<Account> accounts = new ArrayList<Account>();
    try (InputStream inputStream = file.getInputStream()) {
      ICsvBeanReader beanReader =
          new CsvBeanReader(
              new InputStreamReader(inputStream, StandardCharsets.UTF_8),
              CsvPreference.STANDARD_PREFERENCE);

      final String[] nameMapping = new String[] {"name", "surname", "email", "age", "price"};
      final String[] header = beanReader.getHeader(true);

      final CellProcessor[] processors = getProcessors();
      Account account;
      while ((account = beanReader.read(Account.class, nameMapping, processors)) != null) {
        accounts.add(account);
      }
      accounts.forEach(
          a -> {
            try {
              outputStream.write(a.toString().getBytes());
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
      beanReader.close();

    } catch (IOException e) {
      log.error("Cannot read file");
    }
  }

  private static CellProcessor[] getProcessors() {
    final CellProcessor[] processors =
        new CellProcessor[] {
          new NotNull(), // NAME
          new NotNull(), // SURNAME
          new StrRegEx("^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$"), // EMAIL
          new NotNull(new ParseInt()), // AGE
          new Optional(new ParseDouble()) // PRICE
        };
    return processors;
  }
}
