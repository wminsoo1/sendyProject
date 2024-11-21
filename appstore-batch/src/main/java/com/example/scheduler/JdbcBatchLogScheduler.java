package com.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcBatchLogScheduler {

    private final JobLauncher jobLauncher;
    private final Job jdbcLogJob;

    @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 00:00에 실행
    public void runBatchJob() {
        try {
            log.info("배치 작업 시작...");

            JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

            jobLauncher.run(jdbcLogJob, jobParameters);

            log.info("배치 작업 완료");
        } catch (Exception e) {
            log.error("배치 작업 실행 중 오류 발생", e);
        }
    }
}