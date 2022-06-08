package org.bardframework.form.table.utils;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.bardframework.form.table.TableData;
import org.bardframework.form.table.TableHeader;
import org.bardframework.form.table.TableModel;
import org.bardframework.form.table.TableTemplate;
import org.bardframework.form.table.header.TableHeaderTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExcelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    private ExcelUtils() {
        /*
            prevent instantiation
         */
    }

    public static void generateExcel(TableTemplate tableTemplate, TableData tableData, OutputStream outputStream, Locale locale, boolean rtl) throws Exception {
        try (Workbook workbook = new SXSSFWorkbook()) {
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setFontName("Tahoma");
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
            headerStyle.setAlignment(HorizontalAlignment.GENERAL);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Font dataFont = workbook.createFont();
            dataFont.setFontHeightInPoints((short) 10);
            dataFont.setFontName("Tahoma");
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(dataFont);
            dataStyle.setAlignment(HorizontalAlignment.GENERAL);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            ExcelUtils.createSheet(workbook, tableTemplate, tableData, headerStyle, dataStyle, locale);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                workbook.getSheetAt(i).setRightToLeft(rtl);
            }
            workbook.write(outputStream);
        }
    }

    public static void createSheet(Workbook workbook, TableTemplate tableTemplate, TableData tableData, CellStyle headerStyle, CellStyle dataStyle, Locale locale) throws Exception {
        TableModel tableModel = TableUtils.toTable(tableTemplate, locale, Map.of());
        Sheet sheet = workbook.createSheet(tableModel.getName());
        ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        /*
            write headers
        */
        int columnIndex = 0;
        for (TableHeader header : tableModel.getHeaders()) {
            Cell cell = headerRow.createCell(columnIndex++);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header.getTitle());
        }

        /*
         ساخت استایل های مورد نیاز همه ی هدرها
         */
        Map<String, CellStyle> styles = new HashMap<>();
        for (TableHeaderTemplate<?, ?> headerModel : tableTemplate.getHeaderTemplates()) {
            if (null != headerModel.getExcelFormat()) {
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.cloneStyleFrom(dataStyle);
                cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat(headerModel.getExcelFormat()));
                styles.put(headerModel.getExcelFormat(), cellStyle);
            }
        }

        /*
            write data
        */
        for (Map<String, List<Object>> rowDataMap : tableData.getData()) {
            List<Object> rowData = rowDataMap.values().iterator().next();
            Row row = sheet.createRow(rowIndex++);
            for (int index = 0; index < tableTemplate.getHeaderTemplates().size(); index++) {
                Cell cell = row.createCell(index);
                Object cellValue = rowData.get(index);
                TableHeaderTemplate<?, ?> headerTemplate = tableTemplate.getHeaderTemplates().get(index);
                if (null == cellValue) {
                    cell.setBlank();
                } else {
                    if (Number.class.isAssignableFrom(cellValue.getClass())) {
                        cell.setCellValue(((Number) cellValue).doubleValue());
                    } else {
                        cell.setCellValue(cellValue.toString());
                    }
                }
                if (null != headerTemplate.getExcelFormat()) {
                    cell.setCellStyle(styles.get(headerTemplate.getExcelFormat()));
                } else {
                    cell.setCellStyle(dataStyle);
                }
                index++;
            }
        }
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            sheet.autoSizeColumn(i);
        }
        sheet.createFreezePane(0, 1);
    }

}
