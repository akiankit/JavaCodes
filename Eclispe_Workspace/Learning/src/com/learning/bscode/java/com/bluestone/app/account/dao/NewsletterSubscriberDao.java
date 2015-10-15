package com.bluestone.app.account.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.NewsletterSubscriber;
import com.bluestone.app.core.dao.BaseDao;

@Repository("newsletterSubscriberDao")
public class NewsletterSubscriberDao extends BaseDao {
	
	private static final Logger log = LoggerFactory.getLogger(NewsletterSubscriberDao.class);
	
	public void addSubscriber(NewsletterSubscriber subscriber) {
        getEntityManagerForActiveEntities().persist(subscriber);
        log.debug("NewsletterSubscriberDao.addSubscriber() Successful for [{}]", subscriber);
    }
	
	public NewsletterSubscriber getSubscriberByEmail(String email) {
        log.debug("NewsletterSubscriberDao.getSubscriberByEmail(): [{}]", email);
        Query getByEmailIdQuery = getEntityManagerWithoutFilter().createNamedQuery("NewsletterSubscriber.findByEmailId");
        getByEmailIdQuery.setParameter("email", email);
        List<NewsletterSubscriber> subscriberList = getByEmailIdQuery.getResultList();
        if (subscriberList.size() == 0) {
            return null;
        } else {
            return subscriberList.get(0);
        }
    }

}
