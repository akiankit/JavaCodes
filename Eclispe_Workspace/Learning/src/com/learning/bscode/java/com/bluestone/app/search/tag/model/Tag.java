package com.bluestone.app.search.tag.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.bluestone.app.admin.model.TagFilter;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.design.model.Design;

@Audited
@Entity
@Table(name="tag",uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
@NamedQueries({
    @NamedQuery(name="Tag.getTags", query="select DISTINCT t from Tag t where t.name in (:tags)"),
    @NamedQuery(name="Tag.getByDesignIds", query="select t from Tag t join t.design d where d.id in (:dids)")
})
 
public class Tag extends BaseEntity implements Comparable<Tag>{
		
	private static final long serialVersionUID = 1L;
	
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Column(name = "name",nullable=false)
	private String name;
	
	@IndexedEmbedded
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="tag_category_id",nullable=true)
	private TagCategory tagCategory;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	@LazyCollection(LazyCollectionOption.TRUE)
	@JoinTable(name = "design_tag", joinColumns = { @JoinColumn(name = "tag_id") }, inverseJoinColumns = @JoinColumn(name = "design_id"))
	@BatchSize(size=100)
	private Set<Design> design;
	
	@Transient
	private boolean selected=false;
	
	@Transient
	private String Url;;
	
	@Column(name = "isDerived",columnDefinition="bit(1) default false",nullable=false)
	private boolean isDerived;
	
	@Transient
	private int tagCount=0;
	
	@Column(name = "urlPriority",nullable = true)
	private int urlPriority=0;
	
	private Integer textPriority=0;
	
	@Column(name = "is_no_index",columnDefinition="tinyint(1) default false",nullable=false)
	private short isNoIndex;
	
    public short getIsNoIndex() {
        return isNoIndex;
    }

    public void setIsNoIndex(short isNoIndex) {
        this.isNoIndex = isNoIndex;
    }

    public Integer getTextPriority() {
        return textPriority;
    }

    public void setTextPriority(Integer textPriority) {
        this.textPriority = textPriority;
    }

    @OneToOne(mappedBy="tag",cascade=CascadeType.ALL)
	private TagFilter tagFilter;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TagCategory getTagCategory() {
		return tagCategory;
	}

	public void setTagCategory(TagCategory tagCategory) {
		this.tagCategory = tagCategory;
	}

	public Set<Design> getDesign() {
		return design;
	}

	public void setDesign(Set<Design> design) {
		this.design = design;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getUrlPriority() {
		return urlPriority;
	}

	public void setUrlPriority(int urlPriority) {
		this.urlPriority = urlPriority;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	@Override
	public int compareTo(Tag o) {
		if(this.equals(o)) {
			return 0; 
		}
		int diff = this.urlPriority - o.urlPriority;
		if(diff == 0) {
			return (int)(this.getId() - o.getId());
		}
		return diff;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((tagCategory == null) ? 0 : tagCategory.hashCode());
		result = prime * result + urlPriority;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Tag))
			return false;
		Tag other = (Tag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tagCategory == null) {
			if (other.tagCategory != null)
				return false;
		} else if (!tagCategory.equals(other.tagCategory))
			return false;
		if (urlPriority != other.urlPriority)
			return false;
		return true;
	}

	public int getTagCount() {
		return tagCount;
	}

	public void setTagCount(int tagCount) {
		this.tagCount = tagCount;
	}

	public boolean isDerived() {
		return isDerived;
	}

	public void setDerived(boolean isDerived) {
		this.isDerived = isDerived;
	}

    public TagFilter getTagFilter() {
        return tagFilter;
    }

    public void setTagFilter(TagFilter tagFilter) {
        this.tagFilter = tagFilter;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[TagName=").append(getName());

        TagCategory tagCategory = getTagCategory();
        if(tagCategory !=null){
            stringBuilder.append(", TagCategoryName=").append(tagCategory.getName());
        }
        stringBuilder.append(" ]");
        return stringBuilder.toString();
    }

    public void addDesign(Design designTobeAdded) {
        if(design == null) {
            design = new HashSet<Design>();
        }
        design.add(designTobeAdded);
    }
}
