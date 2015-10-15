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

public class PriorityFilter {
    
    private Integer priority;
    
    /**
     * injected parameter
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        key.addParameter(priority);
        return key;
    }
    
    @Factory
    public Filter getFilter() {
        Query query = new TermQuery(new Term("priority", priority.toString()));
        return new CachingWrapperFilter(new QueryWrapperFilter(query));
    }
    
}
