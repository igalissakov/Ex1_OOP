import java.util.Comparator;

class SortBySquareSteps implements Comparator<StepsNode> {
    public int compare(StepsNode nodeA, StepsNode nodeB){
        //compare steps
        int compare1 = Integer.compare(nodeB.getNumSteps(),nodeA.getNumSteps());
        // compareTo function at stepsNode class, check if the x is equal if so it compare between the y
        int compare2 = Integer.compare(nodeA.getX(), nodeB.getX());
        //if compare1 = 0 the number of steps equal, then we will sort according the x and then y if the x also will be equal sort by y
        compare1 = (compare1 == 0) ? compare2 : compare1;
        int compare3 = Integer.compare(nodeA.getY(),nodeB.getY());
        if(compare1 == 0) return compare3;
        return compare1;
    }
}