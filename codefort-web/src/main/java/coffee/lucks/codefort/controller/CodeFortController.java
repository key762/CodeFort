package coffee.lucks.codefort.controller;

import cn.dev33.satoken.stp.StpUtil;
import coffee.lucks.codefort.unit.CodeFort;
import coffee.lucks.codefort.unit.SocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class CodeFortController {

    @GetMapping("/home")
    public String home(Model model) {
        if (StpUtil.isLogin()) {
            model.addAttribute("loginId", StpUtil.getLoginId());
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
    public String doLogin(String username, String password) {
        if ("admin".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return "loginSucceed";
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
