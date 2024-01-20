package app.registrationSystem.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final VisitService visitService;

    @Scheduled(cron = "${archive.time}")
    public void moveScheduledVisitsToHistory() {
        visitService.moveVisitsToHistory();
    }
}
