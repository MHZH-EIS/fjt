package com.ai.eis.mapper;

import com.ai.eis.model.EisRoleMenuResource;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface EisRoleMenuResourceMapper {
    int countByExample(EisRoleMenuResource example);

    int deleteByExample(EisRoleMenuResource example);
    
    int deleteByPrimaryKey(Integer roleId);

    int insert(EisRoleMenuResource record);

    int insertSelective(EisRoleMenuResource record);

    int updateByExampleSelective(@Param("record") EisRoleMenuResource record, @Param("example") EisRoleMenuResource example);

    int updateByExample(@Param("record") EisRoleMenuResource record, @Param("example") EisRoleMenuResource example);
    
    List<EisRoleMenuResource> selectByRoleId(Integer roleId);

	List<EisRoleMenuResource> findByParentIsNull();

	List<EisRoleMenuResource> findByStatusAndParentIsNull(Boolean status);

	List<EisRoleMenuResource> findByParent(EisRoleMenuResource t);

	List<EisRoleMenuResource> findByStatusAndParent(Boolean status, EisRoleMenuResource t);

	void insertByIdAndResourceids(Integer roleid, List<Long> resourceids);

	void insertByIdAndResourceids(Map<String, Object> map);
	
}