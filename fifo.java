import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class fifo{

  //Aborts jobs until a job is able to complete its tasks
  private static void abortjobs(ArrayList<job> jobs, LinkedList<job> blockedjobs, int[] resources, int[][] resourceTable){

    int blockedSize = blockedjobs.size();

    for(int i = 0; i < jobs.size(); i++){
      job curjob = jobs.get(i);
      job nextjob = jobs.get(i);
      if(curjob.isBlocked){
        blockedjobs.remove(curjob);
        for(int j = i; j < jobs.size(); j++){
          nextjob = jobs.get(j);
          if(nextjob.isBlocked)
          break;
        }
        int curjobNumber = curjob.jobNumber;
        int resourcesRequired = nextjob.peek().units;
        int resourceType = nextjob.peek().resource;
        //Clears all entries in the current job
        for(int j = 0; j < resources.length; j++){
          resources[j] += resourceTable[curjobNumber][j];
          resourceTable[curjobNumber][j] = 0;
        }

        curjob.isAborted = true;
        curjob.isBlocked = false;
        curjob.cleartasks();

        if(resourcesRequired <= resources[resourceType])
        break;
      }
    }
  }

  //Computes the number of jobs that are running
  private static int numRunning(ArrayList<job> jobs){
    int running = 0;
    for(job current: jobs) {
      if (!current.status.equals("terminate")) {
        running += 1;
      }
    }
    return running;
  }

  //main FIFO function
  public static void compute(int[] resources, ArrayList<job> jobs){
    int[] buffer = new int[resources.length];
    //placeholder for compute function
    int[][] temp = new int[0][0];
    int[][] resourceTable = new int[jobs.size()][resources.length];
    LinkedList<job> blockedjobs = new LinkedList<job>();
    job curjob;

    for(int curCycle = 0; !extra.isComplete(jobs); curCycle++){

      //Handles jobs that are blocked
      if(blockedjobs.size() > 0){
        //Checks if blocked jobs are able to be completed
        for(int i = 0; i < blockedjobs.size(); i++){
          curjob = blockedjobs.get(i);
          curjob.waitingTime++;
          extra.computeInstructions(curjob, true, resourceTable, buffer, resources, blockedjobs, "fifo", temp, temp);
        }
      }

      //Removes any jobs that are no longer blocked
      for(int j = 0; j < jobs.size(); j++){
        curjob = jobs.get(j);
        if(!curjob.isBlocked){
          int index = blockedjobs.indexOf(curjob);
          if(index >= 0){
            blockedjobs.remove(index);
          }
        }
      }

      //Compute jobs depending on given instruction
      for(int i = 0; i < jobs.size(); i++){
        curjob = jobs.get(i);
        if(curjob.isAvailable()) extra.computeInstructions(curjob, false, resourceTable, buffer, resources, blockedjobs, "fifo", temp, temp);
      }

      //Abort jobs until there are enough resources to continue
      if(blockedjobs.size() == numRunning(jobs)){
        abortjobs(jobs, blockedjobs, resources, resourceTable);
      }

      //Update the resources if a job released its resources
      for(int i = 0; i < buffer.length; i++){
        resources[i] += buffer[i];
        buffer[i] = 0;
      }

      //Each job resets so that it is no longer checked
      for(job current : jobs) current.wasChecked = false;
    }

    System.out.println("\nFIFO\n");
    extra.printOut(jobs);
  }
}
