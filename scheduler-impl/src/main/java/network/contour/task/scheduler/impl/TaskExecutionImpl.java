package network.contour.task.scheduler.impl;


import network.contour.task.scheduler.api.Task;
import network.contour.task.scheduler.api.TaskExecution;

public class TaskExecutionImpl implements TaskExecution {
    private final Task task;
    private final int workerId;
    private final int start;

    public TaskExecutionImpl(Task task, int workerId, int start) {
        this.task = task;
        this.workerId = workerId;
        this.start = start;
    }

    public int getEnd() {
        return getStart() + task.getEffort();
    }

    public Task getTask() {
        return task;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getStart() {
        return start;
    }

    @Override
    public String toString() {
        return "TaskExecution{" +
                "task=" + task +
                ", workerId=" + workerId +
                ", start=" + start +
                '}';
    }
}