package com.bluestone.app.ads.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluestone.app.core.model.BaseEntity;

@Entity
@Table(name = "ip_city")
public class IpCity extends BaseEntity
{
	private static final long serialVersionUID = 2770613892772827643L;
	
	@Column(name = "location_id")
	private int locationID;
	
	@Column(name="start_ip")
	private long startIp;
	
	@Column(name="end_ip")
	private long endIp;
	
	@Column(name="city_name")
	private String cityName;

	public int getLocationID() 
	{
		return locationID;
	}

	public long getStartIp() 
	{
		return startIp;
	}

	public long getEndIp() 
	{
		return endIp;
	}

	public String getCityName() 
	{
		return cityName;
	}

	public void setLocationID(int locationID)
	{
		this.locationID = locationID;
	}

	public void setStartIp(long startIp)
	{
		this.startIp = startIp;
	}

	public void setEndIp(long endIp) 
	{
		this.endIp = endIp;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("com.bluestone.app.adds.model.IPCity[");
		sb.append("locationID="+locationID);
		sb.append(", startIp="+startIp);
		sb.append(", endIp="+endIp);
		sb.append(", cityName="+cityName);
		sb.append("]");
		return sb.toString();
	}
}
