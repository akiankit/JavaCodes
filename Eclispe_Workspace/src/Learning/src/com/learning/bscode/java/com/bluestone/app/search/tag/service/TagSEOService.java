package com.bluestone.app.search.tag.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.search.tag.dao.TagSEODao;
import com.bluestone.app.search.tag.model.TagSEO;


@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class TagSEOService {
    
    private static final Logger log = LoggerFactory.getLogger(TagSEOService.class);
    
    @Autowired
    private TagSEODao tagseoDao;    
    
    public Map<String, Object> getTagSEOPaginatedList(ListFilterCriteria filterCriteria,boolean forActive) {
        int startingFrom = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
        Map<Object, Object> tagseosListAndTotalCount = tagseoDao.getTagSEOsAndTotalCount(startingFrom, filterCriteria, forActive);
        String baseURLForPagination = forActive == true ? "/admin/search/tag/tagseo/list" : "/admin/search/tag/tagseo/disabledlist";
        List<TagSEO> tagseosList = (List<TagSEO>) tagseosListAndTotalCount.get("list");
        Map<String, Object> dataForView = generateDataForView(tagseosListAndTotalCount,
                                                                             filterCriteria.getItemsPerPage(), filterCriteria.getP(),
                                                                             startingFrom, baseURLForPagination,tagseosList.size());
        Map<String,String> searchFieldMap = new HashMap<String, String>();
		searchFieldMap.put("Summary Text Header", "summaryTextHeader");
		searchFieldMap.put("Html Page Title", "htmlPageTitle");
		searchFieldMap.put("Meta Keywords", "metaKeywords");
		searchFieldMap.put("Meta Description", "metaDescription");
		searchFieldMap.put("H1 Page Title", "h1PageTitle");
		searchFieldMap.put("Tags","tag.name");
		
		dataForView.put("searchFieldMap", searchFieldMap);
        return dataForView;
    }    
    
    private Map<String, Object> generateDataForView(Map<Object, Object> listAndTotalCount, int itemsPerPage,
			int currentPageNumber, int startingFrom, String baseURLForPagination, int listSize) {
		int totalCount = (Integer) listAndTotalCount.get("totalCount");
		int numberOfPages = (int) Math.ceil((double) (totalCount) / (double) (itemsPerPage));

		Map<Object, Object> paginationDataWithPrevAndNextLinks = PaginationUtil.getPagination(currentPageNumber,
																							  numberOfPages, baseURLForPagination);
		Map<String, Object> itemsDetails = new HashMap<String, Object>();
		itemsDetails.put("list", listAndTotalCount.get("list"));
		itemsDetails.put("paginationData", paginationDataWithPrevAndNextLinks.get("paginationData"));
		itemsDetails.put("prevLink", paginationDataWithPrevAndNextLinks.get("prevLink"));
		itemsDetails.put("nextLink", paginationDataWithPrevAndNextLinks.get("nextLink"));
		itemsDetails.put("currentPageNumber", currentPageNumber);
		itemsDetails.put("numberOfPages", numberOfPages);
		itemsDetails.put("totalCount", totalCount);
		itemsDetails.put("showingFrom", startingFrom + 1);
		itemsDetails.put("showingTo", startingFrom + listSize);
		return itemsDetails;
	}
    
    public <T> T findAny(Class<T> entityClass, long primaryKey) {
        return tagseoDao.findAny(entityClass, primaryKey);
    }
    
    public <T> T find(Class<T> entityClass, long primaryKey, boolean forActiveEntities) {
        return tagseoDao.find(entityClass, primaryKey, forActiveEntities);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public void create(BaseEntity entity) {
    	tagseoDao.create(entity);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public <T> T update(T entity) {
        return tagseoDao.update(entity);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public BaseEntity markEntityAsDisabled(BaseEntity baseEntity) {
        return tagseoDao.markEntityAsDisabled(baseEntity);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public BaseEntity markEntityAsEnabled(BaseEntity baseEntity) {
        return tagseoDao.markEntityAsEnabled(baseEntity);
    }
}
