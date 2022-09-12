package network.contour.task.scheduler.api;

import java.util.List;
import java.util.ServiceLoader;

/**
 * This class is supposed to be constructed with {@link ServiceLoader} and used to create all new instances
 * of {@link Scheduler}, {@link Task} and {@link TaskExecution}
 */
public interface TaskFactory {
    /**
     * Creates a new scheduler
     * @return a newly created {@link Scheduler}
     */
    Scheduler newScheduler();

    /**
     * Creates a new task
     * @param name the name of the task
     * @param effort the effort required to complete the task
     * @param requirements a list of {@link Task}s that need to be completed before this task can be started
     * @return a newly created {@link Task}
     */
    Task newTask(String name, int effort, List<Task> requirements);

    /**
     * Creates a new {@link TaskExecution}
     * @param task the task scheduled to be executed
     * @param workerId the id of the worked to which the task is assigned
     * @param start the task start time
     * @return a new {@link TaskExecution} instance
     */
    TaskExecution newExecution(Task task, int workerId, int start);
}
