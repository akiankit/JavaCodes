package com.bluestone.app.design.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;

@Repository
public class ParentChildCategoryDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(ParentChildCategoryDao.class);
    
    
    public List<String> getChildCategoryByParent(String parentCategoryType) {
        Query getChildCategoryByParentQuery = getEntityManagerForActiveEntities().createQuery("select pc.childCategory.categoryType from ParentChildCategory pc where pc.parentCategory.categoryType =:parentCategoryType");
        getChildCategoryByParentQuery.setParameter("parentCategoryType", parentCategoryType);
        return getChildCategoryByParentQuery.getResultList();
    }
    
    public List<Object[]> getChildDesignCategoriesIdAndName() {
        Query getChildDesignCategoriesQuery = getEntityManagerForActiveEntities().createQuery("select pc.childCategory.categoryType, pc.childCategory.id from ParentChildCategory pc");
        List<Object[]> childDesignCategories = getChildDesignCategoriesQuery.getResultList();
        if (childDesignCategories == null || childDesignCategories.size() == 0) {
            childDesignCategories.add(new Object[] { "", 0l });
        }
        return childDesignCategories;
    }

    public List<Long> getFinalChildDesignCateogories(List<String> taglist, List<Object[]> childDesignCategories) {
        List<Long> finalChildCategories = new ArrayList<Long>();
        for (Object[] childCateogory : childDesignCategories) {
            boolean existsInUrl = false;
            String categoryName = childCateogory[0].toString();
            Long categoryId = (Long) childCateogory[1];
            if (!StringUtils.isBlank(categoryName)) {
                if (taglist != null && taglist.size() != 0) {
                    for (String tag : taglist) {
                        if (existsInUrl) {
                            continue;
                        }
                        if (categoryName.equalsIgnoreCase(tag)) {
                            existsInUrl = true;
                        }
                    }
                }
                if (existsInUrl == false) {
                    finalChildCategories.add(categoryId);
                }
            }
        }
        
        if (finalChildCategories.isEmpty()) {
            finalChildCategories.add(0l);
        }
        return finalChildCategories;
    }

}
