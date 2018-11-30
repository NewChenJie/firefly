package zool.firefly.scheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


//@Component
public class DemoScheduler {
    @Scheduled(cron = "0/5 * * * * ?")
    public void run() {
        System.out.println("我在定时~~~~~~~~~~~~~");
    }
}
