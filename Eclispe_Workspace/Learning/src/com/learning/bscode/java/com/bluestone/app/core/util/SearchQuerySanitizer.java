package com.bluestone.app.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Rahul Agrawal
 *         Date: 3/4/13
 */
public class SearchQuerySanitizer {

    public static List<String> getCleanSearchTerms(String searchQuery) {
        searchQuery = StringUtils.trim(searchQuery);
        List<String> cleanSearchTerms = new ArrayList<String>();

        if (StringUtils.isNotBlank(searchQuery)) {
            String[] searchTerms = searchQuery.split("\\s+");
            for (int j = 0; j < searchTerms.length; j++) {
                String currentTerm = searchTerms[j].replaceAll("[^a-zA-Z0-9]", " ").trim();
                if (StringUtils.isNotBlank(currentTerm)) {
                    cleanSearchTerms.add(currentTerm);
                }
            }
        }
        return cleanSearchTerms;
    }
}
