package com.bluestone.app.rmi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.dao.BaseDao;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class RMIService {

	private static final Logger log = LoggerFactory.getLogger(RMIService.class);

	@Autowired
	private BaseDao baseDao;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<MeasurementUnit> getAllMeasurementUnits() {
		return baseDao.list(MeasurementUnit.class); 
	}

	public void saveMeasurementUnit(MeasurementUnit measurementUnit) {
		baseDao.create(measurementUnit);
	}

	public void deleteMeasurementUnit(Long measurementUnitId) {
		//check for fk deps
		baseDao.remove(MeasurementUnit.class,measurementUnitId);
	}
}
