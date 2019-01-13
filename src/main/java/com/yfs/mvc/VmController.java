package com.yfs.mvc;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@RequestMapping("/vm")
public class VmController {

    @RequestMapping(value = "/myaccount", method = RequestMethod.GET)
    public String yfs1(Model model) {
        model.addAttribute("myName", "yfs");
        model.addAttribute("myAge", "18");
        return "vm/myaccount";
    }


    @RequestMapping(value = "/myaccount2", method = RequestMethod.GET)
    public String yfs2(Model model) {
        model.addAttribute("myName", "yfs");
        model.addAttribute("myAge", "18");
        return "/vm/myaccount";
    }

}
