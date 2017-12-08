package com.bluestone.app.design.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.util.Constants;

/**
 * @author Rahul Agrawal
 *         Date: 2/25/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class PaginationService {

    private static final Logger log = LoggerFactory.getLogger(PaginationService.class);

    // To compute the data for pagination bar given total pages and current page
    // number
    public Map<Object, Object> getPagination(int currentPageNumber, int totalPages, String baseURL) {

        String prevLink = "";
        String nextLink = "";
        ArrayList<HashMap<Object, Object>> pagination = new ArrayList<HashMap<Object, Object>>();
        int firstPageNumber = 1;

        if (totalPages > 1 && totalPages >= currentPageNumber && currentPageNumber >= 1) {
            if (currentPageNumber - firstPageNumber < Constants.PAGINATION_CONST) {
                // Show all pages before currentPageNumber
                for (int i = firstPageNumber; i < currentPageNumber; i++) {
                    addToPaginationData(pagination, baseURL, i);
                }
            } else {
                // show 1 ... c-2 c-1
                addToPaginationData(pagination, baseURL, firstPageNumber);
                addToPaginationData(pagination, Constants.ELLIPSIS, "");
                for (int i = currentPageNumber - 2; i < currentPageNumber; i++) {
                    addToPaginationData(pagination, baseURL, i);
                }
            }
            // at this point, pages before currentPageNumber have been taken
            // care of, now add CurrentPageNumber
            int positionOfCurrentPage = 0;// this holds only if firstPageNumber
            // = currentPageNumber
            if (currentPageNumber != firstPageNumber) {
                positionOfCurrentPage = pagination.size();
                prevLink = getURLForPageNumber(baseURL, currentPageNumber - 1);
            }

            HashMap<Object, Object> map3 = new HashMap<Object, Object>();
            map3.put(Integer.toString(currentPageNumber), "");
            pagination.add(positionOfCurrentPage, map3);

            if (totalPages - currentPageNumber < Constants.PAGINATION_CONST) {
                // Show all pages after currentPageNumber
                for (int i = currentPageNumber + 1; i <= totalPages; i++) {
                    addToPaginationData(pagination, baseURL, i);
                }
            } else {
                // show c+1 c+2 ... L
                for (int i = currentPageNumber + 1; i <= currentPageNumber + 2; i++) {
                    addToPaginationData(pagination, baseURL, i);
                }
                // taking care of right ellipsis
                addToPaginationData(pagination, Constants.ELLIPSIS, "");
                addToPaginationData(pagination, baseURL, totalPages);
            }
        }
        if (currentPageNumber < totalPages) {
            nextLink = getURLForPageNumber(baseURL, currentPageNumber + 1);
        }
        if (currentPageNumber == totalPages) {
            nextLink = "false";
        }
        Map<Object, Object> returnData = new HashMap<Object, Object>();
        returnData.put("prevLink", prevLink);
        returnData.put("nextLink", nextLink);
        returnData.put("paginationData", pagination);
        return returnData;
    }

    private void addToPaginationData(ArrayList<HashMap<Object, Object>> pagination, String key, String i) {
        HashMap<Object, Object> map1 = new HashMap<Object, Object>();
        map1.put(key, i);
        pagination.add(map1);
    }

    // in case we need URL which is not blank
    private void addToPaginationData(ArrayList<HashMap<Object, Object>> pagination, String baseURL, int i) {
        String key = Integer.toString(i);
        String value = getURLForPageNumber(baseURL, i);
        addToPaginationData(pagination, key, value);
    }

    private String getURLForPageNumber(String baseURL, int i) {
        if (i == 1) {
            return baseURL;
        }
        return baseURL + "&p=" + Integer.toString(i);
    }

    private String getNewURLQueryString(Map<String, String> queryStringMap, String urlQueryString) {
        if (urlQueryString != null) {
            String[] queryParts = urlQueryString.split(Constants.URL_QUERY_STRING_DELIMITER);
            for (String queryPart : queryParts) {
                String[] paramAndValue = queryPart.split(Constants.EQUALS_SIGN);
                queryStringMap.put(paramAndValue[0], paramAndValue.length > 1 ? paramAndValue[1] : "");
            }
        }

        if (queryStringMap.containsKey(Constants.PAGE_NUMBER_PARAM)) {
            queryStringMap.remove(Constants.PAGE_NUMBER_PARAM);
        }
        String newQueryString = "";
        for (Map.Entry<String, String> entry : queryStringMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.length() > 0) {
                newQueryString = newQueryString
                                 + Constants.URL_QUERY_STRING_DELIMITER
                                 + key + Constants.EQUALS_SIGN
                                 + value;
            }
        }

        if (newQueryString.length() > 1 && newQueryString.charAt(0) == '&') {
            newQueryString = newQueryString.substring(1);
        }

        if (newQueryString.length() > 0) {
            newQueryString = Constants.QUESTION_MARK + newQueryString;
        }
        return newQueryString;
    }


}
