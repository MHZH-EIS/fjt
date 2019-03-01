package com.ai.eis.modeler;

import java.io.File;
import java.util.Map;

@FunctionalInterface
public interface AbstractModeler {

    File process(Map <String, String> param) throws Exception;

}
