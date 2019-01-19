package com.ai.eis.controller;

import com.ai.eis.model.EisStandard;
import com.ai.eis.service.StandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/standard")
public class StandardController {

    @Autowired
    private StandardService standardService;

    @RequestMapping("/form")
    public void form(Long id) {
    }

    @ResponseBody
    @RequestMapping("/list")
    public List <EisStandard> queryByCondition(String name) {
        Map <String, String> map = new HashMap <>();
        map.put("name", name);
        return standardService.list(map);
    }

}
