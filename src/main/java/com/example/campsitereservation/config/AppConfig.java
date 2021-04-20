package com.example.campsitereservation.config;

import com.example.campsitereservation.property.ReservationProperties;
import com.example.campsitereservation.domain.ReservationEntity;
import com.example.campsitereservation.dto.Reservation;
import com.example.campsitereservation.property.TaskExecutorProperties;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableConfigurationProperties({
        ReservationProperties.class,
        TaskExecutorProperties.class
})
@EnableAsync
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Bean("asyncTaskExecutor")
    public TaskExecutor asyncExecutor(TaskExecutorProperties properties) {
        log.info("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(Reservation.class, ReservationEntity.class)
                .addMappings(mapping -> mapping.skip(ReservationEntity::setReservationId));
        return modelMapper;
    }
}
