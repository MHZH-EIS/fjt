package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisStandardMapper;
import com.ai.eis.model.EisStandard;
import com.ai.eis.service.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "StandardService")
public class StandardServiceImpl implements StandardService {

    @Autowired
    private EisStandardMapper standardMapper;

    @Override
    public List <EisStandard> list(Map <String, String> map) {
        return standardMapper.list(map);
    }
}
