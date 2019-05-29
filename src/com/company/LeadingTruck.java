public class LeadingTruck extends Vehicle {

    public static final double TRUCK_WIDTH = 4;
    public static final double TRUCK_LENGTH = 10;
    public static double INIT_X = 10;
    public static double INIT_Y = 0; //right lane
    public static double INIT_V = 30;

    private LinkedList<Integer> roadTrainList;

    public LeadingTruck(int nodeID) {
        super(nodeID);
        this.setGPS(new GPS(INIT_X, INIT_Y));
        this.setWidth(TRUCK_WIDTH);
        this.setLength(TRUCK_LENGTH);
        this.setVelocity(INIT_V);
        roadTrainList = new LinkedList<Integer>();
        roadTrainList.add(nodeID);
    }

    @Override
    public void setAcceleration() {
        this.setAcceleration(Math.random() * 2 - 1);
    }

}