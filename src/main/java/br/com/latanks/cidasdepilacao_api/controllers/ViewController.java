package br.com.latanks.cidasdepilacao_api.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {

    @RequestMapping(value = "css/login.css", method = RequestMethod.GET)
    public String loginCSS(Model model, HttpServletResponse response) {
        return "css/login.css";
    }

    @RequestMapping(value = "css/home.css", method = RequestMethod.GET)
    public String homeCSS(Model model, HttpServletResponse response) {
        return "css/home.css";
    }

    @RequestMapping(value = "css/register.css", method = RequestMethod.GET)
    public String registerCSS(Model model, HttpServletResponse response) {
        return "css/register.css";
    }

    @RequestMapping(value = "css/adminPanel.css", method = RequestMethod.GET)
    public String adminPanelCSS(Model model, HttpServletResponse response) {
        return "css/adminPanel.css";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(ModelMap model) {
        ModelAndView mv = new ModelAndView("login.html");
        return mv;
    }

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ModelAndView home(ModelMap model) {
        ModelAndView mv = new ModelAndView("home.html");
        return mv;
    }

    @RequestMapping(value = "adminPanel", method = RequestMethod.GET)
    public ModelAndView adminPanel(ModelMap model) {
        ModelAndView mv = new ModelAndView("adminPanel.html");
        return mv;
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView register(ModelMap model) {
        ModelAndView mv = new ModelAndView("register.html");
        return mv;
    }

    @RequestMapping(value = "js/login.js", method = RequestMethod.GET)
    public String loginJS(Model model, HttpServletResponse response) {
        return "js/login.js";
    }

    @RequestMapping(value = "js/home.js", method = RequestMethod.GET)
    public String homeJS(Model model, HttpServletResponse response) {
        return "js/home.js";
    }

    @RequestMapping(value = "js/register.js", method = RequestMethod.GET)
    public String registerJS(Model model, HttpServletResponse response) {
        return "js/register.js";
    }

    @RequestMapping(value = "js/adminPanel.js", method = RequestMethod.GET)
    public String adminPanelJS(Model model, HttpServletResponse response) {
        return "js/adminPanel.js";
    }
}
