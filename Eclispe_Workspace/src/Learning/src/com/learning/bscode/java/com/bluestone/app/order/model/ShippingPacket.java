package com.bluestone.app.order.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.uniware.OrderItemStatusDetail.ShippingDetails;

@Audited
@Entity
@Table(name = "shipping_packet")
@NamedQuery(name = "ShippingPacket.findFromShippingPackageCode", query = "Select sp from ShippingPacket sp where sp.shipmentPackageCode =:shipmentCode")
public class ShippingPacket extends BaseEntity {
    
    private static final long               serialVersionUID          = 7688148008490227490L;
    
    @Column(nullable = false, unique = true)
    private String                          shipmentPackageCode;
    
    private String                          shippingPackageType       = "DEFAULT";
    
    private String                          shippingProvider;
    
    private String                          trackingNumber;
    
    private Date                            packetCreationDate;
    
    private Date                            packetDispatchDate;
    
    private Date                            packetDeliveredDate;
    
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "shippingPacket", fetch=FetchType.EAGER)
    private List<ShippingPacketHistory> shippingPacketHistoryList = new ArrayList<ShippingPacketHistory>();
    
    @OneToMany(mappedBy = "shippingPacket", fetch=FetchType.LAZY)
    private List<ShippingItem> shippingPacketItemList = new ArrayList<ShippingItem>();
    
    // Default constructor needed by hibernate
    public ShippingPacket() {
    }
    
    public ShippingPacket(String shipmentPackgaeCode, ShippingDetails shippingDetails) {
        this.shipmentPackageCode = shipmentPackgaeCode;
        this.shippingProvider = shippingDetails.getShippingProvider();
        this.shippingPackageType = shippingDetails.getShippingPackageType();
        this.trackingNumber = shippingDetails.getTrackingNumber();
        
        Calendar createdOn = shippingDetails.getCreatedOn();
        if (createdOn != null) {
            this.packetCreationDate = createdOn.getTime();
        }
        
        Calendar deliveredOn = shippingDetails.getDeliveredOn();
        if (deliveredOn != null) {
            this.packetDeliveredDate = deliveredOn.getTime();
        }
        
        Calendar dispatchedOn = shippingDetails.getDispatchedOn();
        if (dispatchedOn != null) {
            this.packetDispatchDate = dispatchedOn.getTime();
        }
    }
    
    public String getShippingPackageType() {
        return shippingPackageType;
    }
    
    public void setShippingPackageType(String shippingPackageType) {
        this.shippingPackageType = shippingPackageType;
    }
    
    public String getShippingProvider() {
        return shippingProvider;
    }
    
    public void setShippingProvider(String shippingProvider) {
        this.shippingProvider = shippingProvider;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    public Date getPacketCreationDate() {
        return packetCreationDate;
    }
    
    public void setPacketCreationDate(Date packetCreationDate) {
        this.packetCreationDate = packetCreationDate;
    }
    
    public Date getPacketDispatchDate() {
        return packetDispatchDate;
    }
    
    public void setPacketDispatchDate(Date packetDispatchDate) {
        this.packetDispatchDate = packetDispatchDate;
    }
    
    public Date getPacketDeliveredDate() {
        return packetDeliveredDate;
    }
    
    public void setPacketDeliveredDate(Date packetDeliveredDate) {
        this.packetDeliveredDate = packetDeliveredDate;
    }
    
    public String getShipmentPackageCode() {
        return shipmentPackageCode;
    }
    
    public void setShipmentPackageCode(String shipmentPackageCode) {
        this.shipmentPackageCode = shipmentPackageCode;
    }

    public List<ShippingPacketHistory> getShippingPacketHistoryList() {
        return shippingPacketHistoryList;
    }

    public void setShippingPacketHistoryList(List<ShippingPacketHistory> shippingPacketHistoryList) {
        this.shippingPacketHistoryList = shippingPacketHistoryList;
    }
    
    public ShippingPacketHistory getLatestShippingPacketHistory() {
        if(shippingPacketHistoryList.size() > 0) {
            return shippingPacketHistoryList.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("ShippingPacket");
        sb.append("{\n\t ShipmentCode=").append(getShipmentPackageCode()).append("\n");
        sb.append("\t PacketCreationdate=").append(DateTimeUtil.getSimpleDateAndTime(packetCreationDate)).append("\n");
        sb.append("\t PacketDispatchDate=").append(DateTimeUtil.getSimpleDateAndTime(packetDispatchDate)).append("\n");
        sb.append("\t PacketDeliveredDate=").append(DateTimeUtil.getSimpleDateAndTime(packetDeliveredDate)).append("\n");
        sb.append("\t ShippingPackageType=").append(shippingPackageType).append("\n");
        sb.append("\t ShippingProvider=").append(shippingProvider).append("\n");
        sb.append("\t TrackingNumber=").append(trackingNumber).append("\n");
        sb.append("\n}");
        return sb.toString();
    }

    public List<ShippingItem> getShippingPacketItemList() {
        return shippingPacketItemList;
    }

    public void setShippingPacketItemList(List<ShippingItem> shippingPacketItemList) {
        this.shippingPacketItemList = shippingPacketItemList;
    }
    
}
