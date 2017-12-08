package com.bluestone.app.search.tag.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Field;

import com.bluestone.app.core.model.BaseEntity;


@Audited
@Entity
@Table(name = "tag_category",uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class TagCategory extends BaseEntity implements Comparable<TagCategory>{

	private static final long serialVersionUID = 1L;

	@Field
	@Column(name = "name",nullable=false)
	private String name;
	
	@Column(name = "priority")
	private int priority;

	@OneToMany(mappedBy = "tagCategory",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@OrderBy(value = "name asc")
	@BatchSize(size=100)
	private Set<Tag> tags;
	
	@Transient
	private boolean selected=false;
	
	@Transient
    private List<Tag> sortedByCountTags;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	@Override
	public int compareTo(TagCategory o) {
		if(this.equals(o)) {
			return 0; 
		}
		int diff = this.priority - o.priority;
		if(diff == 0) {
			return (int)(this.getId() - o.getId());
		}
		return diff;
		
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + priority;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagCategory other = (TagCategory) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}

    public List<Tag> getSortedByCountTags() {
        return sortedByCountTags;
    }

    public void setSortedByCountTags(List<Tag> sortedByCountTags) {
        this.sortedByCountTags = sortedByCountTags;
    }

    @Override
    public String toString() {
        return "TagCategory [name=" + name + ", priority=" + priority + ", selected=" + selected + "]";
    }
    
    

}
