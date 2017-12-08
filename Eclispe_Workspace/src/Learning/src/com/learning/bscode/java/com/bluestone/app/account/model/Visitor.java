package com.bluestone.app.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "visitor")
public class Visitor extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @Column(name = "ip_address")
    private String clientIp;

    @Column(name = "tag_id", nullable = false)
    private int tagId;

    public Visitor() {
    }

    public Visitor(String clientIp, int tagId) {
        this.clientIp = clientIp;
        this.tagId = tagId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ClientIp=").append(clientIp).append(" ");
        if (getCustomer() != null) {
            sb.append("Customer=").append(customer.getId());
        }
        sb.append(" tagId=").append(tagId);
        sb.append(" id=").append(getId()).append(" \n");
        return sb.toString();
    }
}
