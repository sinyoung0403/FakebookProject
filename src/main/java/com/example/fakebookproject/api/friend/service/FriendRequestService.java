package com.example.fakebookproject.api.friend.service;

import com.example.fakebookproject.common.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(Long requestUserId, Long responseUserId) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("requestUserId", requestUserId);
        msg.put("responseUserId", responseUserId);

        rabbitTemplate.convertAndSend(RabbitConfig.FRIEND_QUEUE, msg);
    }
}
