package com.ai.eis.service;

import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;

public interface EisSampleService {

	public int send(EisSampleSend sendSample);

	public int sign(EisSampleSign signSample);

	public int getSampleNum(Integer projectId);

}
