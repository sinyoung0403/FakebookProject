package com.example.fakebookproject.api.friend.queue;

import com.example.fakebookproject.api.friend.dto.FriendStatusResponseDto;
import com.example.fakebookproject.api.friend.service.FriendService;
import com.example.fakebookproject.common.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FriendRequestConsumer {

    private final FriendService friendService;

    @RabbitListener(queues = RabbitConfig.FRIEND_QUEUE)
    public void receive(Map<String, Object> message) {
        try{
            Long requestUserId = Long.valueOf(message.get("requestUserId").toString());
            Long responseUserId = Long.valueOf(message.get("responseUserId").toString());

            FriendStatusResponseDto dto = friendService.requestFriend(requestUserId, responseUserId);


        } catch (Exception e){
            System.out.println("에러 메세지: " + e.getMessage());
        }

    }
}
