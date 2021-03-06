package com.friday.plandial.policy;

import com.friday.plandial.Category;
import com.friday.plandial.DialManager;

public class EditCategoryValidator implements ICategoryValidator{
    public static final int NAME_MIN_LENGTH = 1;
    public static final int NAME_MAX_LENGTH = 15;
  
    @Override
    public boolean validateName(String name) {
        return name != null && NAME_MIN_LENGTH <= name.length() && name.length() <= NAME_MAX_LENGTH;
    }
  
    @Override
    public boolean sameName(DialManager dialManager, String name, Category category) {
        Category sameNamedCategory = dialManager.getCategoryByName(name);
        return sameNamedCategory == null | sameNamedCategory == category;
    }
}
