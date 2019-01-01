package com.yfs.mvc;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Component
@RequestMapping("/my/")
public class MyController {

    @RequestMapping(value = "yfs1", method = RequestMethod.GET)
    public String yfs1(Model model) {
        model.addAttribute("name", "yfs");
        return "my1";
    }
    @ResponseBody
    @RequestMapping(value = "/yfs2", method = RequestMethod.GET)
    public Object yfs2() {
        Map<String,String> json = new HashMap<>(2);
        json.put("aaa","aaa");
        json.put("bbb","bbb");
        return json;
    }

}
