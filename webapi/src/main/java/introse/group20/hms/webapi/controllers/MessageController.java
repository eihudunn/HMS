package introse.group20.hms.webapi.controllers;

import introse.group20.hms.application.services.interfaces.IMessageService;
import introse.group20.hms.core.entities.Message;
import introse.group20.hms.webapi.DTOs.MessageDTO.MessageDisplay;
import introse.group20.hms.webapi.DTOs.MessageDTO.MessageRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class MessageController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IMessageService messageService;
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest){
        Message message = new Message();
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setContent(messageRequest.getContent());
        message.setTime(messageRequest.getTime());
        Message saveMessage = messageService.saveMessage(message);
        MessageDisplay response = modelMapper.map(saveMessage, MessageDisplay.class);
        messagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/queue/messages", response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<MessageDisplay>> getConversation(
            @RequestParam(name = "senderId") UUID senderId,
            @RequestParam(name = "receiverId") UUID receiverId
    ){
        List<Message> messageList = messageService.getConversation(senderId, receiverId);
        List<MessageDisplay> messageDisplayList = messageList.stream()
                .map(message -> modelMapper.map(message, MessageDisplay.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDisplayList);
    }
}