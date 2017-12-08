package com.bluestone.app.geolocation;

import static com.bluestone.app.popup.PopUpConstant.EMPTY_STRING;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;

@Repository("ipcityDao")
public class IpCityDao extends BaseDao
{

    private static final Logger log = LoggerFactory.getLogger(IpCityDao.class);
    
    public String getCityFromIP(String ip)
    {
    	log.debug("IPCityDao.getCityFromIP(): for IP: "+ip);
    	EntityManager entityManager = getEntityManagerForActiveEntities();
    	Query query = entityManager.createNativeQuery("SELECT city_name FROM ip_city WHERE INET_ATON(?) BETWEEN start_ip AND end_ip LIMIT 1");
    	query.setParameter(1, ip);
    	 String city = null;
    	try {
    	    city = (String)query.getSingleResult();
    	} catch (NoResultException e) {
    	    city = null;
    	}
    	log.debug("IPCityDao.getCityFromIP(): for IP: "+ip+" city is: "+city);
    	return city == null ? EMPTY_STRING : city;
    }
}
