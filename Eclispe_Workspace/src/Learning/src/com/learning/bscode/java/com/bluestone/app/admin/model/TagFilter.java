package com.bluestone.app.admin.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.search.tag.model.Tag;

@Audited
@Entity
@Table(name="filter")
public class TagFilter extends BaseEntity {
	
	@IndexedEmbedded
	@OneToOne
	@JoinColumn(name="tag_id",nullable=false)
	private Tag tag;
	
	
	@Column(name = "rule", length=512)
	private String rule;
	
	@Transient
	private 	String[][] parsedRule;
	
	
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	
	public String[][] getParsedRule() {
		return parsedRule;
	}

	public void setParsedRule(String[][] parsedRule) {
		this.parsedRule = parsedRule;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	
	
}


