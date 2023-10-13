package com.luv2code.springbootlibrary.controller;

import com.luv2code.springbootlibrary.entity.Message;
import com.luv2code.springbootlibrary.requestmodels.AdminQuestionRequest;
import com.luv2code.springbootlibrary.service.MessagesService;
import com.luv2code.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {


    private MessagesService messagesService;

    @Autowired
    public MessagesController(MessagesService messagesService){
        this.messagesService=messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestBody Message messageRequest, @RequestHeader(value = "Authorization") String token){

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messagesService.postMessage(messageRequest, userEmail);
    }

    @PutMapping("/secure/answer/message")
    public void answerMessage(@RequestBody AdminQuestionRequest answer, @RequestHeader(value = "Authorization") String token)throws Exception {
        String adminEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        String admin =ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        if(admin==null || !admin.equals("admin")){
            throw new Exception("User not admin");
        }
        messagesService.answerMessage(answer, adminEmail);
    }

}
