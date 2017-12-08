package com.bluestone.app.design.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "parentchildcategory")
public class ParentChildCategory extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_category_id", nullable = false)
    private DesignCategory    parentCategory;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_category_id", nullable = false)
    private DesignCategory    childCategory;
    
    public DesignCategory getParentCategory() {
        return parentCategory;
    }
    
    public void setParentCategory(DesignCategory parentCategory) {
        this.parentCategory = parentCategory;
    }
    
    public DesignCategory getChildCategory() {
        return childCategory;
    }
    
    public void setChildCategory(DesignCategory childCategory) {
        this.childCategory = childCategory;
    }
    
}
