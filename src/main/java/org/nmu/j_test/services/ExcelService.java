package org.nmu.j_test.services;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nmu.j_test.models.Title;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Service
public class ExcelService {

    public XSSFWorkbook getWorkbook (List<Title> titles)
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet();

        var row_0 = sheet.createRow(0);
        var style = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        row_0.createCell(0).setCellValue("Название");
        row_0.createCell(1).setCellValue("Сезон");
        row_0.createCell(2).setCellValue("Ссылка");
        row_0.createCell(3).setCellValue("Описание");
        row_0.createCell(4).setCellValue("Обновлено (UTC)");

        for (int i = 0; i < 5; i++) {
            row_0.getCell(i).setCellStyle(style);
        }

        int rowNum = 1;
        for (var title : titles)
        {
            var row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(title.getName());
            row.createCell(1).setCellValue(title.getSeason());
            row.createCell(2).setCellValue(title.getLink());
            row.createCell(3).setCellValue(title.getDescription());

            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy, HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            var dateString = formatter.format(title.getLastUpdate());

            row.createCell(4).setCellValue(dateString);
            rowNum++;
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

}
