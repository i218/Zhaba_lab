package org.nmu.j_test.controllers;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nmu.j_test.services.ALSiteParserService;
import org.nmu.j_test.services.ExcelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ALController {
    final ALSiteParserService parserService;
    final ExcelService excelService;

    public ALController(ALSiteParserService parserService, ExcelService excelService) {
        this.parserService = parserService;
        this.excelService = excelService;
    }

    @GetMapping
    public String getIndex() {
       return "index";
    }

    @GetMapping("titles")
    public String getTitles(String query, Model model) {
        model.addAttribute("titles", parserService.findTitles(query));
        model.addAttribute("query", query);
        return "titles";
    }

    @GetMapping("api/titles-excel")
    public void getExcel(String query, HttpServletResponse response) {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=titles.xlsx");

        try (ServletOutputStream stream = response.getOutputStream()) {
            try (XSSFWorkbook workbook = excelService.getWorkbook(parserService.findTitles(query))) {
                workbook.write(stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
