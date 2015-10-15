package com.bluestone.app.design.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.search.tag.model.Tag;
import com.googlecode.ehcache.annotations.Cacheable;

@Repository("customizationDao")
public class CustomizationDao extends BaseDao {

    private static String       countQuery                  = "select count(*) from ("
                                                                    + "select count(c.id) from customization c "
                                                                    + " inner join product p on(c.id=p.id) "
                                                                    + " inner join design d on(c.design_id=d.id) "
                                                                    + " inner join design_tag dt on (dt.design_id = d.id)"
                                                                    + " inner join tag t ON (dt.tag_id=t.id) "
                                                                    + " where c.priority=:priority and p.is_active=1 "
                                                                    + " and d.design_category_id not in(:childDesignCategories) group by c.design_id ) AS temp";

    private static String     countQueryForSearchResults          = "select count(*) from (select count(c.id) from customization c"
                                                                        + " inner join product p on(c.id=p.id) "
                                                                        + " inner join design d on(c.design_id=d.id) "
                                                                        + " inner join design_tag dt on (dt.design_id = d.id) "
                                                                        + " inner join tag t ON (dt.tag_id=t.id) "
                                                                        + " where c.priority=:priority and p.is_active=1 "
                                                                        + " and t.name in (:tags) and d.id in (:designIds)"
                                                                        + " group by c.design_id having count(t.id) =:tag_count) AS temp";

    private static String       countQueryWithTags          = "select count(*) from (select count(c.id) from customization c"
                                                                    + " inner join product p on(c.id=p.id) "
                                                                    + " inner join design d on(c.design_id=d.id) "
                                                                    + " inner join design_tag dt on (dt.design_id = d.id) "
                                                                    + " inner join tag t ON (dt.tag_id=t.id) "
                                                                    + " where c.priority=:priority and p.is_active=1 "
                                                                    + " and d.design_category_id not in(:childDesignCategories) and t.name in (:tags) "
                                                                    + " group by c.design_id having count(t.id) =:tag_count) AS temp";

    private static String       minMaxAndCountQuery         = "select min(temp.price), max(temp.price), count(*) from "
                                                                    + " (select c.price as price, count(c.id) from customization c "
                                                                    + " inner join product p on(c.id=p.id) "
                                                                    + " inner join design d on(c.design_id=d.id)"
                                                                    + " inner join design_tag dt on (dt.design_id = d.id)"
                                                                    + " inner join tag t ON (dt.tag_id=t.id) "
                                                                    + " where c.priority=:priority and p.is_active=1 "
                                                                    + " and d.design_category_id not in(:childDesignCategories) group by c.design_id ) AS temp";

    private static String       minMaxAndCountQueryWithTags = "select min(temp.price), max(temp.price) , count(*) from "
                                                                    + " (select c.price as price, count(c.id) from customization c "
                                                                    + " inner join product p on(c.id=p.id) "
                                                                    + " inner join design d on(c.design_id=d.id)"
                                                                    + " inner join design_tag dt on (dt.design_id = d.id) "
                                                                    + " inner join tag t ON (dt.tag_id=t.id) "
                                                                    + " where c.priority=:priority and p.is_active=1"
                                                                    + " and d.design_category_id not in(:childDesignCategories) "
                                                                    + " and t.name in (:tags) "
                                                                    + " group by c.design_id having count(t.id) =:tag_count) AS temp";

    private static String changeDerivedCustomizationStatus = "update product AS p INNER JOIN customization AS cu on cu.id=p.id set p.is_active=:isactive where cu.parentCustomization_id=:customizationId";

    private static String changeAllCustomizationStatus = "update product AS p INNER JOIN customization AS cu on cu.id=p.id set p.is_active=:isactive where cu.design_id=:designId";

    private static final Logger log                         = LoggerFactory.getLogger(CustomizationDao.class);

    @Autowired
    private ParentChildCategoryDao parentChildCategoryDao;


    public Customization getCustomizationsByDesign(Long designId, int priority) {
        try {
            Query createNamedQuery = getEntityManagerForActiveEntities().createNamedQuery("Customization.getByDesignId");
            createNamedQuery.setParameter("designId", designId);
            createNamedQuery.setParameter("priority", priority);
            Customization customization = (Customization) createNamedQuery.getSingleResult();
            return customization;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Customization getCustomizationWithoutIsActiveFilter(Long designId, int priority) {
        try {
            Query createNamedQuery = getEntityManagerWithoutFilter().createNamedQuery("Customization.getByDesignId");
            createNamedQuery.setParameter("designId", designId);
            createNamedQuery.setParameter("priority", priority);
            Customization customization = (Customization) createNamedQuery.getSingleResult();
            return customization;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Cacheable(cacheName="searchPageCustomizationCache")
    public Map<String, Object> getCustomizationsAndTotalCountForSearchQuery(List<String> cleanSearchTerms, int start, int maxRows, String sortBy, String[] tags) {
        List<Customization> currentPageResultList = new ArrayList<Customization>();
        List<Customization> totalResultList = new ArrayList<Customization>();
        int resultCount = 0;

        if (cleanSearchTerms.size() > 0) {

            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManagerForActiveEntities());
            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Customization.class).get();

            org.apache.lucene.search.Query query = null;

            BooleanJunction<BooleanJunction> bool = qb.bool();
            TermMatchingContext onFields = qb.keyword().onFields("design.id", "design.designCode", "shortDescription", "design.tags.name", "design.longDescription", "design.designName", "design.designCategory.categoryType");

            for (String cleanSearchTerm : cleanSearchTerms) {
                bool.must(onFields.matching(cleanSearchTerm).createQuery());
            }

            List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
            // Disabling search for child categories
            for (Object[] childCategory : childDesignCategories) {
                String categoryName = childCategory[0].toString();
                String categoryId = childCategory[1].toString();
                if (!StringUtils.isBlank(categoryName)) {
                    org.apache.lucene.search.Query createQuery = qb.keyword().onField("design.designCategory.id").matching(categoryId).createQuery();
                    bool.must(createQuery).not();
                }
            }
            query = bool.createQuery();

            FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(query, Customization.class);
            persistenceQuery.enableFullTextFilter("priority").setParameter("priority", 1);
            persistenceQuery.enableFullTextFilter("isActive").setParameter("isActive", (short) 1);

            org.apache.lucene.search.Sort sort = null;

			if (sortBy.equals(Constants.MOSTPOPULAR)) {
				sort = new Sort(new SortField("score", SortField.LONG, true));
			} else if (sortBy.equals(Constants.PRICEHIGH)) {
				sort = new Sort(new SortField("price", SortField.FLOAT, true));
			} else if (sortBy.equals(Constants.PRICELOW)) {
				sort = new Sort(new SortField("price", SortField.FLOAT, false));
			} else if (sortBy.equals(Constants.DATE_ADD)) {
				sort = new Sort(new SortField("createdAt", SortField.LONG, true));
			}

            if (sort != null) {
                persistenceQuery.setSort(sort);
            }

            resultCount = persistenceQuery.getResultSize();
            totalResultList = persistenceQuery.getResultList();
            int toIndex = start + maxRows;
            if (toIndex > totalResultList.size()) {
                toIndex = totalResultList.size();
            }
            currentPageResultList = totalResultList.subList(start, toIndex);
        }

        Map<String, Object> customizationListAndTotalCount = null;
        if (resultCount > 0) {
            if (tags != null) {
                List<Long> designIds = new ArrayList<Long>();
                for (Customization custom : totalResultList) {
                    designIds.add(custom.getDesign().getId());
                }
                customizationListAndTotalCount = this.getCustomizationsAndTotalCountForSearchTags(designIds, tags, start, maxRows, sortBy);
            } else {
                customizationListAndTotalCount = new HashMap<String, Object>();
                customizationListAndTotalCount.put("customizationList", currentPageResultList);
                customizationListAndTotalCount.put("customizationTotalList", totalResultList);
                customizationListAndTotalCount.put("totalCustomizationCount", resultCount);
            }
        }
        return customizationListAndTotalCount;
    }

    private Map<String, Object> getCustomizationsAndTotalCountForSearchTags(List<Long> desingIds, String[] tags, int start, int maxRows, String sortBy) {
        List<Customization> totalResultList = new ArrayList<Customization>();
        List<Customization> currentPageResultList;
        if (sortBy == null || sortBy.length() == 0) {
            sortBy = Constants.DATE_ADD;
        }

        Query customizationListQuery = getEntityManagerForActiveEntities().createNamedQuery("Customization.customizationListBySearchTags_" + sortBy);
        customizationListQuery.setParameter("tags", Arrays.asList(tags));
        customizationListQuery.setParameter("tag_count", (long) tags.length);
        customizationListQuery.setParameter("designIds", desingIds);
        customizationListQuery.setParameter("priority", 1);
        totalResultList = customizationListQuery.getResultList();

        Query customizationCountQuery = getEntityManagerForActiveEntities().createNativeQuery(countQueryForSearchResults);
        customizationCountQuery.setParameter("tags", Arrays.asList(tags));
        customizationCountQuery.setParameter("tag_count", (long) tags.length);
        customizationCountQuery.setParameter("designIds", desingIds);
        customizationCountQuery.setParameter("priority", 1);
        BigInteger countFromDb = (BigInteger) customizationCountQuery.getSingleResult();
        int customizationCount = countFromDb.intValue();

        int toIndex = start + maxRows;
        if (toIndex > totalResultList.size()) {
            toIndex = totalResultList.size();
        }
        currentPageResultList = totalResultList.subList(start, toIndex);

        Map<String, Object> customizationDetails = new HashMap<String, Object>();
        customizationDetails.put("customizationList", currentPageResultList);
        customizationDetails.put("customizationTotalList", totalResultList);
        customizationDetails.put("totalCustomizationCount", customizationCount);
        return customizationDetails;
    }

    @Cacheable(cacheName="seoMinMaxPriceCache")
    public Long[] getMinAndMaxPriceFromTags(List<String> selectedTagNamesList) {
        Long[] minMaxAndCount = new Long[3];
        try {
            Query query;
            List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
            List<Long> finalChildDesignCategories = parentChildCategoryDao.getFinalChildDesignCateogories(selectedTagNamesList, childDesignCategories);
            if (!selectedTagNamesList.isEmpty()) {
                query = getEntityManagerForActiveEntities().createNativeQuery(minMaxAndCountQueryWithTags);
                query.setParameter("tags", selectedTagNamesList);
                query.setParameter("tag_count", (long) selectedTagNamesList.size());
            } else {
                query = getEntityManagerForActiveEntities().createNativeQuery(minMaxAndCountQuery);
            }
            query.setParameter("childDesignCategories", finalChildDesignCategories);
            query.setParameter("priority", 1);

            Object[] resultArray = (Object[]) query.getSingleResult();

            Object minPrice = resultArray[0];
            if (minPrice != null) {
                minMaxAndCount[0] = ((BigDecimal) minPrice).longValue();
            } else {
                minMaxAndCount[0] = 0l;
            }

            Object maxPrice = resultArray[1];
            if (maxPrice != null) {
                minMaxAndCount[1] = ((BigDecimal) maxPrice).longValue();
            } else {
                minMaxAndCount[1] = 0l;
            }

            Object count = resultArray[2];
            if (count instanceof Long) {
                minMaxAndCount[2] = (Long) count;
            } else if (count instanceof BigInteger) {
                minMaxAndCount[2] = ((BigInteger) count).longValue();
            }
        } catch (NoResultException e) {
            if (log.isDebugEnabled()) {
                log.debug("Unable to fetch min price for tags = ", selectedTagNamesList, e);
            }
            minMaxAndCount[0] = new Long(0);
            minMaxAndCount[1] = new Long(0);
            minMaxAndCount[2] = new Long(0);
        }
        return minMaxAndCount;
    }

    @Cacheable(cacheName="seoCountCache")
    public int getCustomizationCountFromTags(List<String> taglist) {
        Query customizationCountQuery;
        List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
        List<Long> finalChildDesignCategories = parentChildCategoryDao.getFinalChildDesignCateogories(taglist, childDesignCategories);
        if (!taglist.isEmpty()) {
            customizationCountQuery = getEntityManagerForActiveEntities().createNativeQuery(countQueryWithTags);
            customizationCountQuery.setParameter("tags", taglist);
            customizationCountQuery.setParameter("tag_count", (long) taglist.size());
        } else {
            customizationCountQuery = getEntityManagerForActiveEntities().createNativeQuery(countQuery);
        }
        customizationCountQuery.setParameter("priority", 1);
        customizationCountQuery.setParameter("childDesignCategories", finalChildDesignCategories);
        int intValue = ((BigInteger) customizationCountQuery.getSingleResult()).intValue();
        return intValue;
    }

    @Cacheable(cacheName="browsePageProductsCache")
	public Map<String, Object> getCustomizationsAndTotalCount(List<String> taglist, Set<Tag> tagSet, int start,
																int maxRows, String sortBy, List<Long> preferredProductIds) {

		//sortBy = getSortBy(sortBy);
		List<Customization> list;
		Query customizationListQuery;
		List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
		List<Long> finalChildCategories = parentChildCategoryDao.getFinalChildDesignCateogories(taglist,childDesignCategories);
		if (start != 0) {
			start = start - preferredProductIds.size();
		}
		if (preferredProductIds == null || preferredProductIds.size() == 0) {
			preferredProductIds = new ArrayList<Long>();
			preferredProductIds.add(0l);
		}

		List<Customization> finalPreferredProductList = new ArrayList<Customization>();
		if (start == 0) {
			List<Customization> preferredProductsList = getProductsList(preferredProductIds);
			if (preferredProductsList != null && preferredProductsList.size() > 0) {
				for (Customization preferredProduct : preferredProductsList) {
					boolean hasAllTags = true;
					Set<Tag> tags = preferredProduct.getDesign().getTags();
					if (tagSet != null && tagSet.size() > 0) {
						for (Tag tag : tagSet) {
							if (!tags.contains(tag)) {
								hasAllTags = false;
								break;
							}
						}
					}
					if (hasAllTags == true) {
						finalPreferredProductList.add(preferredProduct);
					}
				}
			}
		}

		int preferredProductsSize = finalPreferredProductList.size();

		if (!taglist.isEmpty()) {
			customizationListQuery = getEntityManagerForActiveEntities().createNamedQuery(
					"Customization.customizationListByTags_" + sortBy);
			customizationListQuery.setParameter("tags", taglist);
			customizationListQuery.setParameter("tag_count", (long) taglist.size());
		} else {
			customizationListQuery = getEntityManagerForActiveEntities().createNamedQuery(
					"Customization.customizationDefaultList_" + sortBy);
		}
		customizationListQuery.setParameter("childDesignCategories", finalChildCategories);
		customizationListQuery.setParameter("preferredProductIds", preferredProductIds);
		customizationListQuery.setParameter("priority", 1);

		customizationListQuery.setFirstResult(start);
		customizationListQuery.setMaxResults(maxRows - preferredProductsSize);
		list = customizationListQuery.getResultList();

		int customizationCount = getCustomizationCountFromTags(taglist);
		Map<String, Object> customizationDetails = new HashMap<String, Object>();
		List<Customization> finalList = new ArrayList<Customization>();
		finalList.addAll(finalPreferredProductList);
		finalList.addAll(list);
		customizationDetails.put("customizationList", finalList);
		customizationDetails.put("totalCustomizationCount", customizationCount);
		return customizationDetails;
	}

    public List<Customization> getRelatedProductsByPrice(Customization customization, BigDecimal priceDifference, int noOfProducts) {
        BigDecimal price = customization.getPrice();
        BigDecimal higherPrice = price.add(priceDifference);
        BigDecimal lowerPrice = price.subtract(priceDifference);

        Query createNamedQuery = getEntityManagerForActiveEntities().createNamedQuery("Customization.getRelatedProductsByPrice");
        createNamedQuery.setParameter("customizationId", customization.getId());
        createNamedQuery.setParameter("lowerPrice", lowerPrice);
        createNamedQuery.setParameter("higherPrice", higherPrice);
        createNamedQuery.setParameter("categoryType", customization.getDesign().getDesignCategory().getCategoryType());
        createNamedQuery.setMaxResults(noOfProducts);

        List<Customization> resultList = createNamedQuery.getResultList();
        if (log.isDebugEnabled()) {
            log.debug(createNamedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        }
        return resultList;

    }

    @Cacheable(cacheName="productListCache")
    public List<Customization> getProductsList(List<Long> list) {
        Query createNamedQuery = getEntityManagerForActiveEntities().createNamedQuery("Customization.getByDesignIdList");
        createNamedQuery.setParameter("designId", list);
        createNamedQuery.setParameter("priority", 1);
        List<Customization> resultList = createNamedQuery.getResultList();
        return resultList;
    }

    // return list of customizations including active and inactive entities
    public List<Customization> getProductByDesignId(Long designId) {
        Query createNamedQuery = getEntityManagerWithoutFilter().createNamedQuery("Customization.getProductByDesignId");
        createNamedQuery.setParameter("designId", designId);
        List<Customization> resultList = createNamedQuery.getResultList();
        return resultList;
    }

    public List<Customization> getCustomizationList() {
        Query createQuery = getEntityManagerForActiveEntities().createQuery("select c from Customization c");
        List<Customization> list = createQuery.getResultList();
        return list;
    }

    public List<Customization> getAllCustomizationWithPriority(int priority) {
        Query getAllCustomizationWithPriority = getEntityManagerForActiveEntities().createNamedQuery("customization.getAllCustomizationWithPriority");
        getAllCustomizationWithPriority.setParameter("priority", priority);
        return getAllCustomizationWithPriority.getResultList();
    }
    
    public List<Customization> getAllCustomizationsofParentCategoryWithPriority(int priority) {
        Query getAllCustomizationWithPriority = getEntityManagerForActiveEntities().createNamedQuery("customization.getAllCustomizationsofParentCategoryWithPriority");
        getAllCustomizationWithPriority.setParameter("priority", priority);        
        return getAllCustomizationWithPriority.getResultList();
    }

    public Customization getCustomizationBySku(Customization customization) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select c from Customization c where c.skuCode=:sku");
        createQuery.setParameter("sku", customization.getSkuCode());
        createQuery.setMaxResults(1);
        try {
            Customization custom = (Customization) createQuery.getSingleResult();
            custom.setImageVersion(custom.getImageVersion() + 1);
            return custom;
        } catch (NoResultException noResultException) {
            log.debug("Exception while executing CustomizationDao.getCustomizationBySku()", noResultException);
            return customization;
        }

    }

    public Customization getCustomizationBySku(String skuCode) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select c from Customization c where c.skuCode=:sku");
        createQuery.setParameter("sku", skuCode);
        createQuery.setMaxResults(1);
        try {
            Customization custom = (Customization) createQuery.getSingleResult();
            return custom;
        } catch (NoResultException noResultException) {
            log.debug("Exception while executing CustomizationDao.getCustomizationBySku()", noResultException);
            return null;
        }
    }

    @Cacheable(cacheName="customizationListBySku")
    public List<Customization> getCustomizationListBySkuGroupByCategory(Set<String> skuCodeList) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select c from Customization c where c.skuCode in (:skuCodeList)");
        createQuery.setParameter("skuCodeList", skuCodeList);
        final List<Customization> resultList = createQuery.getResultList();
        Collections.sort(resultList , new Comparator<Customization>() {
            @Override
            public int compare(Customization o1, Customization o2) {
                return o1.getCategory().compareTo(o2.getCategory());
            }
        });
        int count = 1;
        for (Customization customization : resultList) {
            log.info("ReadyToShip Display Order:{}.) [{}] [{}]", (count++), customization.getSkuCode(), customization.getCategory());

        }
        return resultList;
    }


    public void deleteFromTable(String table,Customization customization){
    	String query = "delete from "+table+" t where t.customization=:customization";
    	Query createQuery = getEntityManagerWithoutFilter().createQuery(query);
    	createQuery.setParameter("customization", customization);
    	createQuery.executeUpdate();
    }

    public int markDerivedCustomizationAsActive(Long parentCustomizationId) {
        Query createNamedQuery = getEntityManagerWithoutFilter().createNativeQuery(changeDerivedCustomizationStatus);
        createNamedQuery.setParameter("customizationId", parentCustomizationId);
        createNamedQuery.setParameter("isactive", 1);
        return createNamedQuery.executeUpdate();

    }

    public int markDerivedCustomizationAsInActive(Long parentCustomizationId) {
        Query createNamedQuery = getEntityManagerWithoutFilter().createNativeQuery(changeDerivedCustomizationStatus);
        createNamedQuery.setParameter("customizationId", parentCustomizationId);
        createNamedQuery.setParameter("isactive", 0);
        return createNamedQuery.executeUpdate();
    }

    public int markAllCustomizationDisabled(long designId) {
        Query createNamedQuery = getEntityManagerWithoutFilter().createNativeQuery(changeAllCustomizationStatus);
        createNamedQuery.setParameter("designId", designId);
        createNamedQuery.setParameter("isactive", 0);
        return createNamedQuery.executeUpdate();
    }

    public int markAllCustomizationEnabled(Long designId) {
        Query createNamedQuery = getEntityManagerWithoutFilter().createNativeQuery(changeAllCustomizationStatus);
        createNamedQuery.setParameter("designId", designId);
        createNamedQuery.setParameter("isactive", 1);
        return createNamedQuery.executeUpdate();    }

}
