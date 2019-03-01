package com.ai.eis.service;

import com.ai.eis.model.EisCreateReport;


import java.util.List;
import java.util.Map;

public interface EisCreateReportService {
    int insert(EisCreateReport report);
    List<EisCreateReport> queryByCondition(Map<String, String> map);
    EisCreateReport queryById(Integer id);
    int update(EisCreateReport report);

    int deleteByPrimaryKey(Integer id);
}
