package com.bluestone.app.account.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.bluestone.app.core.model.BaseEntity;

@Entity
@Table(name = "visitor_history")
public class VisitorHistory extends BaseEntity{

	private static final long serialVersionUID = 1L;	
	
	@ManyToOne
    @JoinColumn(name = "visitor_id", nullable = false)
    private Visitor visitor;
	
	private final String referrer;
	
	@Column(name="utm_source")
	private final String utmSource;
	
	@Column(name="utm_medium")
	private final String utmMedium;
	
	@Column(name="utm_campaign")
	private final String utmCampaign;
	
	@Column(name="utm_term")
	private final String utmTerm;
	
	@Column(name="utm_content")
	private final String utmContent;	
		
	private final String keyword;
	
	@Column(name="matched_search_query")
	private final String matchedSearchQuery;	

	public VisitorHistory(Visitor visitor, String referrer, String utmSource,
			String utmMedium, String utmCampaign, String utmTerm,
			String utmContent, String keyword, String matchedSearchQuery) {		
		this.visitor = visitor;
		this.referrer = StringUtils.substring(referrer, 0, 255);
		this.utmSource = StringUtils.substring(utmSource, 0, 255);
		this.utmMedium = StringUtils.substring(utmMedium, 0, 255);
		this.utmCampaign = StringUtils.substring(utmCampaign, 0, 255);
		this.utmTerm = StringUtils.substring(utmTerm, 0, 255);
		this.utmContent = StringUtils.substring(utmContent, 0, 255);
		this.keyword = StringUtils.substring(keyword, 0, 255);
		this.matchedSearchQuery = StringUtils.substring(matchedSearchQuery, 0, 255);
	}

    public VisitorHistory(Visitor visitor, Map<String,String> paramMap){
        this(visitor,
             paramMap.get("referrer"),
             paramMap.get("utmSource"),
             paramMap.get("utmMedium"),
             paramMap.get("utmCampaign"),
             paramMap.get("utmTerm"),
             paramMap.get("utmContent"),
             paramMap.get("keyword"),
             paramMap.get("matchedSearchQuery"));

    }

	public Visitor getVisitor() {
		return visitor;
	}

	public String getReferrer() {
		return referrer;
	}

	public String getUtmSource() {
		return utmSource;
	}

	public String getUtmMedium() {
		return utmMedium;
	}

	public String getUtmCampaign() {
		return utmCampaign;
	}

	public String getUtmTerm() {
		return utmTerm;
	}

	public String getUtmContent() {
		return utmContent;
	}

	public String getKeyword() {
		return keyword;
	}	

	public String getMatchedSearchQuery() {
		return matchedSearchQuery;
	}
	
}
