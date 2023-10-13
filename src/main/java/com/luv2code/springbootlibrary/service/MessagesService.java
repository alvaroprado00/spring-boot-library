package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.MessageRepository;
import com.luv2code.springbootlibrary.entity.Message;
import com.luv2code.springbootlibrary.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository){
        this.messageRepository=messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail){

        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void answerMessage(AdminQuestionRequest answer, String adminEmail) throws Exception{

        Optional<Message> validateMessage= messageRepository.findById(answer.getId());

        if(validateMessage.isEmpty() || validateMessage.get().isClosed()){
            throw new Exception("Message not found or already answered");
        }

        validateMessage.get().setAdminEmail(adminEmail);
        validateMessage.get().setResponse(answer.getResponse());
        validateMessage.get().setClosed(true);
        messageRepository.save(validateMessage.get());


    }
}
