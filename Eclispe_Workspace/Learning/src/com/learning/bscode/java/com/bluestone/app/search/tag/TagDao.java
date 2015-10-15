package com.bluestone.app.search.tag;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.QueryTimer;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagSEO;
import com.googlecode.ehcache.annotations.Cacheable;

@Repository("TagDao")
public class TagDao extends BaseDao {
    
    private static String  allDesignIdsHavingATag = "select dt.design_id from design_tag dt " +
            " inner join design d on(dt.design_id=d.id) " +
            " inner join tag t on (dt.tag_id=t.id)" +
            " where t.name in (:tags) " +
            " and d.is_active=true" +
            " and d.design_category_id not in(:childDesignCategories) " +
            " group by dt.design_id having count(dt.tag_id) =:tag_count";
    
    private static String  allDesignIds = "select dt.design_id from design_tag dt " +
            " inner join design d on(dt.design_id=d.id) " +
            " and d.is_Active=true" + 
            " and d.design_category_id not in(:childDesignCategories) " +
            " group by dt.design_id";
    
    
    private static String  countOfDesignForATag = "select count(*) from design_tag dt where dt.tag_id = :tagId";

    
    private static final Logger log = LoggerFactory.getLogger(TagDao.class);
    
    @Cacheable(cacheName="tagSeoCache")
    public TagSEO getOverrideSEODetails(List<String> tagNamesList) {
        TagSEO retVal = null;
        if (tagNamesList != null && tagNamesList.size() > 0) {
            Query tagSEOQuery = getTagSEOQuery(tagNamesList);
            try {
                if (tagSEOQuery != null) {
                    retVal = ((TagSEO) tagSEOQuery.getSingleResult());
                }
            } catch (NonUniqueResultException e) {
                if (log.isErrorEnabled()) {
                    log.error("Multiple tagSEO results for "+StringUtils.join(tagNamesList,','), e);
                }
                retVal = (TagSEO) tagSEOQuery.getResultList().get(0);
            } catch (NoResultException e) {
                if (log.isDebugEnabled()) {
                    log.debug(e.getMessage());
                }
            }
        }
        return retVal;
    }
    
    private Query getTagSEOQuery(List<String> tagNamesList) {
        if (tagNamesList != null && tagNamesList.size() > 0) {
            Query tagSEOQuery;
            tagSEOQuery = getEntityManagerForActiveEntities().createNamedQuery("TagSEO.getOverrideDetails");
            tagSEOQuery.setParameter("tags", tagNamesList);
            tagSEOQuery.setParameter("tag_count", (long) tagNamesList.size());
            return tagSEOQuery;
        } else {
            return null;
        }
    }
    
    //@Cacheable(cacheName="tagListByNames")
    public List<Tag> getTagListFromNames(List<String> tagList) {
        List<Tag> list = new ArrayList<Tag>();
        if (tagList.size() > 0) {
            Query createQuery = getEntityManagerForActiveEntities().createNamedQuery("Tag.getTags");
            createQuery.setParameter("tags", tagList);
            list = createQuery.getResultList();
        }
        return list;
    }
    
    public List<Tag> getAllTagsForDesigns(List<String> tagList,List<Long> finalChildDesignCateogories) {
        long start = QueryTimer.getCurrentTime();
        List<Tag> finalTagList = new ArrayList<Tag>();
         Query createQuery;
         if (!tagList.isEmpty()) {
             createQuery = getEntityManagerForActiveEntities().createNativeQuery(allDesignIdsHavingATag);
             createQuery.setParameter("tags", tagList);
             createQuery.setParameter("tag_count", (long) tagList.size());
         } else {
             createQuery = getEntityManagerForActiveEntities().createNativeQuery(allDesignIds);
         }
         createQuery.setParameter("childDesignCategories", finalChildDesignCateogories);
         
         List<BigInteger> designList = createQuery.getResultList();
         List<Long> designIdList = new ArrayList<Long>();
         for (BigInteger eachId : designList) {
             designIdList.add(eachId.longValue());
         }
         
         if(designIdList != null && designIdList.size() > 0) {
             Query query = getEntityManagerForActiveEntities().createNamedQuery("Tag.getByDesignIds");
             query.setParameter("dids", designIdList);
             finalTagList = query.getResultList();
         }
         
         long end = QueryTimer.getCurrentTime();
         if(QueryTimer.isOn()) {
            QueryTimer.logger.debug("Total time to getAllTagsForDesigns: {}", (end-start));
         }
         return finalTagList;
     }
    
    public Tag getTag(String tag) {
        Query createQuery = getEntityManagerForActiveEntities().createQuery("select t from Tag t where t.name =:tag");
        createQuery.setParameter("tag", tag);
        Tag tagRes = null;
        try {
            tagRes = (Tag) createQuery.getSingleResult();
        } catch (NoResultException e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage());
            }
        }
        return tagRes;
    }
    
    public List<Tag> getAllTags() {
        Query createQuery = getEntityManagerForActiveEntities().createQuery("select t from Tag t");
        List<Tag> list = createQuery.getResultList();
        
        return list;
    }
    
    public int getCountOfDesignForATag(long tagId) {
        Query createQuery = getEntityManagerForActiveEntities().createNativeQuery(countOfDesignForATag);
        createQuery.setParameter("tagId", tagId);
        BigInteger countOfDesigns = (BigInteger) createQuery.getSingleResult();
        
        return countOfDesigns.intValue();
    }
    
}
