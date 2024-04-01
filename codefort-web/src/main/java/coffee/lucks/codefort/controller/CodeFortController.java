package coffee.lucks.codefort.controller;

import cn.dev33.satoken.stp.StpUtil;
import coffee.lucks.codefort.unit.CodeFort;
import coffee.lucks.codefort.unit.SocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class CodeFortController {

    @Value("${codefort.user}")
    private String user;

    @Value("${codefort.password}")
    private String fortPassword;

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        if (StpUtil.isLogin()) {
            model.addAttribute("loginName", "Admin");
            model.addAttribute("dataList", SocketServer.serverCodeFortMap.values());
            return "home";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(Model model, String username, String password) {
        if (user.equals(username) && fortPassword.equals(password)) {
            model.addAttribute("loginName", "Admin");
            model.addAttribute("dataList", SocketServer.serverCodeFortMap.values());
            StpUtil.login("Admin");
            return "home";
        }
        return "loginFailed";
    }

    @GetMapping("/doLogout")
    public String doLogout() {
        StpUtil.logout(10001);
        return "redirect:/login";
    }

    @GetMapping("/fortOut/{cpuSerial}")
    public String fortOut(@PathVariable String cpuSerial,
                          Model model) {
        if (StpUtil.isLogin()) {
            for (Map.Entry<SocketServer, CodeFort> entry : SocketServer.serverCodeFortMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().getCpuSerial().equals(cpuSerial)) {
                    entry.getKey().outLine();
                }
            }
            return "redirect:/home";
        }
        return "redirect:/login";
    }

    @PostMapping("/fortInfo/{cpuSerial}")
    public String fortInfo(@PathVariable String cpuSerial,
                           @RequestParam(required = false) String data,
                           Model model) {
        if (StpUtil.isLogin()) {
            for (Map.Entry<SocketServer, CodeFort> entry : SocketServer.serverCodeFortMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().getCpuSerial().equals(cpuSerial)) {
                    entry.getKey().info(data);
                }
            }
            return "redirect:/home";
        }
        return "redirect:/login";
    }

}
