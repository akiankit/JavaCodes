package com.bluestone.app.uniware;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unicommerce.uniware.services.CustomFieldsCustomField;

/**
 * @author Rahul Agrawal
 *         Date: 10/22/12
 */
class CustomFieldBuilder {
    static CustomFieldsCustomField[] createCustomFields(Map<String, String> customFields) {
        List<CustomFieldsCustomField> list = new LinkedList<CustomFieldsCustomField>();
        if (customFields != null) {
            Set<String> keys = customFields.keySet();
            for (String eachKey : keys) {
                String value = customFields.get(eachKey);
                list.add(new CustomFieldsCustomField(eachKey, value));
            }
        }
        return list.toArray(new CustomFieldsCustomField[list.size()]);
    }
}
