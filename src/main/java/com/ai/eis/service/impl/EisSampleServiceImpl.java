package com.ai.eis.service.impl;

import com.ai.eis.mapper.EisSampleSendMapper;
import com.ai.eis.mapper.EisSampleSignMapper;
import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;
import com.ai.eis.service.EisSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "EisSampleService")
public class EisSampleServiceImpl implements EisSampleService {

	@Autowired
	private EisSampleSignMapper signMapper;

	@Autowired
	private EisSampleSendMapper sendMapper;

	@Override
	public int send(EisSampleSend sendSample) {
		return sendMapper.insert(sendSample);
	}

	@Override
	public int sign(EisSampleSign signSample) {
		return signMapper.insert(signSample);
	}

	@Override
	public int getSampleNum(Integer projectId) {
		return signMapper.getSampleNum(projectId);
	}

}
