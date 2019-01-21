package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisStItemMapper;
import com.ai.eis.model.EisStItem;
import com.ai.eis.service.SItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "SItemService")
public class SItemServiceImpl implements SItemService {

    @Autowired
    private EisStItemMapper itemMapper;

    @Override
    public List <EisStItem> queryByCondition(Map <String, String> map) {
        return itemMapper.queryByCondition(map);
    }

    @Override
    public void add(EisStItem item) {
        itemMapper.insertSelective(item);
    }

    @Override
    public void deleteByStandardId(Integer id) {
        itemMapper.deleteByStandardId(id);
    }

    @Override
    public void deleteByItemId(Integer id) {
        itemMapper.deleteByPrimaryKey(id);
    }

	@Override
	public EisStItem queryById(Integer id) {
		// TODO Auto-generated method stub
		return itemMapper.selectByPrimaryKey(id);
	}
}
