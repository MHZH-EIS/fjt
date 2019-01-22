package com.ai.eis.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.eis.mapper.EisMenuResourceMapper;
 
import com.ai.eis.model.EisMenuResource;
 

@Service(value = "eisMenuResourceService")
public class EisMenuResourceServiceImpl  implements EisMenuResourceService {


    @Autowired
    private EisMenuResourceMapper eisMenuResourceMapper;
	
	@Override
	public int addMenuResource(EisMenuResource role) {
 
		return eisMenuResourceMapper.insert(role);
	}

 

	@Override
	public List<EisMenuResource> selectByRoleId(Integer roleId) {
 
		return null;
	}



	@Override
	public List<EisMenuResource> selectAllResources() {
 
		return eisMenuResourceMapper.selectAllResources();
	}



	@Override
	public Long selectParentResource(Long id) {
 
		return eisMenuResourceMapper.selectParentResource(id);
	}



	@Override
	public List<EisMenuResource> selectChildResource(Long id) {
 
		return eisMenuResourceMapper.selectChildResource(id);
	}



	@Override
	public EisMenuResource selectByMenuId(Long id) {
 		return eisMenuResourceMapper.selectMenuIdResource(id);
	}



	@Override
	public int deleteMenuResource(Long id) {
		 
		return eisMenuResourceMapper.deleteByid(id);
	}



	@Override
	public List<EisMenuResource> selectParentIsNull() {
 
		return eisMenuResourceMapper.findByParentIsNull();
	}



	@Override
	public List<EisMenuResource> selectByStatusAndParentIsNull() {
 
		return null;
	}



	@Override
	public List<EisMenuResource> selectByStatusAndParent(Long id) {
 
		return null;
	}


 


	@Override
	public Iterable<EisMenuResource> getSourceTree() {
		return this.getResourceTree(null);
	}



	@Override
	public Iterable<EisMenuResource> getResourceTree(Boolean status) {
        Iterable<EisMenuResource> root;
        if (status == null) {
            root = eisMenuResourceMapper.findByParentIsNull();
        } else {
            root = eisMenuResourceMapper.findByStatusAndParentIsNull(status);
        }
        this.buildTree(root, status);
        return root;
	}
	
    private void buildTree(Iterable<EisMenuResource> root, Boolean status) {
        root.forEach(t -> {
            List<EisMenuResource> children;

            if (status == null) {
                children = eisMenuResourceMapper.findByParent(t.getId());
            } else {
            	HashMap<String, Object> paraMap = new HashMap<String,Object>();
            	paraMap.put("status", status);
            	paraMap.put("parent_id",t.getId());
                children = eisMenuResourceMapper.findByStatusAndParent(paraMap);
            }

            children.forEach(c -> t.getChildren().add(c));

            // 此处递归
            buildTree(children, status);
        });
    }

	@Override
	public int updateMenuResource(EisMenuResource role) {
		return eisMenuResourceMapper.update(role);
	}

}
