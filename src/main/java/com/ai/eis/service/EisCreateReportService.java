package com.ai.eis.service;

import com.ai.eis.model.EisCreateReports;


import java.util.List;
import java.util.Map;

public interface EisCreateReportService {
    int insert(EisCreateReports report);
    List<EisCreateReports> queryByCondition(Map<String, String> map);
    EisCreateReports queryById(Integer id);
    int update(EisCreateReports report);

    int deleteByPrimaryKey(Integer id);
}
