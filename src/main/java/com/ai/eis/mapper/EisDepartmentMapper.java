package com.ai.eis.mapper;

import com.ai.eis.model.EisDepartment;

public interface EisDepartmentMapper {
    int deleteByPrimaryKey(Integer dmId);

    int insert(EisDepartment record);

    int insertSelective(EisDepartment record);

    EisDepartment selectByPrimaryKey(Integer dmId);

    int updateByPrimaryKeySelective(EisDepartment record);

    int updateByPrimaryKey(EisDepartment record);
}