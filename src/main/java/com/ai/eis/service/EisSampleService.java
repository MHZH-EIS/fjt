package com.ai.eis.service;

import com.ai.eis.model.EisSampleSend;
import com.ai.eis.model.EisSampleSign;

import java.util.List;
import java.util.Map;

public interface EisSampleService {

    int send(EisSampleSend sendSample);

    int sign(EisSampleSign signSample);

    int getSampleNum(Integer projectId);

    List <Map <String, Object>> listProject(Map <String, String> map);

    List <EisSampleSend> listSendRecord(Map <String, String> map);

    List <EisSampleSign> listSignRecord(Map <String, String> map);

}
