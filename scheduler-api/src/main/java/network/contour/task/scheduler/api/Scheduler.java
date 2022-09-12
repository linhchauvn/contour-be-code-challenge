package network.contour.task.scheduler.api;

import java.util.List;

public interface Scheduler {
    /**
     * @param tasks an iterable containing the {@link Task} to be completed
     * @param numWorkers the number of available workers
     * @return a list of {@link TaskExecution}
     */
    List<TaskExecution> estimate(Iterable<Task> tasks, int numWorkers);
}
