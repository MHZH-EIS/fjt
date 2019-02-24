package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisCreateReportsMapper;
import com.ai.eis.model.EisCreateReports;
import com.ai.eis.service.EisCreateReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("EisCreateReportService")
public class EisCreateReportServiceImpl implements EisCreateReportService {
    @Autowired
    private EisCreateReportsMapper eisCreateReportsMapper;


    @Override
    public int insert(EisCreateReports report) {
        return eisCreateReportsMapper.insertSelective(report);
    }

    @Override
    public List<EisCreateReports> queryByCondition(Map<String, String> map) {
        return eisCreateReportsMapper.queryByCondition(map);
    }

    @Override
    public EisCreateReports queryById(Integer id) {
        return eisCreateReportsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(EisCreateReports report) {
        return eisCreateReportsMapper.updateByPrimaryKeySelective(report);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return eisCreateReportsMapper.deleteByPrimaryKey(id);
    }
}
