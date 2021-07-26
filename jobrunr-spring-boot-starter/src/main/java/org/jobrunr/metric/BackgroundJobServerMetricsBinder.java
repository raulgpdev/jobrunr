package org.jobrunr.metric;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.jobrunr.server.BackgroundJobServer;

import javax.annotation.PostConstruct;

public class BackgroundJobServerMetricsBinder {

    private final BackgroundJobServer backgroundJobServer;

    public BackgroundJobServerMetricsBinder(BackgroundJobServer backgroundJobServer) {
        this.backgroundJobServer = backgroundJobServer;
    }

    @PostConstruct
    public void setUpMetrics() {
        this.registryBackgroundJobServer();
    }

    private void registryBackgroundJobServer() {
        String PREFIX = "jobrunr_background_server_";
        String workerPoolSize = PREFIX + "worker_pool_size";
        String pollIntervalInSeconds = PREFIX + "poll_interval_in_seconds";
        String processAllocatedMemory = PREFIX + "process_all_located_memory";
        String processFreeMemory = PREFIX + "process_free_memory";
        String systemTotalMemory = PREFIX + "system_total_memory";
        String systemFreeMemory = PREFIX + "system_free_memory";
        String firstHeartbeat = PREFIX + "first_heartbeat";
        String lastHeartbeat = PREFIX + "last_heartbeat";

        SimpleMeterRegistry backgroundJobServerMeter = new SimpleMeterRegistry();
        FunctionCounter.builder(pollIntervalInSeconds, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getPollIntervalInSeconds()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        FunctionCounter.builder(workerPoolSize, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getWorkerPoolSize()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        Gauge.builder(processAllocatedMemory, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getProcessAllocatedMemory()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        Gauge.builder(processFreeMemory, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getProcessFreeMemory()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        Gauge.builder(systemFreeMemory, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getSystemFreeMemory()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        Gauge.builder(systemTotalMemory, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getSystemTotalMemory()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        Gauge.builder(firstHeartbeat, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getFirstHeartbeat().getEpochSecond()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);
        Gauge.builder(lastHeartbeat, this.backgroundJobServer, (bgJobServer) -> (double) bgJobServer.getServerStatus().getLastHeartbeat().getNano()).tag("id", this.backgroundJobServer.getId().toString()).register(backgroundJobServerMeter);

        Metrics.addRegistry(backgroundJobServerMeter);
    }
}
