package manager;

public class CreateWorkers implements Runnable {
    private int numOfWorkers;


    public CreateWorkers(int numOfWorkers) {
        this.numOfWorkers = numOfWorkers;
    }

    @Override
    public void run() {
        Manager_Ec2 ec2OPS = new Manager_Ec2();
        System.out.printf("number of workers to create is %d\n", numOfWorkers);
        System.out.printf("Starting %d additional worker nodes\n", this.numOfWorkers);
//        ec2OPS.createWorkers(Manager.ec2Client, this.numOfWorkers);
        System.out.println("for now we wont create nothing since its still local");
    }
}

