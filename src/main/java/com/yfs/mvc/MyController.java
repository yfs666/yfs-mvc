package com.yfs.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/my/")
public class MyController {

    @RequestMapping(value = "yfs1", method = RequestMethod.GET)
    public String yfs1(Model model) {
        model.addAttribute("name", "yfs");
        return "my1";
    }

}
