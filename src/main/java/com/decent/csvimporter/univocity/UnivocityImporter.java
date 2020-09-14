package com.decent.csvimporter.univocity;

import com.decent.csvimporter.controller.DataImporter;
import com.decent.csvimporter.generator.model.Account;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Slf4j
@Service
@Profile("univocity")
public class UnivocityImporter implements DataImporter {

    private OutputStream outputStream = OutputStream.nullOutputStream();

    @Timed(value = "import.time")
    public void makeImport(MultipartFile file) {
        log.info("Import data using univocity library");

        try (InputStream inputStream = file.getInputStream()) {
            new InputStreamReader(file.getInputStream());
            BeanListProcessor<Account> rowProcessor = new BeanListProcessor<>(Account.class);
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setSkipEmptyLines(true);
            settings.setProcessor(rowProcessor);
            CsvParser parser = new CsvParser(settings);
            parser.parse(new InputStreamReader(inputStream));
            rowProcessor.getBeans().forEach(
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
