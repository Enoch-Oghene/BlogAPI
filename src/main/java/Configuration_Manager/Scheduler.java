package Configuration_Manager;

import Service.Service_User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    Service_User service_user;

    @Scheduled(fixedRate = 60000L)
    public void scheduleTaskWithFixedRate() {
        service_user.userDeactivationScheduler();
    }
}
