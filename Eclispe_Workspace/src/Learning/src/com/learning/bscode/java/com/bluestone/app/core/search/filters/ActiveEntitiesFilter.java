package com.bluestone.app.core.search.filters;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;
import org.hibernate.search.filter.impl.CachingWrapperFilter;

public class ActiveEntitiesFilter {
    
    private short isActive;
    
    /**
     * injected parameter
     */
    public void setIsActive(short isActive) {
        this.isActive = isActive;
    }
    
    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        key.addParameter(isActive);
        return key;
    }
    
    @Factory
    public Filter getFilter() {
        Query query = new TermQuery(new Term("isActive", String.valueOf(isActive)));
        return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }
    
}
