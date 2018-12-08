package zool.firefly.listenner;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class RabbitListenner {

    @RabbitListener(queues = "base.queue")
    public void receive(String message) {
        System.out.println("接收消息:" + message);
    }


}
