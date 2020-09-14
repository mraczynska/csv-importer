package com.decent.csvimporter.jacksondataformatcsv;

import com.decent.csvimporter.controller.DataImporter;
import com.decent.csvimporter.generator.model.Account;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("jackson")
public class JacksonDataFormatCsvImporter implements DataImporter {

    private OutputStream outputStream = OutputStream.nullOutputStream();

    @Override
    public void makeImport(MultipartFile file) {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("name", CsvSchema.ColumnType.STRING)
                .addColumn("surname", CsvSchema.ColumnType.STRING)
                .addColumn("email", CsvSchema.ColumnType.STRING)
                .addColumn("age", CsvSchema.ColumnType.NUMBER)
                .addColumn("price", CsvSchema.ColumnType.NUMBER)
                .build().withHeader();

        ObjectReader oReader = csvMapper.reader(Account.class).with(schema);
        List<Account> accounts = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            MappingIterator<Account> account = oReader.readValues(inputStream);
            while (account.hasNext()) {
                Account current = account.next();
                accounts.add(current);
            }

      System.out.println(accounts.size());
            accounts.forEach(
                    a -> {
                        try {
                            outputStream.write(a.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
