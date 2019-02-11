package com.ai.eis.service;

import com.ai.eis.model.EisStItem;

import java.util.List;
import java.util.Map;

public interface SItemService {

    List <EisStItem> queryByCondition(Map <String, String> map);

    void add(EisStItem item);

    void deleteByStandardId(Integer id);

    void deleteByItemId(Integer id);
    
    EisStItem queryById(Integer id);
    
    int update(EisStItem item);
}
