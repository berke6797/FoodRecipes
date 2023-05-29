package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.FavoriteCategoriesMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCategoriesMailProducer {

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.favoriteCategoryBindingKey}")
    String favoriteCategoryBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendMailForFavoriteCategory(FavoriteCategoriesMailModel model){
        rabbitTemplate.convertAndSend(exchange,favoriteCategoryBindingKey,model);
    }

}
