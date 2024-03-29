package coffee.lucks.codefort.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SimpleAuthenticateController {

    @GetMapping("/home")
    public String home(Model model){
        if(StpUtil.isLogin()){
            model.addAttribute("loginId", StpUtil.getLoginId());
            return "home";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/doLogin")
    public String doLogin(String username, String password){
        if("admin".equals(username) && "123456".equals(password)){
            StpUtil.login(10001);
            return "loginSucceed";
        }
        return "loginFailed";
    }

    @GetMapping("/doLogout")
    public String doLogout(){
        StpUtil.logout(10001);
        return "redirect:/login";
    }

}
