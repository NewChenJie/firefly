package zool.firefly.listenner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author : chenjie
 * @date : 2018-12-25 13:21
 * @describe :
 */
@Component
@Slf4j
public class RedisMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        //消息体
        String body = new String(message.getBody());
        //渠道名称
        String topic = new String(bytes);

        log.info("接收到消息：{}",body);
        log.info("由{}渠道发送而来",topic);
    }
}
