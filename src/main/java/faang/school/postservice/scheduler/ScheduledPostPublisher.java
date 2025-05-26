package faang.school.postservice.scheduler;

import faang.school.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledPostPublisher {

    private final PostService postService;

    @Scheduled(cron = "${app.scheduler.post-publish-cron}")
    public void publishScheduledPosts() {
        log.info("Starting scheduled posts publication");
        postService.publishScheduledPosts();
    }
}
