package com.example.job;

import com.example.completeddelivery.entity.CompletedDelivery;
import com.example.driver.model.entity.Driver;
import jakarta.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JdbcBatchLogJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public JdbcBatchLogJobConfig(JobRepository jobRepository,
        PlatformTransactionManager transactionManager, @Qualifier("serviceDataSource") DataSource dataSource,
        EntityManagerFactory entityManagerFactory) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job jdbcLogJob() {
        return new JobBuilder("jdbcLogJob", jobRepository)
            .start(jdbcLogStep())
            .build();
    }

    @Bean
    public Step jdbcLogStep() {
        return new StepBuilder("jdbcLogStep", jobRepository)
            .<CompletedDelivery, Driver>chunk(CHUNK_SIZE, transactionManager)
            .reader(jpaPagingItemReader())
            .processor(driverSettlementProcessor())
            .writer(driverSettlementWriter())
            .transactionManager(transactionManager)
            .build();
    }

    @Bean
    public JpaPagingItemReader<CompletedDelivery> jpaPagingItemReader() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();

        return new JpaPagingItemReaderBuilder<CompletedDelivery>()
            .name("jpaPagingItemReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT c FROM CompletedDelivery c WHERE c.createdAt >= :oneMonthAgo ORDER BY c.id")
            .parameterValues(Map.of("oneMonthAgo", oneMonthAgo))
            .pageSize(CHUNK_SIZE)
            .build();
    }

    @Bean
    public ItemProcessor<CompletedDelivery, Driver> driverSettlementProcessor() {
        return completedDelivery -> {
            Long driverId = completedDelivery.getDriverId();
            BigDecimal deliveryFee = completedDelivery.getDeliveryFee();

            if (driverId == null || deliveryFee == null) {
                return null;
            }

            return Driver.builder()
                .id(driverId)
                .balance(deliveryFee)
                .build();
        };
    }

    @Bean
    public JdbcBatchItemWriter<Driver> driverSettlementWriter() {
        return new JdbcBatchItemWriterBuilder<Driver>()
            .dataSource(dataSource)
            .sql("UPDATE driver SET balance = balance + :balance WHERE id = :id")
            .beanMapped()
            .build();
    }

}
