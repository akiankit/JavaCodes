package com.bluestone.app.design.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="design_subcategory")
public class DesignSubCategory extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="design_category_id",nullable=false, insertable=false, updatable=false)
	private DesignCategory designCategory;
	
	@Column(name="sub_category_name")
	private String subcategoryName;

	public DesignCategory getDesignCategory() {
		return designCategory;
	}

	public void setDesignCategory(DesignCategory designCategory) {
		this.designCategory = designCategory;
	}

	public String getSubcategoryName() {
		return subcategoryName;
	}

	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DesignSubCategory");
        sb.append("{subcategoryName='").append(subcategoryName).append('\'');
        sb.append('}');
        sb.append("\n");
        sb.append(super.toString());
        return sb.toString();
    }
}
