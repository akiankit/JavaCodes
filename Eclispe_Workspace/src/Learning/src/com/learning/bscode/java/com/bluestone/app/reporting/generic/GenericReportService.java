package com.bluestone.app.reporting.generic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.reporting.AbstractReportService;
import com.bluestone.app.reporting.GenericReport;

@Service
@Transactional(readOnly=true)
public class GenericReportService extends AbstractReportService<GenericReport>{
	
	@Autowired
	private GenericReportDao genericReportDao;
	
	@Override
    public int getCountOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate) {
		String query = listFilterCriteria.getQuery();
		String[] querySplit = query.split("(from|FROM)");
		query = "select count(*) from " + querySplit[1];
		return genericReportDao.getCountOfRecords(query);
	}
	
	 @Override
	 public List<GenericReport> getListOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate, int offset) {
		 List<GenericReport> listOfGenericReport = new ArrayList<GenericReport>();
		 List<Object[]> records = genericReportDao.getRecords(listFilterCriteria.getQuery(), listFilterCriteria.getItemsPerPage(), offset);
         for (Object[] record : records) {
         	String[] stringArray = new String[record.length]; 
     		for (int i = 0; i < record.length; i++) {
     			if(record[i]!=null)
     				stringArray[i] = record[i].toString();
     		}
     		listOfGenericReport.add(new GenericReport(stringArray));
         }
         return listOfGenericReport;
	 }
	 
}
