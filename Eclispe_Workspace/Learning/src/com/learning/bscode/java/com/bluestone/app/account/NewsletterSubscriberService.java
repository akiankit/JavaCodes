package com.bluestone.app.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.dao.NewsletterSubscriberDao;
import com.bluestone.app.account.model.NewsletterSubscriber;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class NewsletterSubscriberService {

    private static final Logger log = LoggerFactory.getLogger(NewsletterSubscriberService.class);
	
	@Autowired
	private NewsletterSubscriberDao subscriberDao;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void addSubscriber(NewsletterSubscriber subscriber){
        log.debug("NewsletterSubscriberService.addSubscriber() ;[{}]", subscriber);
		subscriberDao.addSubscriber(subscriber);
	}
	
	public NewsletterSubscriber getSubscriberByEmailId(String email) {
        log.debug("NewsletterSubscriberService.getSubscriberByEmailId = [{}]", email);
		NewsletterSubscriber existingSubscriber = subscriberDao.getSubscriberByEmail(email);
		return existingSubscriber;
	}

}
