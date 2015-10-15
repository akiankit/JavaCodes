package com.bluestone.app.geolocation;

import static com.bluestone.app.popup.PopUpConstant.EMPTY_STRING;
import static com.bluestone.app.popup.PopUpConstant.PATTERN_IP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class IpCityService {
    
    private static final Logger log = LoggerFactory.getLogger(IpCityService.class);
    
    @Autowired
    private IpCityDao           ipCityDao;
    
    public String getCityForIP(String ip) {
        log.debug("IPCityService.getCityForIP(). for ip: " + ip);
        if (ip == null || ip.equals("")) {
            throw new IllegalArgumentException("IP can't be null or empty string.");
        }
        if (isValidIp(ip)) {
            String city = ipCityDao.getCityFromIP(ip);
            if (city == null || EMPTY_STRING.equals(city)) {
                return EMPTY_STRING;
            } else {
                return city;
            }
        } else {
            return EMPTY_STRING;
        }
    }
    
    private boolean isValidIp(String ip) {
        return PATTERN_IP.matcher(ip).matches();
    }
}
