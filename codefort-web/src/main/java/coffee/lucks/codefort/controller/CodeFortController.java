package coffee.lucks.codefort.controller;

import coffee.lucks.codefort.unit.SocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fort")
public class CodeFortController {

    @RequestMapping("/list")
    @ResponseBody
    public Object list() {
        return SocketServer.serverCodeFortMap.values();
    }

    @GetMapping("/showDataList")
    public String showDataList(Model model) {
        // 将数据列表添加到Model中
        model.addAttribute("dataList", SocketServer.serverCodeFortMap.values());
        // 返回Thymeleaf模板页面
        return "data-list";
    }

}
