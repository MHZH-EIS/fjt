package com.ai.eis.modeler;

import java.util.Map;

@FunctionalInterface
public interface AbstractModeler {

    void process(Map <String, String> param) throws Exception;

}
