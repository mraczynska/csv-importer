package com.decent.csvimporter.csveed;

import com.decent.csvimporter.controller.DataImporter;
import com.decent.csvimporter.generator.model.Account;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Slf4j
@Service
@Profile("csveed")
public class CsveedImporter implements DataImporter {

    private OutputStream outputStream = OutputStream.nullOutputStream();

    @Timed(value = "import.time")
    public void makeImport(MultipartFile file) {

        log.info("Import data using csveed library");
        try (InputStream inputStream = file.getInputStream()) {
            CsvClient<Account> csvClient = new CsvClientImpl<>(new InputStreamReader(inputStream), Account.class);
            csvClient.readBeans().forEach(
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
