import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class extra {

  //Checks if jobs are completed
  public static boolean isComplete(ArrayList<job> jobs){
    for(job current: jobs) {
      if (!current.status.equals("terminate")) return false;
    }
    return true;
  }

  //Removes tasks and frees the resources it has (This is for the Banker's algorithm)
  public static void clearjob(job current, int[][] resourcesNeeded, int[][] resourceTable, int[] resources){
      int taskNum = current.jobNumber;
      for(int i = 0; i < resourcesNeeded[taskNum].length; i++){
          resources[i] += resourceTable[taskNum][i];
          resourcesNeeded[taskNum][i] = 0;
      }
  }

  //main compute function. Depending on task type and either banker or FIFO, rach if statement will compute accordingly
  public static void computeInstructions(job t, boolean blocked, int[][] resourceTable, int[] buffer, int[] resources, LinkedList<job> blockedjobs, String type, int[][] maxResources, int[][] resourcesNeeded){
    task request = t.peek();
    String activity = request.activity;
    int jobNumber = t.jobNumber;
    int resourceType = request.resource;
    int units = request.units;
    t.wasChecked = true;

    if(t.delay > 0) t.delay--;

    else{
      if(activity.equals("initiate")) {
        if (type == "fifo")  t.removeTask();
        else if (type == "banker") {
          if(units > resources[resourceType]){
            t.isAborted = true;
            t.isBlocked = false;
            extra.clearjob(t, resourcesNeeded, resourceTable, resources);
            t.cleartasks();
          }
          else{
            maxResources[jobNumber][resourceType] = units;
            resourcesNeeded[jobNumber][resourceType] = units;
            t.removeTask();
          }
        }
      }

      else if(activity.equals("request")){
        if (type == "fifo") {
          //if more units are requested than it has
          if(units > resources[resourceType]){
            //Blocks the job if it is not already blocked
            if(!blocked){
              blockedjobs.add(t);
              t.isBlocked = true;
            }
          }
          else{
            //Allocates resources to the job and removes the same amount from the resources currently avaliable
            resources[resourceType] -= units;
            resourceTable[jobNumber][resourceType] += units;
            t.removeTask();
            t.isBlocked = false;
          }
        }
        else if (type == "banker") {
          //Checks if the job is safe
          if(t.isSafe){
            //Banker's request-resource logic
            if(units <= resourcesNeeded[jobNumber][resourceType] ){
              if(units <= resources[resourceType]){
                //Allocates resources to the job and removes the same amount from the number of resources avaliable
                resources[resourceType] -= units;
                resourceTable[jobNumber][resourceType] += units;
                resourcesNeeded[jobNumber][resourceType] -= units;
                t.removeTask();
                t.isBlocked = false;
              }
              else{
                if(!blocked){
                  //Adds the job to the blocked jobs queue
                  blockedjobs.add(t);
                  t.isBlocked = true;
                }
              }
            }

            //Aborts jobs if resources exceed the amount requested
            else{
              t.isAborted = true;
              t.isBlocked = false;
              clearjob(t, resourcesNeeded, resourceTable, resources);
              t.cleartasks();
            }
          }
          //Blocks the job if it is not considered safe
          else if(!blocked){
            blockedjobs.add(t);
            t.isBlocked = true;
          }
        }
      }

      else if (activity.equals("compute")){
        t.delay = resourceType - 1;
        t.removeTask();
      }

      else if(activity.equals("release")){
        //Frees the resources from the job and lets them be avaliable for the next cycle.
        if (type == "banker") resourcesNeeded[jobNumber][resourceType] += resourceTable[jobNumber][resourceType];
        buffer[resourceType] += resourceTable[jobNumber][resourceType];
        resourceTable[jobNumber][resourceType] = 0;
        t.removeTask();
      }
      else if(activity.equals("terminate")){
        if (type == "banker") clearjob(t, resourcesNeeded, resourceTable, resources);
        t.removeTask();
      }
    }
    if(!t.status.equals("terminate"))
    t.timeTaken++;
  }


  //Prints information for each job and an overall
  public static void printOut(ArrayList<job> jobs){
    int totalTimeTaken = 0;
    int totalWaitTime = 0;

    for(job current : jobs){
      if(!current.isAborted){
        totalTimeTaken += current.timeTaken;
        totalWaitTime += current.waitingTime;
      }
      System.out.println(current.information());
    }
    System.out.println(String.format("Total  %d  %d  %.0f%%", totalTimeTaken, totalWaitTime, 100 * (totalWaitTime / (float) totalTimeTaken)));
  }

}
