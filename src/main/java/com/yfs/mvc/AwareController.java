package com.yfs.mvc;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/aware/")
public class AwareController implements EnvironmentAware{

    private Environment environment;

    @RequestMapping(value = "yfs1", method = RequestMethod.GET)
    public String yfs1(Model model) {
        model.addAttribute("name", "yfs");
        return "my1";
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
