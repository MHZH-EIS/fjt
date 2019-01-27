package com.ai.eis.mapper;

import com.ai.eis.model.EisMenuResource;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface EisMenuResourceMapper {
    int countByExample(EisMenuResource example);

    int deleteByExample(EisMenuResource example);

    int insert(EisMenuResource record);

    int update(EisMenuResource role);

    int insertSelective(EisMenuResource record);

    int updateByExampleSelective(@Param("record") EisMenuResource record, @Param("example") EisMenuResource example);

    int updateByExample(@Param("record") EisMenuResource record, @Param("example") EisMenuResource example);
    
    List<EisMenuResource>  selectByRoleId(Integer roleId);
    
    List<EisMenuResource> selectAllResources();
    
    int deleteByid(Long id);
    Long        selectParentResource(Long id);
    List<EisMenuResource>  selectChildResource(Long id);
    
    EisMenuResource  selectMenuIdResource(Long id);

	List<EisMenuResource> findByParentIsNull();

	List<EisMenuResource> findByStatusAndParentIsNull(Boolean status);

	List<EisMenuResource> findByParent(Long parent_id);

	List<EisMenuResource> findByStatusAndParent(HashMap<String, Object> paraMap);
    
    
    
}