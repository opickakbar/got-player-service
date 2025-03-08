package com.gameofthree.playerservice.config;

import com.gameofthree.playerservice.service.PlayerService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String GAME_EXCHANGE = "game.exchange";

    @Bean
    public DirectExchange gameExchange() {
        return new DirectExchange(GAME_EXCHANGE);
    }

    @Bean
    public Queue playerQueue1() {
        return new Queue("player.queue.1", false);
    }

    @Bean
    public Queue playerQueue2() {
        return new Queue("player.queue.2", false);
    }

    @Bean
    public Binding bindingPlayer1(Queue playerQueue1, DirectExchange gameExchange) {
        return BindingBuilder.bind(playerQueue1).to(gameExchange).with("player.1");
    }

    @Bean
    public Binding bindingPlayer2(Queue playerQueue2, DirectExchange gameExchange) {
        return BindingBuilder.bind(playerQueue2).to(gameExchange).with("player.2");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public String playerQueue() {
        return PlayerService.getPlayerRole().equals("Player1") ? "player.queue.1" : "player.queue.2";
    }
}





