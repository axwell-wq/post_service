package faang.school.postservice.config.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${app.amount-thread-pool}")
    private int corePoolSize;

    @Value("${app.queue-capacity}")
    private int queueCapacity;

    @Bean
    public ThreadPoolTaskExecutor threadPool() {
        return new ThreadPoolTaskExecutor() {{
            setCorePoolSize(corePoolSize);
            setQueueCapacity(queueCapacity);
        }};
    }
}
