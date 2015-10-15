package com.bluestone.app.design.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.Design;

@Repository("designDao")
public class DesignDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(DesignDao.class);

    @Autowired
    private CustomizationDao customizationDao;

    public List<Design> listDesigns() {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select d from Design d");
        List<Design> list = createQuery.getResultList();
        return list;
    }

    public void deleteDesignTag(long id) {
        Query createQuery = getEntityManagerWithoutFilter().createNativeQuery("delete from design_tag where design_id=:id");
        createQuery.setParameter("id", id);
        createQuery.executeUpdate();
    }

    public List<Design> getDesignList(List<Long> ids) {
        Query createQuery;
        if (ids != null) {
            createQuery = getEntityManagerWithoutFilter().createQuery("select d from Design d " +
                                                                      " join fetch d.designImageView " +
                                                                      " join fetch d.designMetalFamilies dmf " +
                                                                      " where d.id in (:ids)");
            createQuery.setParameter("ids", ids);

        } else {
            createQuery = getEntityManagerWithoutFilter().createQuery("select d from Design d " +
                                                                      " join fetch d.designImageView " +
                                                                      " join fetch d.designMetalFamilies dmf");
        }

        List<Design> list = createQuery.getResultList();

        return list;
    }

    public List<String> getDesignCodeListByCode(List<String> designCode) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select d.designCode from Design d where d.designCode in (:designCode)");
        createQuery.setParameter("designCode", designCode);
        return createQuery.getResultList();
    }

    public List<Design> getDesignListByCode(List<String> designCode) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select d from Design d where d.designCode in (:designCode)");
        createQuery.setParameter("designCode", designCode);
        return createQuery.getResultList();
    }

    public Design getDesignByCode(String designCode) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select d from Design d where d.designCode=:designCode");
        createQuery.setParameter("designCode", designCode);
        createQuery.setMaxResults(1);
        try {
            return (Design) createQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }
    public Map<Object, Object> getCatalogListAndTotalCount(int start,ListFilterCriteria filterCriteria, boolean forActive) {

		EntityManager entityManager =  getEntityManagerWithoutFilter();
		Query designCountQuery = entityManager.createQuery("select count(c.id) from Design c where c."
															+ filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() + "%'");

		Query designListQuery = entityManager.createQuery("select c from Design c where c." + filterCriteria.getColumn()
															+ " like '%" + filterCriteria.getValueToSearch() + "%' order by " 
															+ filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder());
		designListQuery.setFirstResult(start);
		designListQuery.setMaxResults(filterCriteria.getItemsPerPage());

		List<Design> designList = designListQuery.getResultList();
		Map<Object, Object> designDetails = new HashMap<Object, Object>();

		designDetails.put("list", designList);
		designDetails.put("totalCount", ((Long) designCountQuery.getSingleResult()).intValue());
		return designDetails;
	}
    
    public List<Design> listDesigns(int offset, int maxResults) {
        Query createQuery = getEntityManagerWithoutFilter().createQuery("select d from Design d");
        createQuery.setFirstResult(offset);
        createQuery.setMaxResults(maxResults);
        List<Design> list = createQuery.getResultList();
        return list;
    }

}
