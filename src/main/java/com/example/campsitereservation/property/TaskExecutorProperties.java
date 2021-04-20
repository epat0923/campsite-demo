package com.example.campsitereservation.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("reservation.executor")
public class TaskExecutorProperties {

    private Integer corePoolSize = 10;
    private Integer maxPoolSize = 100;
    private Integer queueCapacity = 100;

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
