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
        return standardMapper.queryByCondition(map);
    }

    @Override
    public void add(EisStandard standard) {
        standardMapper.insert(standard);
    }

    @Override
    public void delete(Integer id) {
        standardMapper.deleteByPrimaryKey(id);
    }

    @Override
    public EisStandard queryById(Integer id) {
        return standardMapper.selectByPrimaryKey(id);
    }

    @Override
    public List <Map <String, Object>> queryByProject(Integer projectId) {
        return standardMapper.queryByProject(projectId);
    }

	@Override
	public int update(EisStandard standard) {
		return standardMapper.updateByPrimaryKeySelective(standard);
	}
}
