package com.decent.csvimporter.generator.api;

import com.decent.csvimporter.generator.service.CsvGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CsvGeneratorController {

    @Autowired
    private CsvGenerator generator;

    @GetMapping(value = "/generate/{maxRows}")
    public void generate(HttpServletResponse response, @PathVariable("maxRows") int maxRows) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=data.csv");
        generator.generate(maxRows, response.getWriter());
    }
}
