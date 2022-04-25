package com.approvalSystem.controller;

import com.approvalSystem.service.ApprovalService;
import net.sf.json.JSONSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
    private final ApprovalService approvalService;

    public ViewController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @RequestMapping(value = "/main")
    public String main(Model model){
        // main page
        model.addAttribute("docTypeList", JSONSerializer.toJSON(approvalService.getDocTypeList()));
        model.addAttribute("userList", JSONSerializer.toJSON(approvalService.getAllUserList()));

        return "main";
    }
    @RequestMapping(value = "/serviceCheck")
    public String serviceCheck(){
        // service test page
        return "serviceCheck";
    }
}