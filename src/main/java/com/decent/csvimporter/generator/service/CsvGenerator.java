package com.decent.csvimporter.generator.service;

import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;

@Service
public class CsvGenerator {

  private static final String EMAIL_DOMAIN = "@decent.com";
  private static final String DELIMITER = ",";
  private static final String HEADERS =
      "NAME".concat(DELIMITER).concat("SURNAME").concat(DELIMITER).concat("EMAIL").concat(DELIMITER).concat("AGE").concat(DELIMITER).concat("PRICE").concat("\n");

  public void generate(int maxRows, PrintWriter writer) {
    StringBuilder stringBuilder = new StringBuilder().append(HEADERS);

    IntStream.range(0, maxRows)
        .forEach(
            row -> {
              String name = randomString(15);
              String surname = randomString(20);
              int age = nextInt(20, 50);
              String email = randomString(10).concat(EMAIL_DOMAIN);
              double price = nextDouble(10.0, 10000.0);

              String csvRow =
                  name.concat(DELIMITER)
                      .concat(surname)
                      .concat(DELIMITER)
                      .concat(email)
                      .concat(DELIMITER)
                      .concat(String.valueOf(age))
                      .concat(DELIMITER)
                      .concat(String.valueOf(price));
              stringBuilder.append(csvRow).append("\n");
            });

    writer.write(stringBuilder.toString());
  }

  private static String randomString(int length) {
    return randomAlphabetic(length);
  }
}
