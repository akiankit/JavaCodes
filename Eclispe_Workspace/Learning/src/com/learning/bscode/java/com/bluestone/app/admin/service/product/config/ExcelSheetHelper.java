package com.bluestone.app.admin.service.product.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.collections.impl.Vector;

import com.bluestone.app.core.util.DataSheetConstants;

/**
 * @author Rahul Agrawal
 *         Date: 9/20/12
 */
class ExcelSheetHelper {

    private static final Logger log = LoggerFactory.getLogger(ExcelSheetHelper.class);

    static final String[] SPREAD_SHEET_NAMES = new String[]{"Datasheet.xlsx","DataSheet.xlsx", "DataSheet.xls","Datasheet.xls"};

    static Object getCell(Vector dataSheetVector, int sheet, int row, int cellIndex) {
        // added 1 to ensure logs are readable. Index starts from 0 in code and from 1 in spreadsheet that we see
        log.info("ExcelSheetHelper.getCell() : sheet={} , row={} , cellIndex={}", (sheet+1), (row+1), (cellIndex+1));

        Object result = null;
        Cell rawCell;
        /*Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        Vector cellStoreVector = (Vector) rowVectorHolder.elementAt(row);
        rawCell = (Cell) cellStoreVector.elementAt(cellIndex);*/
        rawCell = getRawCell(dataSheetVector, sheet, row, cellIndex);

        if (rawCell != null) {
            switch (rawCell.getCellType()) {

                case Cell.CELL_TYPE_STRING:
                    log.info("Data Type = String");
                    result = (rawCell.getRichStringCellValue().getString());
                    break;

                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(rawCell)) {
                        log.info("Data Type = java.util.Date");
                        result = rawCell.getDateCellValue(); // java.util.Date
                    } else {
                        log.info("Data Type = double");
                        result = rawCell.getNumericCellValue(); // primitive double
                    }
                    break;

                case Cell.CELL_TYPE_BOOLEAN:
                    log.info("Data Type = boolean");
                    result = rawCell.getBooleanCellValue(); // boolean
                    break;

                case Cell.CELL_TYPE_FORMULA:
                    log.info("Data Type = Formula");
                    result = rawCell.getCellFormula(); // String
                    break;
                default:
                    log.warn("****** WARNING ****** Cell.getCellType() did not match any of the cell types.Please investigate ************");
                    break;
            }
        }
        log.info("Result = {}\n\n", result);
        return result;
    }

    static Cell getRawCell(Vector dataSheetVector, int sheet, int row, int cellIndex) {
        // added 1 to ensure logs are readable. Index starts from 0 in code and from 1 in spreadsheet that we see
        log.debug("ExcelSheetHelper.getRawCell() : sheet={} , row={} , cellIndex={}", (sheet+1), (row+1), (cellIndex+1));
        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        Vector cellStoreVector = (Vector) rowVectorHolder.elementAt(row);
        if(cellStoreVector != null){
        	Cell cell = (Cell) cellStoreVector.elementAt(cellIndex);
        	return cell;
        }else{
        	return null;
        }
    }

    static Vector importExcelSheet(String designCode, String productUploadPath) throws ProductUploadException {
        log.debug("ExcelSheetHelper.importExcelSheet() for Code={} , Product upload path={}", designCode, productUploadPath);
        Vector sheetVectorHolder = new Vector();
        
        String fileName = getFileNameForCode(designCode, productUploadPath);
        if (fileName == null) {
            throw new ProductUploadException("Could not find the spreadsheet file " + fileName);
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            Workbook workbook = WorkbookFactory.create(fileInputStream); // or     // sample.xls
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Vector rowVectorHolder = new Vector();
                String name = sheet.getSheetName();
                log.debug("ExcelSheetHelper.importExcelSheet() sheet Name={}", name);
                if (DataSheetConstants.sheets.get(name.toLowerCase()) != null) {
                    int rownum = 0;
                    for (Row row : sheet) {
                        Vector cellStoreVector = new Vector();
                        for (Cell cell : row) {
                            int columnIndex = cell.getColumnIndex();
                            try {
                                cellStoreVector.setElementAt(cell, columnIndex);
                            } catch (Exception e) {
                                log.error("Error while consuming row {} cell {} ", row.getRowNum(), columnIndex);
                                throw new ProductUploadException("Error occurred while processing the excel spreadsheet during uploading for design code = "
                                                                 + designCode + " at sheet name=" + name + " row=" + row.getRowNum() +  " column: " + columnIndex);
                            }
                        }
                        if (rownum < row.getRowNum()) {
                            for (int r = 0; r < (row.getRowNum() - rownum); r++) {
                                rowVectorHolder.appendElement(null);
                            }
                            rownum = row.getRowNum();
                        }
                        rownum++;
                        rowVectorHolder.appendElement(cellStoreVector);
                    }
                    sheetVectorHolder.setElementAt(rowVectorHolder, DataSheetConstants.sheets.get(name.toLowerCase()));
                }
            }
            return sheetVectorHolder;
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("Datasheet file not found for designCode = {}", designCode, fileNotFoundException);
            throw new ProductUploadException("Datasheet file not found error for designCode=" + designCode);
        } catch (ProductUploadException e1) {
            throw e1;
        } catch (Exception e1) {
            log.error("Error occurred in processing the excel spread during product upload for designCode = {}", designCode, e1);
            throw new ProductUploadException("Error occurred while processing the excel spreadsheet during uploading for designCode = " + designCode);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.warn("Unable to close the inputstream for the file : " + fileName + " for designCode " + designCode, e);
                }
            }
        }


    }

    private static String getFileNameForCode(String code, String productUploadPath) {
        log.debug("ExcelSheetHelper.getFileNameForCode(): Code={} , productUploadPath={}", code, productUploadPath);
        for (int i = 0; i < SPREAD_SHEET_NAMES.length; i++) {
            final String fileName = productUploadPath + File.separator + code + File.separator + SPREAD_SHEET_NAMES[i];
            log.debug("ExcelSheetHelper.getFileNameForCode() : Generated the File={}", fileName);
            File file = new File(fileName);
            if (file.exists()) {
                return fileName;
            }
        }
        return null;
    }


}
