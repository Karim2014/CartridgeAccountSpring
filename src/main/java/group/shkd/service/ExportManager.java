package group.shkd.service;

import group.shkd.model.Cartridge;
import group.shkd.model.RefuelingList;
import group.shkd.model.Repository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExportManager {

    private XSSFWorkbook workbook;

    @Autowired
    private DataSource dataSource;

    public void exportToPdf(String fileName, int listId) throws JRException, SQLException {
        //Компиляция файла jrxml
        JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/jrxml/refueling-list.jrxml"));

        //Параметры отчета
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("list_id", listId);

        //DataSource
        Connection jrDataSource = dataSource.getConnection();

        //Заполняем отчет
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, jrDataSource);

        //XLS exporter
        JRXlsxExporter exporter = new JRXlsxExporter();
        //Input
        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
        exporter.setExporterInput(exporterInput);
        //Output
        OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput("output.xlsx");
        exporter.setExporterOutput(exporterOutput);

        SimpleXlsxExporterConfiguration configuration = new SimpleXlsxExporterConfiguration();

        //exporter.setConfiguration(configuration);
        exporter.exportReport();

        //JasperExportManager.exportReportToPdfFile(jasperPrint, "output.xlsx");

    }

    @Deprecated
    public void exportToExcelViaPoi(String fileName, RefuelingList refuelingList)  {
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Картриджи на заправку");
        CellStyle cellStyle = createStyle();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        cell.setCellValue("Картриджи на заправку");
        cell.setCellStyle(cellStyle);
        row.createCell(1);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,1));
        int rowIndex = 1;
        for (Cartridge cartridge : refuelingList.getCartridges()) {
            row = sheet.createRow(rowIndex++);
            cell = row.createCell(0);
            cell.setCellValue(cartridge.getFullName());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(cartridge.getNum());
            cell.setCellStyle(cellStyle);
        }
        sheet.setColumnWidth(0, 8500);
        sheet.setColumnWidth(1, 5500);
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CellStyle createStyle() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Times New Roman");
        cellStyle.setFont(font);

        return cellStyle;
    }

}
