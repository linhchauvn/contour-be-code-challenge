package network.contour.task.scheduler.api;

import java.util.Iterator;
import java.util.List;

/**
 * An interface representing a task, it implements {@link Iterable} and it's inherited
 */
public interface Task extends Iterable<Task> {
    /**
     * @return the name of the task
     */
    String getName();

    /**
     * @return the time required to complete the task in arbitrary unit of measure
     */
    int getEffort();

    /**
     * @return the list of tasks that need to be completed before this task can be started
     */
    List<Task> getRequirements();

    /**
     * @return an iterator that returns all of this task's requirement in order, this method is just
     * syntactic sugar for {@code task.getRequirements().iterator()}
     */
    @Override
    Iterator<Task> iterator();
}
