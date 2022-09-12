### Problem overview

This challenge revolves about the problem of task scheduling: imagine you have a set of tasks that need to be carried out,
for each task it is known exactly how much time is required for completion (known as "effort") and a task
may have a set of dependency task that need to be carried out before it can be started (known as "requirements").
Given a certain number of available independent workers you have to compute a reasonable task distribution such as

- tasks are not executed before their required tasks are completed
- at any given time, there are no idling workers and available tasks (uncompleted tasks with 0 uncompleted requirements)

### API contract

You have been provided with these basic data structures in the `scheduler-api` subproject:

```java
public interface Task extends Iterable<Task> {
    String getName();
    int getEffort();
    List<Task> getRequirements();
}

public interface TaskExecution {
    int getEnd();
    Task getTask();
    int getWorkerId();
    int getStart();
}

public interface Scheduler {
    List<TaskExecution> estimate(Iterable<Task> tasks, int numWorkers);
}
```

The `scheduler-impl` subproject contains the implementation for all these interfaces except
for the `network.contour.task.scheduler.api.Scheduler` interface. 

### What you need to do
To solve this challenge you are supposed to write 
a valid implementation of it yourself and return it in 
`network.contour.task.scheduler.impl.TaskFactoryImpl#newScheduler` in the `scheduler-impl` subproject.


Do NOT alter the content `scheduler-api` subproject as doing that will break the API contract
of this challenge. You are allowed to use third party libraries 
(just add them to the `implementation` configuration of the `scheduler-impl` subproject) but priority
will be given to solutions that do not make use of any additional dependency 
(you don't really need any library to solve this challenge).

### How to test your solution

There is a ready-made junit test class in 
`network.contour.task.scheduler.SchedulerTest` that creates a scheduler and submit
a simple set of tasks to it, feel free to expand it and complicate the test cases
to address all of your testing needs. Note the test currently 
fails with `NullPointerException` as `TaskFactoryImpl.newScheduler()` 
returns `null` but will start working as soon as you change that method to return
an implementation of a `network.contour.task.scheduler.api.Scheduler`


### How to submit your solution

To submit your solution invoke the `:solutionArchive` Gradle task with

```bash
./gradlew solutionArchive
```

from the root directory of this repository, this creates an archive in `build/libs/solution.bin`. 
Send that archive back to us via email at `walter.oggioni@contour.network`.

topological sort
graph A -> B
