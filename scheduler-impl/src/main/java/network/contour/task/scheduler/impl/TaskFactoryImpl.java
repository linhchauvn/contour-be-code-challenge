package network.contour.task.scheduler.impl;

import network.contour.task.scheduler.api.Scheduler;
import network.contour.task.scheduler.api.Task;
import network.contour.task.scheduler.api.TaskExecution;
import network.contour.task.scheduler.api.TaskFactory;

import java.util.List;

public class TaskFactoryImpl implements TaskFactory {
    @Override
    public Scheduler newScheduler() {
        return new SchedulerImpl();
    }

    @Override
    public Task newTask(String name, int effort, List<Task> requirements) {
        return new TaskImpl(name, effort, requirements);
    }

    @Override
    public TaskExecution newExecution(Task task, int workerId, int start) {
        return new TaskExecutionImpl(task, workerId, start);
    }
}
