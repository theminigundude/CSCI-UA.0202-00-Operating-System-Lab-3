import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.lang.Throwable;

public class banking{

  //Checks if the the job is valid for given set
  private static boolean isValid(int[] workingSet, int[] resourcesNeeded){

    if(workingSet.length != resourcesNeeded.length) return false;
    for(int i = 0; i < workingSet.length; i++){
      workingSet[i] -= resourcesNeeded[i];
      if(workingSet[i] < 0){
        workingSet[i] += resourcesNeeded[i];
        return false;
      }
    }
    return true;
  }

  //Checks if the job is safe for current cycle
  private static void safetyAlgorithm(job curjob, int[] resources, int[][] resourcesNeeded, int[][] resourceTable){
    int[] workingSet = Arrays.copyOf(resources, resources.length);

    if(!curjob.isComplete()){
      String activity = curjob.peek().activity;
      if(isValid(workingSet, resourcesNeeded[curjob.jobNumber])
      && activity.equals("request"))
      curjob.isSafe = true;
    }
  }

  public static void compute(int[] resources, ArrayList<job> jobs){
    int[] buffer = new int[resources.length];
    int[][] maxResources = new int[jobs.size()][resources.length];
    int[][] resourceTable = new int[jobs.size()][resources.length];
    int[][] resourcesNeeded = new int[jobs.size()][resources.length];
    LinkedList<job> blockedjobs = new LinkedList<job>();
    job curjob;

    for(int curCycle = 0; !extra.isComplete(jobs) ; curCycle++){

      //Handles jobs that are blocked
      if(blockedjobs.size() > 0){
        //Checks if blocked jobs are able to be completed
        for(int i = 0; i < blockedjobs.size(); i++){
          curjob = blockedjobs.get(i);
          curjob.waitingTime++;
          //Runs the safety algorithm on the jobs to determine if it can be completed
          safetyAlgorithm(curjob, resources, resourcesNeeded, resourceTable);
          extra.computeInstructions(curjob, true, resourceTable, buffer, resources, blockedjobs, "banker", maxResources, resourcesNeeded);
        }
      }

      //Removes any jobs that are no longer blocked
      for(int j = 0; j < jobs.size(); j++){
        curjob = jobs.get(j);
        if(!curjob.isBlocked){
          int index = blockedjobs.indexOf(curjob);
          if(index >= 0)
          blockedjobs.remove(index);
        }
      }

      //Compute jobs depending on the given instructions
      for(int i = 0; i < jobs.size(); i++){
        curjob = jobs.get(i);
        if(curjob.isAvailable()){
          //Runs the safety algorithm on the jobs to determine if it can be completed
          safetyAlgorithm(curjob, resources, resourcesNeeded, resourceTable);
          //Computes instructions
          extra.computeInstructions(curjob, false, resourceTable, buffer, resources, blockedjobs, "banker", maxResources, resourcesNeeded);
        }
      }


      //Update the resources if a job released its resources
      for(int i = 0; i < buffer.length; i++){
        resources[i] += buffer[i];
        buffer[i] = 0;
      }

      //Each job resets so that it is no longer considered "checked" and safe
      for(job current : jobs){
        current.wasChecked = false;
        current.isSafe = false;
      }
    }

    System.out.println("\nBankers\n");
    extra.printOut(jobs);
  }
}
