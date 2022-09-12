package network.contour.task.scheduler.impl;

import network.contour.task.scheduler.api.Scheduler;
import network.contour.task.scheduler.api.Task;
import network.contour.task.scheduler.api.TaskExecution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchedulerImpl implements Scheduler {
    @Override
    public List<TaskExecution> estimate(Iterable<Task> tasks, int numWorkers) {
        // validate input
        if (!isValidateInput(tasks, numWorkers)) {
            System.out.println("Invalid input");
            return Collections.emptyList(); // throwing exception?
        }

        // calculate effort and put to TreeMap
        Map<Task, Integer> startTimeMap = new HashMap<>();
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            calculatePossibleStartTime(startTimeMap, iterator.next());
        }
        LinkedHashMap<Task, Integer> sortedStartTimeMap = new LinkedHashMap<>();
        startTimeMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(o -> sortedStartTimeMap.put(o.getKey(), o.getValue()));

        // init workers map
        Map<Integer, Integer> workersEffort = new HashMap<>();
        for (int i = 1; i <= numWorkers; i++) {
            workersEffort.put(i, 0);
        }

        // create task execution
        List<TaskExecution> list = sortedStartTimeMap.entrySet().stream()
                .map(entry -> {
                    Map.Entry<Integer, Integer> worker = Collections.min(workersEffort.entrySet(), Map.Entry.comparingByValue());
                    int correctStartTime = entry.getValue() > worker.getValue() ? entry.getValue() : worker.getValue();
                    TaskExecution execution = new TaskExecutionImpl(entry.getKey(), worker.getKey(), correctStartTime);
                    updatePossibleStartTime(sortedStartTimeMap, entry.getKey(), execution.getEnd());
                    workersEffort.put(worker.getKey(), execution.getEnd());
                    return execution;
                })
                .collect(Collectors.toList());
        return list;
    }

    private void updatePossibleStartTime(Map<Task, Integer> startTimeMap, Task dependTask, Integer actualEndTime) {
        if (!startTimeMap.isEmpty()) {
            for (Map.Entry<Task, Integer> entry : startTimeMap.entrySet()) {
                if (entry.getKey().getRequirements() != null && entry.getKey().getRequirements().contains(dependTask) && entry.getValue() < actualEndTime) {
                    entry.setValue(actualEndTime);
                }
            }
        }
    }

    private boolean isValidateInput(Iterable<Task> tasks, int numWorkers) {
        if (tasks == null || !tasks.iterator().hasNext() || numWorkers < 1) {
            System.out.println("Invalid tasks or number of workers.");
            return false;
        }

        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task head = iterator.next();
            if (isCircularList(head, head)) {
                System.out.println("The task has circle issue.");
                return false;
            }
        }
        return true;
    }


    private boolean isCircularList(Task head, Task current) {
        if (current.getRequirements() != null && !current.getRequirements().isEmpty()) {
            for (Task task : current.getRequirements()) {
                if (task == head) {
                    return true;
                }
                if (isCircularList(head, task)) {
                    return true;
                }
            }
        }
        return false;
    }


    private Integer calculatePossibleStartTime(Map<Task, Integer> startTimeMap, Task task) {
        int startTime = 0;
        if (task.getRequirements() != null && !task.getRequirements().isEmpty()) {
            for (Task item : task.getRequirements()) {
                int estStartTime;
                if (startTimeMap.get(item) != null) {
                    estStartTime = startTimeMap.get(item) + item.getEffort();

                } else {
                    int itemStartTime = 0;
                    if (item.getRequirements() != null && !item.getRequirements().isEmpty()) {
                        for (Task childItem : item.getRequirements()) {
                            itemStartTime = calculatePossibleStartTime(startTimeMap, childItem);
                        }
                    }
                    estStartTime = itemStartTime + item.getEffort();
                }
                if (estStartTime > startTime) {
                    startTime = estStartTime;
                }
            }
        }
        startTimeMap.put(task, startTime);
        return startTime;
    }
}
