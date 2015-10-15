package com.bluestone.app.admin.dao;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.model.Customization;

@Repository("ReindexDao")
public class ReindexDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(ReindexDao.class);
    
    public synchronized void reindex() {
        log.info("reindex job started");
        reindex(Customization.class);
    }
    
    public synchronized void reindex(Class clazz) {
        log.info("reindex job started for entity {} ", clazz.getName());
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManagerForActiveEntities());
        try {
            fullTextEntityManager.createIndexer(clazz).startAndWait();
            log.info("reindex job ended for entity {} ", clazz.getName());
        } catch (InterruptedException e) {
            if(log.isErrorEnabled()) {
                log.error("Reindex job interrupted", e);
            }
        }
    }
}
