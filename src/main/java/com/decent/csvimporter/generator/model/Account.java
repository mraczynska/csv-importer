package com.decent.csvimporter.generator.model;

import com.decent.csvimporter.generator.EmailValidator;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Validate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvConverter;
import org.csveed.annotations.CsvFile;
import org.csveed.bean.ColumnNameMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CsvFile(separator = ',', mappingStrategy = ColumnNameMapper.class)
public class Account {

  @CsvBindByName(column = "NAME", required = true)
  @Parsed(field = "name")
  @CsvCell(columnName = "NAME", required = true)
  private String name;

  @CsvBindByName(column = "SURNAME", required = true)
  @Parsed(field = "surname")
  @CsvCell(columnName = "SURNAME", required = true)
  private String surname;

  @CsvBindByName(column = "AGE", required = true)
  @Parsed(field = "age")
  @CsvCell(columnName = "AGE", required = true)
  private int age;

  @PreAssignmentValidator(
      validator = MustMatchRegexExpression.class,
      paramString = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$")
  @CsvBindByName(column = "EMAIL", required = true)
  @Parsed(field = "email")
  @CsvCell(columnName = "EMAIL", required = true)
  @CsvConverter(converter = EmailValidator.class)
  @Validate(matches = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$")
  private String email;

  @CsvBindByName(column = "PRICE")
  @Parsed(field = "price")
  @CsvCell(columnName = "PRICE")
  private double price;
}
