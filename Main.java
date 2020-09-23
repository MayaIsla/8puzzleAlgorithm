public class Main {
    final static private String START_STATE = "13425786";
    final static private String GOAL_STATE = "12345678";


    public static void main(String[] args) {
        String rootState = START_STATE;
        long startTime = System.currentTimeMillis();

        Algorithm search = new Algorithm(new Node(rootState), GOAL_STATE);
        search.breadthFirstSearch();
        search.aStar(null);
        search.iterativeDeepening(10);

        long finishTime = System.currentTimeMillis();
        long totalTime = finishTime - startTime;
        System.out.println("Time  : " + totalTime);


    }
}