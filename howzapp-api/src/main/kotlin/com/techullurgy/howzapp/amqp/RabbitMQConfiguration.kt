package com.techullurgy.howzapp.amqp

import com.techullurgy.howzapp.notifications.websockets.AppWebsocketHandler
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnBooleanProperty(value = ["rabbitmq.enabled"], prefix = "application", havingValue = true, matchIfMissing = true)
class RabbitMQConfiguration(
    private val handler: AppWebsocketHandler
) {

    @Bean
    fun fanoutExchange(): FanoutExchange {
        return FanoutExchange(APP_RECEIPT_EXCHANGE, true, false)
    }

    @Bean
    fun queue1(): Queue {
        return Queue(RECEIPTS__ON_NEW_RECEIPT, true)
    }

    @Bean
    fun queue2(): Queue {
        return Queue(RECEIPTS__RECEIPT_REACTION, true)
    }

    @Bean
    fun fanoutBinding1(fanoutExchange: FanoutExchange, queue1: Queue): Binding {
        return BindingBuilder.bind(queue1).to(fanoutExchange)
    }

    @Bean
    fun fanoutBinding2(fanoutExchange: FanoutExchange, queue2: Queue): Binding {
        return BindingBuilder.bind(queue2).to(fanoutExchange)
    }

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = messageConverter()
        return template
    }

    companion object {
        const val APP_RECEIPT_EXCHANGE = "app_receipt_exchange"
        const val RECEIPTS__ON_NEW_RECEIPT = "receipts.on_new_receipt"
        const val RECEIPTS__RECEIPT_REACTION = "receipts.receipt_reaction"
        const val RECEIPTS__NEW_RECEIPT_ACCEPTED = "receipts.new_receipt_accepted"
        const val MESSAGES__ON_NEW_MESSAGE_STATUS_CHANGED = "messages.on_new_message_status_changed"
    }
}

@Configuration
@ConditionalOnBooleanProperty(value = ["rabbitmq.enabled"], prefix = "application", havingValue = false)
class RabbitMqConfiguration2 {
    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = EmptyRabbitTemplate(connectionFactory)
        return template
    }
}

class EmptyRabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate(connectionFactory) {
    override fun convertAndSend(
        exchange: String,
        routingKey: String,
        message: Any,
    ) {}
}