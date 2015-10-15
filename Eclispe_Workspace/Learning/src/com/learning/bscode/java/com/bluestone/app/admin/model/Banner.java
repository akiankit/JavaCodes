package com.bluestone.app.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "banner")
public class Banner extends BaseEntity {
    
    private static final long    serialVersionUID = 1L;
    
    public enum PageTypeForBanner {
        HOME("home");
        
        private String pageName;
        
        private PageTypeForBanner(String pageName){
        	this.pageName = pageName; 
        }
        
    }
    
    @Column(name = "page_type", columnDefinition = "varchar(64)", nullable = false,unique=true)
    @Enumerated(EnumType.STRING)
    private PageTypeForBanner pageType;
    
    @Column(columnDefinition="text",name="htmlContent")
	private String htmlContent;

	public PageTypeForBanner getPageType() {
		return pageType;
	}
	
	public String getPageName(){
		return this.pageType.pageName;
	}

	public void setPageType(PageTypeForBanner pageType) {
		this.pageType = pageType;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
    
    
}
