package com.bluestone.app.design.service;

import java.util.Comparator;

import com.bluestone.app.search.tag.model.Tag;

public class TagTextPriorityComparator implements Comparator<Tag> {

    @Override
    public int compare(Tag o1, Tag o2) {
		int diff = o1.getTextPriority() - o2.getTextPriority();
		if(diff == 0) {
			return (int)(o1.getId() - o2.getId());
		}
		return diff;
    }
    
}
