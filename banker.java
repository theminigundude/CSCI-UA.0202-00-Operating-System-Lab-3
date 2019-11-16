import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class banker{

  public static void main(String args[]){

    //variables declaration
    int numjobs = 0, numResources = 0;
    int[] bankersResources, fifoResources;
    ArrayList<job> bankersjobs = new ArrayList<job>();
    ArrayList<job> fifojobs = new ArrayList<job>();

    try{
      //read in file and # of tasks and # of resource type
      File file = new File(args[0]);
      Scanner sc = new Scanner(file);
      numjobs = sc.nextInt();
      numResources = sc.nextInt();
      fifoResources = bankersResources = new int[numResources];

      //Initializes jobs array by creating a job based on numjobs
      for(int i = 0; i < numjobs; i++){
        bankersjobs.add(new job(i));
        fifojobs.add(new job(i));
      }

      // Allocates each resource with its maximum number of units.
      for(int i = 0; sc.hasNextInt(); i++){
        int units = sc.nextInt();
        fifoResources[i] = bankersResources[i] = units;
      }

      //add tasks to each instructions list
      while(sc.hasNext()){
          String activity = sc.next();
          int jobNumber = sc.nextInt();
          task current = new task(activity, jobNumber, sc.nextInt(), sc.nextInt());
          bankersjobs.get(jobNumber - 1).add(current);
          fifojobs.get(jobNumber - 1).add(current);
      }
      //two main function calls
      fifo.compute(fifoResources, fifojobs);
      banking.compute(bankersResources, bankersjobs);
      System.out.println("");

    }
    catch(FileNotFoundException e){
      System.out.println("Cannot find file");
    }
  }

}
