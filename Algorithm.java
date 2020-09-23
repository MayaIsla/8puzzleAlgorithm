import java.util.*;

public class Algorithm {
    private Node root;
    private String goalSate;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public String getGoalSate() {
        return goalSate;
    }

    public void setGoalSate(String goalSate) {
        this.goalSate = goalSate;
    }

    public Algorithm(Node root, String goalSate) {
        this.root = root;
        this.goalSate = goalSate;
    }


    //******************************************************************************************************
    //    Breadth first algorithm


    public void breadthFirstSearch() {
        // stateSet is a set that contains node that are already visited
        Set<String> stateSets = new HashSet<String>();
       //int totalCost = 0;
        int time = 0;
        Node node = new Node(root.getState());
        Queue<Node> queue = new LinkedList<Node>();
        Node currentNode = node;
        while (!currentNode.getState().equals(goalSate)) {
            stateSets.add(currentNode.getState());
            List<String> nodeSuccessors = Child.getSuccessors(currentNode.getState());
            for (String n : nodeSuccessors) {
                if (stateSets.contains(n))
                    continue;
                stateSets.add(n);
                Node child = new Node(n);
                currentNode.addChild(child);
                child.setParent(currentNode);
                queue.add(child);

            }
            currentNode = queue.poll();
            time += 1;
        }

        Child.printSolution(currentNode, stateSets, root, time);

    }

    /**
     * A* Algorithm 
     */
    //*************************************************************************************************
    public void aStar(Heuristic heuristic) {
        Set<String> stateSets = new HashSet<String>();
        int time = 0;
        Node node = new Node(root.getState());
        node.setTotalCost(0);
       nodeComparator nodePriorityComparator = new nodeComparator();
        PriorityQueue<Node> nodePriorityQueue = new PriorityQueue<Node>(10, nodePriorityComparator);
        Node currentNode = node;
        while (!currentNode.getState().equals(goalSate)) {
            stateSets.add(currentNode.getState());
            List<String> nodeSuccessors = Child.getSuccessors(currentNode.getState());
            for (String n : nodeSuccessors) {
                if (stateSets.contains(n))
                    continue;
                stateSets.add(n);
                Node child = new Node(n);
                currentNode.addChild(child);
                child.setParent(currentNode);

                if (heuristic == Heuristic.H_ONE)
                    child.setTotalCost(currentNode.getTotalCost() + Character.getNumericValue(child.getState().charAt(child.getParent().getState().indexOf('0'))), heuristicOne(child.getState(), goalSate));
                else if (heuristic == Heuristic.H_TWO)
                    child.setTotalCost(currentNode.getTotalCost() + Character.getNumericValue(child.getState().charAt(child.getParent().getState().indexOf('0'))), heuristicTwo(child.getState(), goalSate));
                else if (heuristic == Heuristic.H_THREE)
                   child.setTotalCost(currentNode.getTotalCost() + Character.getNumericValue(child.getState().charAt(child.getParent().getState().indexOf('0'))), heuristicThree(child.getState(), goalSate));
                   nodePriorityQueue.add(child);
            }
            currentNode = nodePriorityQueue.poll();
            time += 1;
        }
        Child.printSolution(currentNode, stateSets, root, time);
    }


    private int heuristicThree(String state, String goalSate2) {
        return 0;
    }

    /**
     * Iterative Deepening Search algorithm
     * 
     */
    //====================================================================================================
    public void iterativeDeepening(int depthLimit) {
        Node currentNode = new Node(root.getState());
        boolean solutionFound = false;
      
        Set<String> stateSets = new HashSet<String>();
        Set<String> totalVisitedStates = new HashSet<>();
        int time = 0;
        for (int maxDepth = 1; maxDepth < depthLimit; maxDepth++) {
           
            stateSets.clear();
            
            Vars<Node> mainQueue = new Vars<>();
           
            Vars<Node> successorsQueue = new Vars<>();
            Node node = new Node(root.getState());
            mainQueue.enqueue(node);
            currentNode = node;
            List<String> nodeSuccessors = null;
            stateSets.add(currentNode.getState());
            while (!mainQueue.isEmpty()) {
                currentNode = mainQueue.dequeue();
                time += 1;
                if (currentNode.getState().equals(goalSate)) {
                    solutionFound = true;
                    break;
                }
                if (currentNode.getDepth() < maxDepth) {
                    nodeSuccessors = Child.getSuccessors(currentNode.getState());
                    for (String n : nodeSuccessors) {
                        if (stateSets.contains(n))
                            continue;

                        stateSets.add(n);
                        Node child = new Node(n);
                        currentNode.addChild(child);
                        child.setParent(currentNode);
                        child.setVisited(true);
                        child.setDepth(currentNode.getDepth() + 1);
                        successorsQueue.enqueue(child);

                    }
                   
                    mainQueue.addQueue(successorsQueue);
                
                    successorsQueue.clear();
                }
            }

            if (solutionFound)
                break;
            totalVisitedStates.addAll(stateSets);
        }
        if (!solutionFound)
            System.out.println("No solution Found!");
        else {
           Child.printSolution(currentNode, totalVisitedStates, root, time);


        }


    }


    private int heuristicOne(String currentState, String goalSate) {
        int difference = 0;
        for (int i = 0; i < currentState.length(); i += 1)
            if (currentState.charAt(i) != goalSate.charAt(i))
                difference += 1;
        return difference;
    }

    
    private int heuristicTwo(String currentState, String goalSate) {
        int difference = 0;
        for (int i = 0; i < currentState.length(); i += 1)
            for (int j = 0; j < goalSate.length(); j += 1)
                if (currentState.charAt(i) == goalSate.charAt(j))
                    difference = difference + ((Math.abs(i % 3 - j % 3)) + Math.abs(i / 3 + j / 3));
        return difference;
    }


    

}