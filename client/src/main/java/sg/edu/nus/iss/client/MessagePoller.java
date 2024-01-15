package sg.edu.nus.iss.client;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.ListOperations;

@Component
public class MessagePoller{
    @Autowired private RedisTemplate<String, String> template;

    @Autowired
    private ApplicationArguments  applicationArguments;

    @Async
    public void start(){
        List<String> customerNameArgs = applicationArguments.getNonOptionArgs();
        ListOperations<String, String> registrationList = template.opsForList();
        registrationList.leftPush("registration", customerNameArgs.get(0));
    }
}
