import java.util.LinkedList;

public class job{

    public boolean isAborted, isBlocked, wasChecked, isSafe;
    public int timeTaken, waitingTime, jobNumber, delay;
    public LinkedList<task> tasks;
    public String status;

    //This is a custom class I have made to store all the jobs that come in. This inlcude a list of all the tasks.
    public job(int number){
        jobNumber = number;
        isAborted = isBlocked = wasChecked = isSafe = false;
        timeTaken = waitingTime = delay = 0;
        status = "";
        tasks = new LinkedList<task>();
    }

    //returns first element in LinkedList tasks
    public task peek() {
      return tasks.peek();
    }

    //Add to list
    public void add(task i) {
      tasks.addLast(i);
    }

    //removes a task from the list and updates the status of the task
    public void removeTask(){
        status = tasks.peek().activity;
        tasks.removeFirst();
    }

    //checks if the task is available for a cycle
    public boolean isAvailable(){
        return !isAborted && !status.equals("terminate") && !isBlocked && !wasChecked;
    }

    //checks if the task completed its tasks
    public boolean isComplete(){
        return tasks.size() == 0 || tasks.peek().activity.equals("terminate");
    }

    //Removes all tasks for the task
    public void cleartasks(){
        tasks.clear();
        status = "terminate";
    }

    //function to print info
    public String information(){
        if (isAborted) return String.format("Task  %d: aborted", jobNumber + 1);
        else return String.format("Task %d:  %d  %d  %.0f%%", jobNumber + 1, timeTaken, waitingTime, (waitingTime / (float) timeTaken) * 100);
    }
}
