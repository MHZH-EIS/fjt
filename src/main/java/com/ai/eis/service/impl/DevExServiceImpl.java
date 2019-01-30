package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisItemDevMapper;
import com.ai.eis.model.EisItemDev;
import com.ai.eis.service.DevExService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("DevExService")
public class DevExServiceImpl implements DevExService {

    @Autowired
    private EisItemDevMapper itemDevMapper;

    @Override
    public int insertSelective(EisItemDev record) {
        return itemDevMapper.insertSelective(record);
    }

    @Override
    public List <EisItemDev> queryByCondition(Map <String, String> map) {
        return itemDevMapper.queryByCondition(map);
    }

	@Override
	public Map<String, String> queryDisplayList(Integer id) {
		
		return itemDevMapper.queryDisplayList(id);
	}
}
