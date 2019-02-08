package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisSampleSendMapper;
import com.ai.eis.mapper.EisSampleSignMapper;
import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.EisSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "EisSampleService")
public class EisSampleServiceImpl implements EisSampleService {

    @Autowired
    private EisSampleSignMapper signMapper;

    @Autowired
    private EisSampleSendMapper sendMapper;

    @Override
    public int send(EisSampleSend sendSample) {
        return sendMapper.insertSelective(sendSample);
    }

    @Override
    public int sign(EisSampleSign signSample) {
        return signMapper.insertSelective(signSample);
    }

    @Override
    public int deleteSendById(Integer id) {
        return sendMapper.deleteById(id);
    }

    @Override
    public int deleteSignById(Integer id) {
        return signMapper.deleteById(id);
    }

    @Override
    public List <Map <String, Object>> listProject(Map <String, String> map) {
        return signMapper.listProject(map);
    }

    @Override
    public List <EisSampleSend> listSendRecord(Map <String, String> map) {
        return sendMapper.listSendRecord(map);
    }

    @Override
    public List <EisSampleSign> listSignRecord(Map <String, String> map) {
        return signMapper.listSignRecord(map);
    }

	@Override
	public int updateSend(EisSampleSend sendSample) {
 
		return sendMapper.updateSend(sendSample);
	}

	@Override
	public int updateSign(EisSampleSign signSample) {
		 return signMapper.updateSign(signSample);
	}
}
