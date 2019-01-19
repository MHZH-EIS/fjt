package com.ai.eis.service;

import java.util.List;

import com.ai.eis.model.EisMenuResource;
import com.ai.eis.model.EisRoleMenuResource;

 


 

public interface EisMenuResourceService  {
	int addMenuResource(EisMenuResource role);
	int deleteMenuResource(Long id);
	
    EisMenuResource   selectByMenuId(Long id);

    List<EisMenuResource>  selectByRoleId(Integer roleId);
    List<EisMenuResource>  selectAllResources();
    
    Long        selectParentResource(Long id);
    List<EisMenuResource>  selectChildResource(Long id);
    List<EisMenuResource>  selectParentIsNull();
    List<EisMenuResource>  selectByStatusAndParentIsNull();
    List<EisMenuResource>  selectByStatusAndParent(Long id);
    
    Iterable<EisMenuResource>     getSourceTree();
    Iterable<EisMenuResource>     getResourceTree(Boolean status);
    
 
    
}
