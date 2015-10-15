package com.bluestone.app.admin.model;

import org.apache.commons.lang.StringUtils;

import com.bluestone.app.core.util.ApplicationProperties;

public class ListFilterCriteria {

	// pageNumber
	private int p = 1;

	private String valueToSearch = "";

	protected String column = "";

	protected String sortBy = "";

	private int itemsPerPage = Integer.parseInt(ApplicationProperties.getProperty("adminpage.itemsPerPage"));

	private boolean sortByMode = true;
	
	private boolean prefixSearch = false;

	private String query;


	public int getP() {
		return p==0 ? 1: p;
	}

	public void setP(int p) {
		this.p = (p==0 ? 1 :p);
	}

	public String getValueToSearch() {
		return StringUtils.isBlank(valueToSearch) ? "" : valueToSearch.trim();
	}

	public void setValueToSearch(String valueToSearch) {
		this.valueToSearch = StringUtils.isBlank(valueToSearch) ? "" : valueToSearch.trim();
	}

	public String getColumn() {
		return StringUtils.isBlank(column) ? "id" : column;
	}

	public void setColumn(String column) {
		this.column = StringUtils.isBlank(column) ? "id" : column;
	}

	public String getSortBy() {
		return StringUtils.isBlank(sortBy) ? "id" : sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = StringUtils.isBlank(sortBy) ? "id" : sortBy;
	}

	public int getItemsPerPage() {
		return itemsPerPage==0 ? Integer.parseInt(ApplicationProperties.getProperty("adminpage.itemsPerPage")) : itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage==0 ? Integer.parseInt(ApplicationProperties.getProperty("adminpage.itemsPerPage")) : itemsPerPage;
	}

	public boolean isSortByMode() {
		return sortByMode;
	}

	public void setSortByMode(boolean sortByMode) {
		this.sortByMode = sortByMode;
	}

	public String getSortOrder() {
		return (this.sortByMode == false ? "ASC" : "DESC");
	}

	public boolean getPrefixSearch() {
		return prefixSearch;
	}

	public void setPrefixSearch(boolean prefixSearch) {
		this.prefixSearch = prefixSearch;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListFilterCriteria [p=");
		builder.append(p);
		builder.append(", valueToSearch=");
		builder.append(valueToSearch);
		builder.append(", column=");
		builder.append(column);
		builder.append(", sortBy=");
		builder.append(sortBy);
		builder.append(", itemsPerPage=");
		builder.append(itemsPerPage);
		builder.append(", sortByMode=");
		builder.append(sortByMode);
		builder.append(", prefixSearch=");
		builder.append(prefixSearch);
		builder.append(", query=");
		builder.append(query);
		builder.append("]");
		return builder.toString();
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}

}
