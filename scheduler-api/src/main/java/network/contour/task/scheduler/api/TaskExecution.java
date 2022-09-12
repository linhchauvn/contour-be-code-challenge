package network.contour.task.scheduler.api;

/**
 * Represents a scheduled task execution
 */
public interface TaskExecution {

    /**
     * @return the task completion time
     */
    int getEnd();

    /**
     * @return the scheduled task being executed
     */
    Task getTask();

    /**
     * @return the id of the worker to which the task is assigned
     */
    int getWorkerId();

    /**
     * @return the task start time
     */
    int getStart();
}
