package com.bluestone.app.admin.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVWriter;

import com.bluestone.app.admin.model.ProductHeaderEnum;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.ProductService;
import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.shipping.dao.HolidayDao;
import com.google.common.base.Throwables;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

@Service
public class AdminUtilityService {

	private static final Logger log = LoggerFactory.getLogger(AdminUtilityService.class);

	@Autowired
	private TagDao tagDao;

	@Autowired
	private DesignDao designDao;

	@Autowired
	private ProductService productService;

	@Autowired
	private HolidayDao holidayDao;

	public File writeTagsDataToFile(List<Customization> customizationsList, String sessionId) throws Exception {
		final String FILE_NAME_PREFIX = "ProductTags";

		final String PRODUCT_TAGS = generateUniqueFileName(sessionId, FILE_NAME_PREFIX);

		FileWriter fileWriter = null;
		CSVWriter csvWriter = null;
		File file = null;
		try {
			file = getFile(PRODUCT_TAGS);
			fileWriter = new FileWriter(file);
			csvWriter = new CSVWriter(fileWriter);

			int numOfTags = tagDao.getAllTags().size();
			final int MAXSIZE = numOfTags + 3;
			String temp[] = new String[MAXSIZE];
			temp[0] = "ProductId";
			temp[1] = "ProductUrl";
			temp[2] = "ProductCode";
			for (int i = 3; i < numOfTags; i++) {
				temp[i] = "Tagging" + Integer.toString(i - 2);
			}

			csvWriter.writeNext(temp);

			for (Customization customization : customizationsList) {
				assignEmptyValuesToArray(temp);
				temp[0] = Long.toString(customization.getId());
				temp[1] = Util.getSiteUrlWithContextPath() + "/" + customization.getUrl();
				temp[2] = customization.getDesign().getDesignCode();
				Set<Tag> tagsSet = customization.getDesign().getTags();
				int i = 3;
				Iterator<Tag> tagsIterator = tagsSet.iterator();
				while (tagsIterator.hasNext()) {
					temp[i++] = tagsIterator.next().getName();
				}
				csvWriter.writeNext(temp);
			}
			csvWriter.flush();
			return file;
		} catch (Exception exception) {
			log.error("Error while writing tags the file {}", (file != null ? file.getAbsolutePath() : ""), exception);
			throw exception;
		} finally {
			FileUtil.closeStream(csvWriter);
			FileUtil.closeStream(fileWriter);
		}
	}

	private File getFile(final String fileName) {
		File file = new File(fileName);
		final String fileAbsolutePath = file.getAbsolutePath();
		log.debug("AdminUtilityService.getFile() : {}", fileAbsolutePath);
		if (file.exists()) {
			log.debug("{} exists , hence deleting it and creating a new one.", fileAbsolutePath);
			file.delete();
			try {
				boolean isNewFile = file.createNewFile();
				if (isNewFile) {
					log.debug("A new file with the name {} was created successfully", fileAbsolutePath);
				} else {
					log.error("Failed to create a new file-{}. Check if the file {} already exists", fileName,
							fileAbsolutePath);
				}
			} catch (IOException e) {
				log.error("file download - {} file creation failed.", fileAbsolutePath, e);
				throw new RuntimeException(e);
			}
		} else {
			log.debug("No previous File - {} exists. So no deletion required", fileAbsolutePath);
		}
		return file;
	}

	// protected for testing
	protected String generateUniqueFileName(String sessionId, String FILE_NAME_PREFIX) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		String timeStamp = currentTimestamp.toString();
		String fileName = FILE_NAME_PREFIX + sessionId + timeStamp;
		return System.getProperty("java.io.tmpdir") + File.separator + fileName.replaceAll("[^a-zA-Z0-9]+", "")
				+ ".csv";
	}

	private void assignEmptyValuesToArray(String[] temp) {
		// @todo P1 : Rahul Agrawal array index out of bound issue can happen
		// here
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				temp[i] = "";
			}
		}

	}

	@TriggersRemove(cacheName = {"browsePageProductsCache", "searchPageCustomizationCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void uploadProductScoreFile(String[] contentLines) {
		List<Product> productsList = productService.listAllJewelleryProduct();
		if (contentLines != null && contentLines.length > 0) {
			for (int i = 0; i < contentLines.length; i++) {
				if (i == 0) {
					// skipping first row assuming the first row contains
					// headers
					continue;
				}
				String[] row = contentLines[i].split(",");
				Product productToUpdate = null;
				// Assuming first column is product id and second is the value
				// of score
				Long productId = Long.valueOf(row[0]);
				for (Product product : productsList) {
					if (product.getId() == productId) {
						productToUpdate = product;
						break;
					}
				}
				Long score = Long.valueOf(row[1]);
				if (productToUpdate != null) {
					productToUpdate.setScore(score);
					productService.updateProduct(productToUpdate);
				}
			}
		}
	}

	public File writeProductScoreToFile(String sessionId) throws Exception {

		final String FILE_NAME_PREFIX = "ProductScores";

		final String PRODUCT_SCORES = generateUniqueFileName(sessionId, FILE_NAME_PREFIX);

		FileWriter fileWriter = null;
		CSVWriter csvWriter = null;
		File file = null;
		try {
			file = getFile(PRODUCT_SCORES);
			fileWriter = new FileWriter(file);
			csvWriter = new CSVWriter(fileWriter);

			List<Product> productList = productService.listAllJewelleryProduct();

			String temp[] = {"Product Id", "Score"};
			csvWriter.writeNext(temp);

			for (Product product : productList) {
				assignEmptyValuesToArray(temp);
				temp[0] = Long.toString(product.getId());
				temp[1] = Long.toString(product.getScore());
				csvWriter.writeNext(temp);
			}
			csvWriter.flush();
			return file;
		} catch (Exception exception) {
			log.error("Error while writing product score to file {}", (file != null ? file.getAbsolutePath() : ""),
					exception);
			throw exception;
		} finally {
			FileUtil.closeStream(csvWriter);
			FileUtil.closeStream(fileWriter);
		}

	}

	public File writeHolidayToFile(String sessionId) throws Exception {
		final String FILE_NAME_PREFIX = "HolidayList";

		final String HOLIDAY_LIST = generateUniqueFileName(sessionId, FILE_NAME_PREFIX);

		FileWriter fileWriter = null;
		CSVWriter csvWriter = null;
		File file = null;
		try {
			file = getFile(HOLIDAY_LIST);
			fileWriter = new FileWriter(file);
			csvWriter = new CSVWriter(fileWriter);

			String temp[] = {"Dates"};
			csvWriter.writeNext(temp);

			List<Date> holidayList = holidayDao.getHolidayList();

			for (Date date : holidayList) {
				assignEmptyValuesToArray(temp);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				String formatedDate = dateFormat.format(date);
				temp[0] = formatedDate;
				csvWriter.writeNext(temp);
			}
			csvWriter.flush();
			return file;
		} catch (Exception exception) {
			log.error("Error while writing holiday list to file {}", (file != null ? file.getAbsolutePath() : ""),
					exception);
			throw exception;
		} finally {
			FileUtil.closeStream(csvWriter);
			FileUtil.closeStream(fileWriter);
		}
	}

	public File getProductListFile(Enumeration<String> parameterNames, String fileName) {
		LinkedList<ProductHeaderEnum> headerNames = getColumnNamesForProductList(parameterNames);
		List<Map<String, String>> sheetContents = new LinkedList<Map<String, String>>();
		// get design list according to user filter criteria
		List<Design> designs = designDao.listDesigns(0, 20);
		for (Design design : designs) {
			Map<String, String> rowData = new HashMap<String, String>();
			
			rowData.put(ProductHeaderEnum.DESIGN_ID.getHeaderName(), String.valueOf(design.getId()));
			rowData.put(ProductHeaderEnum.DESIGN_DESCRIPTION.getHeaderName(), design.getLongDescription());
			rowData.put(ProductHeaderEnum.DESIGN_CATEGORY.getHeaderName(), design.getDesignCategory().getCategoryType());
			rowData.put(ProductHeaderEnum.DESIGN_CODE.getHeaderName(), design.getDesignCode());
			rowData.put(ProductHeaderEnum.DESIGN_URL.getHeaderName(), design.getCustomizations().get(0).getUrl());
			
			sheetContents.add(new HashMap<String, String>(rowData));
			rowData.clear();
			List<Customization> customizations = design.getCustomizations();
			
			for (Customization customization : customizations) {
				rowData.put(ProductHeaderEnum.PRICE.getHeaderName(), customization.getPrice().toString());
				rowData.put(ProductHeaderEnum.TOTAL_WEIGHT.getHeaderName(),String.valueOf(customization.getTotalWeightOfProduct()));
				rowData.put(ProductHeaderEnum.CUSTOMIZATION_ID.getHeaderName(), String.valueOf(customization.getId()));
				rowData.put(ProductHeaderEnum.CUSTOMIZATION_SKU.getHeaderName(), customization.getSkuCode());
				rowData.put(ProductHeaderEnum.CUSTOMIZATION_PRIORITY.getHeaderName(), String.valueOf(customization.getPriority()));
				rowData.put(ProductHeaderEnum.CUSTOMIZATION_SHORT_DESCRIPTION.getHeaderName(), customization.getShortDescription());
				
				MetalSpecification metalSpecification = customization.getCustomizationMetalSpecification().get(0).getMetalSpecification();
				rowData.put(ProductHeaderEnum.METAL_COLOR.getHeaderName(), metalSpecification.getColor());
				rowData.put(ProductHeaderEnum.METAL_TYPE.getHeaderName(),metalSpecification.getFullname());
				rowData.put(ProductHeaderEnum.METAL_WEIGHT.getHeaderName(), customization.getCustomizationMetalSpecification().get(0).getMetalWeight().toString());

				List<CustomizationStoneSpecification> customizationStoneSpecifications = customization.getCustomizationStoneSpecification();
				for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
					StoneSpecification stoneSpecification = customizationStoneSpecification.getStoneSpecification();
					String stoneType = stoneSpecification.getStoneType();
					String quantityHeader = "";
					String settingTypeHeader = "";
					String shapeHeader = "";
					String sizeHeader = "";
					String weightHeader = "";
					if(stoneType.equalsIgnoreCase("Diamond")){
						rowData.put(ProductHeaderEnum.DIAMOND_CLARITY.getHeaderName(), stoneSpecification.getClarity());
						rowData.put(ProductHeaderEnum.DIAMOND_COLOR.getHeaderName(), stoneSpecification.getColor());
						quantityHeader = ProductHeaderEnum.DIAMOND_QUANTITY.getHeaderName();
						settingTypeHeader = ProductHeaderEnum.DIAMOND_SETTING_TYPE.getHeaderName();
						shapeHeader = ProductHeaderEnum.DIAMOND_SHAPE.getHeaderName();
						sizeHeader = ProductHeaderEnum.DIAMOND_SIZE.getHeaderName();
						weightHeader = ProductHeaderEnum.DIAMOND_WEIGHT.getHeaderName();
					}else{
						rowData.put(ProductHeaderEnum.STONE_TYPE.getHeaderName(), stoneType);
						
						quantityHeader = ProductHeaderEnum.STONE_QUANTITY.getHeaderName();
						settingTypeHeader = ProductHeaderEnum.STONE_SETTING_TYPE.getHeaderName();
						shapeHeader = ProductHeaderEnum.STONE_SHAPE.getHeaderName();
						sizeHeader = ProductHeaderEnum.STONE_SIZE.getHeaderName();
						weightHeader = ProductHeaderEnum.STONE_WEIGHT.getHeaderName();
					}
					rowData.put(quantityHeader, String.valueOf(customizationStoneSpecification.getDesignStoneSpecification().getNoOfStones()));
					rowData.put(settingTypeHeader, customizationStoneSpecification.getDesignStoneSpecification().getSettingType());
					rowData.put(shapeHeader, stoneSpecification.getShape());
					rowData.put(sizeHeader,stoneSpecification.getSize());
					String totalDiamondWeight = (new BigDecimal(customizationStoneSpecification.getDesignStoneSpecification().getNoOfStones())).toString();
					rowData.put(weightHeader, totalDiamondWeight);
					sheetContents.add(new HashMap<String, String>(rowData));
					rowData.clear();
				}
				
				sheetContents.add(new HashMap<String, String>(rowData));
				rowData.clear();
			}
		}
		File file = writeProductDataToFile(fileName, sheetContents, headerNames);
		return file;
	}

	private LinkedList<ProductHeaderEnum> getColumnNamesForProductList(Enumeration<String> parameterNames) {
		List<ProductHeaderEnum> columnHeaderNames = new ArrayList<ProductHeaderEnum>();
		while (parameterNames.hasMoreElements()) {
			String columnName = (String) parameterNames.nextElement();
			ProductHeaderEnum headerEnum = ProductHeaderEnum.getHeader(columnName);
			columnHeaderNames.add(headerEnum);
		}
		LinkedList<ProductHeaderEnum> orderedHeaders = ProductHeaderEnum.getOrderedHeaders(columnHeaderNames);
		return orderedHeaders;
	}

	public File writeProductDataToFile(String uniqueFileName, List<Map<String, String>> sheetContents,LinkedList<ProductHeaderEnum> headerEnums) {
		try {
			String templatePath = "D:\\workspace\\bluestone_prod_2.0\\src\\main\\java\\com\\bluestone\\app\\admin\\service\\Certification_Template.xls";
			InputStream inp = new FileInputStream(templatePath);

			Map<String, Integer> sheetTitleMap = new HashMap<String, Integer>();
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);

			CellStyle cellStyle = getCenteredBorderedCellStyle(wb);
			Row row = sheet.createRow(0);
			int lastColumn = headerEnums.size();

			for (int cn = 0; cn < lastColumn; cn++) {
				Cell cell = row.createCell(cn);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(headerEnums.get(cn).getHeaderName());
				sheet.autoSizeColumn(cn);
				sheetTitleMap.put(headerEnums.get(cn).getHeaderName(), cn);
				sheet.autoSizeColumn(cn);

			}
			int startRowNo =1;
			int srNo = 1;
			long currentTimeMillis = System.currentTimeMillis();
			log.info("Start time-"+currentTimeMillis);
			for (Map<String, String> rowMap : sheetContents) {
				if(rowMap !=null && rowMap.isEmpty() == false){
					Set<String> keySet = rowMap.keySet();
					boolean createRow = false;
					for (String columnName : keySet) {
						Set<String> titleColumnNames = sheetTitleMap.keySet();
						if(titleColumnNames.contains(columnName)){
							createRow = true;
							startRowNo++;
							srNo++;
							break;
						}
					}
					if(createRow){
						Row rowToWrite = sheet.createRow(startRowNo);
						rowToWrite.setHeightInPoints(sheet.getDefaultRowHeightInPoints());
						for (Entry<String, Integer> entry : sheetTitleMap.entrySet()) {
							int cellNo = sheetTitleMap.get(entry.getKey());
							Cell cell = rowToWrite.getCell(cellNo);
							if (cell == null) {
								cell = rowToWrite.createCell(cellNo);
							}
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue(rowMap.get(entry.getKey()));
							sheet.autoSizeColumn(cellNo);
						}
					}
				}
				
			}
			long currentTimeMillis2 = System.currentTimeMillis();
			log.info("Total time -"+(currentTimeMillis2-currentTimeMillis));
			File file = new File(uniqueFileName);
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			inp.close();
			fileOut.close();
			return file;
		} catch (Exception e) {
			final Throwable rootCause = Throwables.getRootCause(e);
			if (log.isErrorEnabled()) {
				log.error("Error occurred while getting Excel Document in DocumentUtil.generateBarcode: Cause={} ",
						e.toString(), rootCause);
			}
		}
		return null;
	}

	private static CellStyle getCenteredBorderedCellStyle(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		return cellStyle;
	}

}
