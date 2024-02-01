import java.lang.*;
import java.util.*;

public abstract class Sorts{}

class SortByMovesWinner implements Comparator<MoveNode>{

    @Override
    public int compare(MoveNode nodeA, MoveNode nodeB){
        //first: the winner side
        int compare1 = nodeB.compareTo(nodeA); //check if A the winner or loser side
        //sec: number of moves
        int compare2 = Integer.compare(nodeA.getNumMoves(), nodeB.getNumMoves());
        compare1 = (compare1 == 0) ? compare2 : compare1;
        int compare3 = Integer.compare(nodeA.getPieceNum(),nodeB.getPieceNum());
        if(compare1 == 0) return compare3;
        return compare1;
    }
}



//
class SortByKills implements Comparator<DistanceAndKillsNode> {

    @Override
    public int compare(DistanceAndKillsNode nodeA, DistanceAndKillsNode nodeB){
        //compare the number of kills of each piece
        //1st level comparison number of kills
        int compare1 = Integer.compare(nodeB.getNumOfKills(), nodeA.getNumOfKills());
        //compare by the number of the piece
        //2nd level comparison by piece number
        int compare2 = Integer.compare(nodeA.getPieceNumber(), nodeB.getPieceNumber());
        //check the compressions
        compare1 = (compare1 == 0) ? compare2 : compare1;
        //3rd level comparison by the winner side in case the other compressions were equals.
        if(compare1 == 0) return nodeB.compareTo(nodeA); //check if nodeA is the winner or the loser side.
        return compare1;
    }
}

//sort distance of piece by 1- sum of the distance from top, 2- number of piece from down, 3- piece winner.
class SortByDistance implements Comparator<DistanceAndKillsNode> {

    //sort distance of piece by 1- sum of the distance from top, 2- number of piece from down, 3- piece winner.
    @Override
    public int compare(DistanceAndKillsNode nodeA, DistanceAndKillsNode nodeB) {
        //1st compare distance
        int compare1 = Integer.compare(nodeB.getPieceDistance(), nodeA.getPieceDistance());
        //2nd compare piece number
        int compare2 = Integer.compare(nodeA.getPieceNumber(), nodeB.getPieceNumber());
        //check compression
        compare1 = (compare1 == 0) ? compare2 : compare1;
        //3nd level comparison
        if (compare1 == 0) return nodeB.compareTo(nodeA);
        return compare1;
        }

}

//sort number of steps on a square of difference pieces 1. number of steps from down, 2. by X from down 3. by Y from down
class SortBySquareSteps implements Comparator<StepsNode>{

    @Override
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