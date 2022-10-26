package com.dongkap.common.configuration;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import lombok.Setter;

@Setter
@Configuration
@EnableAsync
@ConfigurationProperties("async-thread-pool")
public class AsynchronousConfiguration implements AsyncConfigurer {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private int corePoolSize = 20;
	private int maximumPoolSize = 1000;
	private int keepAliveTime = 30;
	private int queueCapacity = 200;
	private String threadPrefix = "DONGKAP_EXEC-";
	
	@Override
    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maximumPoolSize);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadPrefix);
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe)->{
        	LOGGER.warn("The current task thread pool queue is full.");
        });
        executor.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				LOGGER.error("An unknown exception occurred in the thread pool execution task.", ex);
			}
		};
	}

}
