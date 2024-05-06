package gc.garcol.cluster.domain;

import io.aeron.cluster.service.Cluster;

/**
 * @author thaivc
 * @since 2024
 */
public interface TimerManager {

    /**
     * Schedules a timer
     *
     * @param deadline      the deadline of the timer
     * @param timerRunnable the timerRunnable to perform when the timer fires
     * @return the correlation id of the timer
     */
    long scheduleTimer(final long deadline, final Runnable timerRunnable);

    /**
     * Restores a timer that the cluster has snapshotted the timer state, but not the timer manager
     * internal state
     *
     * @param timerCorrelationId the correlation id of the timer
     * @param task               the task to perform when the timer fires
     */
    void restoreTimer(final long timerCorrelationId, final Runnable task);

    /**
     * Called when a timer cluster event occurs
     *
     * @param correlationId the cluster timer id
     * @param timestamp     the timestamp the timer was fired at
     */
    void onTimerEvent(final long correlationId, final long timestamp);

    /***
     * Sets the cluster object used for scheduling timers
     * @param cluster the cluster object
     */
    public void setCluster(final Cluster cluster);
}
