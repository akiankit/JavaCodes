package com.bluestone.app.search.tag.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="tagseo")
@NamedQueries({
        @NamedQuery(name = "TagSEO.getOverrideDetails", 
                query = "select ts from TagSEO ts join ts.tags t where t.name in (:tags) and ts.id in " +
                		"( select ts2.id from TagSEO ts2 join ts2.tags t2 group by ts2 having count(t2)=:tag_count) group by ts having count(t)=:tag_count")
})
public class TagSEO extends BaseEntity { 
		
	private static final long serialVersionUID = 1L;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@BatchSize(size=50)
	private Set<Tag> tags;
	
	@Column
	private String h1PageTitle;
	
	@Column
	private String htmlPageTitle;
	
	@Column
	private String metaKeywords;
	
	@Column
	private String metaDescription;
	
	@Column(columnDefinition="text")
	private String summaryTextHeader;
	
    public Set<Tag> getTags() {
        return tags;
    }
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    public String getH1PageTitle() {
        return h1PageTitle;
    }
    public void setH1PageTitle(String h1PageTitle) {
        this.h1PageTitle = h1PageTitle;
    }
    public String getHtmlPageTitle() {
        return htmlPageTitle;
    }
    public void setHtmlPageTitle(String htmlPageTitle) {
        this.htmlPageTitle = htmlPageTitle;
    }
    public String getMetaKeywords() {
        return metaKeywords;
    }
    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }
    public String getMetaDescription() {
        return metaDescription;
    }
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }
    public String getSummaryTextHeader() {
        return summaryTextHeader;
    }
    public void setSummaryTextHeader(String summaryTextHeader) {
        this.summaryTextHeader = summaryTextHeader;
    }
	
}
