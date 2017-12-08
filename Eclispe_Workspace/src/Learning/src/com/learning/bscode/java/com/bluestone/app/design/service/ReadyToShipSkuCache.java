package com.bluestone.app.design.service;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.googlecode.ehcache.annotations.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.dao.ReadyToShipDao;
import com.bluestone.app.design.model.ReadyToShip;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ReadyToShipSkuCache {

    private static final Logger log = LoggerFactory.getLogger(ReadyToShipSkuCache.class);

    private static final String CACHE_NAME = "readyToShipProductsCache";

    @Autowired
    private ReadyToShipDao readyToShipDao;

    @Cacheable(cacheName = CACHE_NAME)
    public SortedSet<String> getReadyToShipSKUs() {
        SortedSet<String> treeSet = new TreeSet<String>(); // ordering by the sku codes
        List<ReadyToShip> readyToShipSKUs = readyToShipDao.getReadyToShipProductsSkus();
        if (readyToShipSKUs != null) {
            log.info("ReadyToShipSkuCache.getReadyToShipSKUs(): Count ={}", readyToShipSKUs.size());
            int counter = 1;
            for (ReadyToShip readyToShip : readyToShipSKUs) {
                log.info("ReadyToShipSkuCache: [{}] [{}]", counter++, readyToShip);
                treeSet.add(readyToShip.getSkuCode());
            }
        }
        return treeSet;
    }

    //@TriggersRemove(cacheName = {CACHE_NAME}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void put(ReadyToShip readyToShip) {
        readyToShipDao.create(readyToShip);
    }

    //@TriggersRemove(cacheName = {CACHE_NAME}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void remove(ReadyToShip readyToShip) {
        readyToShipDao.remove(readyToShip);
    }


}
