package network.contour.task.scheduler.impl;

import network.contour.task.scheduler.api.Task;

import java.util.Iterator;
import java.util.List;

public class TaskImpl implements Task {
    private final String name;
    private final int effort;
    private final List<Task> requirements;

    public TaskImpl(
        String name,
        int effort,
        List<Task> requirements) {
        if(effort < 0)
            throw new IllegalArgumentException(String.format("Negative task effort is not allowed (%d)", effort));
        this.name = name;
        this.effort = effort;
        this.requirements = requirements;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getEffort() {
        return effort;
    }

    public List<Task> getRequirements() {
        return requirements;
    }

    @Override
    public Iterator<Task> iterator() {
        return requirements.iterator();
    }
}
