public class task{

    public int taskNumber, resource, units;
    public String activity;

    //task is a custom data structure I created to store all the lines of instruction that is read
    //It will be stored in the job class for each job input.
    public task(String activity, int number, int resource, int units){
        this.activity = activity;
        this.taskNumber = number - 1;
        this.units = units;
        if (activity.equals("compute") || activity.equals("terminate")) this.resource = resource;
        else this.resource = resource - 1;
    }

    public String toString(){
        return String.format("Activity: %s\tTask Number: %d\tResource: " +"%d\tUnits: %d\n",
                activity, taskNumber, resource, units);
    }
}
