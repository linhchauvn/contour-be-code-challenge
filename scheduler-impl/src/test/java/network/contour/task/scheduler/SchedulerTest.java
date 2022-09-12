package network.contour.task.scheduler;

import network.contour.task.scheduler.api.Scheduler;
import network.contour.task.scheduler.api.Task;
import network.contour.task.scheduler.api.TaskExecution;
import network.contour.task.scheduler.api.TaskFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SchedulerTest {
    private static final TaskFactory taskFactory =
        StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                ServiceLoader.load(TaskFactory.class).iterator(), 0), false)
                .findFirst().orElseThrow(
                    () -> new RuntimeException(
                            String.format("No suitable %s implementation found", Scheduler.class.getName())));
    @Test
    void test_invalidInput() {
        System.out.println("--- test_invalidInput ---");
        Scheduler scheduler = taskFactory.newScheduler();
        System.out.println("Input is null");
        List<TaskExecution> executionResult = scheduler.estimate(null, 0);
        Assertions.assertEquals(0, executionResult.size());

        System.out.println("Input has cycle issue");
        Task a = taskFactory.newTask("A", 1, new ArrayList<>());
        Task b = taskFactory.newTask("B", 2, Arrays.asList(a));
        Task c = taskFactory.newTask("C", 3, Arrays.asList(a, b));
        a.getRequirements().add(c);
        List<Task> tasks = Arrays.asList(a, b, c);
        executionResult = scheduler.estimate(tasks, 2);
        Assertions.assertEquals(0, executionResult.size());
        System.out.println();
    }

    @Test
    void test_onlyOneWorker() {
        System.out.println("--- test_onlyOneWorker ---");
        Task a = taskFactory.newTask("A", 1, Collections.emptyList());
        Task b = taskFactory.newTask("B", 2, Collections.emptyList());
        Task c = taskFactory.newTask("C", 3, Arrays.asList(a, b));
        Task d = taskFactory.newTask("D", 5, Arrays.asList(a, b, c));
        List<Task> tasks = Arrays.asList(a, b, c, d);
        int availableWorkers = 1;

        Scheduler scheduler = taskFactory.newScheduler();
        List<TaskExecution> executionResult = scheduler.estimate(tasks, availableWorkers);

        printResult(executionResult);
        System.out.println();
    }

    @Test
    void test_happpyCase() {
        System.out.println("--- test_happpyCase ---");
        Task a = taskFactory.newTask("A", 1, Collections.emptyList());
        Task b = taskFactory.newTask("B", 2, Collections.emptyList());
        Task c = taskFactory.newTask("C", 3, Arrays.asList(a, b));
        Task d = taskFactory.newTask("D", 5, Arrays.asList(a, b, c));
        List<Task> tasks = Arrays.asList(a, b, c, d);
        int availableWorkers = 10;

        Scheduler scheduler = taskFactory.newScheduler();
        List<TaskExecution> executionResult = scheduler.estimate(tasks, availableWorkers);

        printResult(executionResult);
        System.out.println();
    }

    @Test
    void test_happpyCase2() {
        System.out.println("--- test_happpyCase ---");
        Task a = taskFactory.newTask("A", 1, Collections.emptyList());
        Task b = taskFactory.newTask("B", 2, Collections.emptyList());
        Task c = taskFactory.newTask("C", 4, Arrays.asList(a, b));
        Task d = taskFactory.newTask("D", 20, Collections.emptyList());
        List<Task> tasks = Arrays.asList(a, b, c, d);
        int availableWorkers = 2;

        Scheduler scheduler = taskFactory.newScheduler();
        List<TaskExecution> executionResult = scheduler.estimate(tasks, availableWorkers);

        printResult(executionResult);
        System.out.println();
    }

    @Test
    void test_singleTaskVeryLongEffort() {
        System.out.println("--- test_singleTaskVeryLongEffort ---");
        Task a = taskFactory.newTask("A", 10, Collections.emptyList());
        Task b = taskFactory.newTask("B", 2, Collections.emptyList());
        Task c = taskFactory.newTask("C", 3, Arrays.asList(a, b));
        Task d = taskFactory.newTask("D", 5, Arrays.asList(a, b));
        Task e = taskFactory.newTask("E", 7, Arrays.asList(b));
        List<Task> tasks = Arrays.asList(a, b, c, d, e);
        int availableWorkers = 10;

        Scheduler scheduler = taskFactory.newScheduler();
        List<TaskExecution> executionResult = scheduler.estimate(tasks, availableWorkers);

        printResult(executionResult);
        System.out.println();
    }

    @Test
    void test_manyNotDependTasks() {
        System.out.println("--- test_manyNotDependTasks ---");
        Task a = taskFactory.newTask("A", 1, Collections.emptyList());
        Task b = taskFactory.newTask("B", 2, Collections.emptyList());
        Task c = taskFactory.newTask("C", 2, Collections.emptyList());
        Task d = taskFactory.newTask("D", 3, Collections.emptyList());
        Task e = taskFactory.newTask("E", 5, Arrays.asList(a, d));
        Task f = taskFactory.newTask("F", 6, Arrays.asList(b, c));
        Task g = taskFactory.newTask("G", 7, Arrays.asList(e, f));
        List<Task> tasks = Arrays.asList(a, b, c, d, e, f, g);
        int availableWorkers = 2;

        Scheduler scheduler = taskFactory.newScheduler();
        List<TaskExecution> executionResult = scheduler.estimate(tasks, availableWorkers);

        printResult(executionResult);
        System.out.println();
    }

    @Test
    void test_taskEffortBigDiff() {
        System.out.println("--- test_taskEffortBigDiff ---");
        Task a = taskFactory.newTask("A", 1, Collections.emptyList());
        Task b = taskFactory.newTask("B", 10, Collections.emptyList());
        Task c = taskFactory.newTask("D", 2, Arrays.asList(a));
        Task d = taskFactory.newTask("D", 3, Arrays.asList(c));
        Task e = taskFactory.newTask("E", 4, Arrays.asList(d));
        List<Task> tasks = Arrays.asList(a, b, c, d, e);
        int availableWorkers = 2;

        Scheduler scheduler = taskFactory.newScheduler();
        List<TaskExecution> executionResult = scheduler.estimate(tasks, availableWorkers);

        printResult(executionResult);
        System.out.println();
    }

    private static void printResult(List<TaskExecution> executionResult) {
        Map<Integer, List<TaskExecution>> workersMaps =
                executionResult.stream()
                        .collect(Collectors.groupingBy(TaskExecution::getWorkerId));

        for(Map.Entry<Integer, List<TaskExecution>> entry : workersMaps.entrySet()) {
            System.out.printf("Worker: %d\n", entry.getKey());
            boolean first = true;
            for(TaskExecution taskExecution : entry.getValue()) {
                if(!first) System.out.println(", ");
                else first = false;
                System.out.printf("(%s, [%d, %d])",
                        taskExecution.getTask().getName(),
                        taskExecution.getStart(),
                        taskExecution.getEnd());
            }
            System.out.println();
        }
    }
}
