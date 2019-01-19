package com.ai.eis.service;

import com.ai.eis.model.EisDevice;

import java.util.List;

public interface EisDeviceService {

    void insert(EisDevice device);

    List <EisDevice> selectAll();

}
