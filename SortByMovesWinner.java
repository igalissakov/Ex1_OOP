import java.util.Comparator;

class SortByMovesWinner implements Comparator<MoveNode> {
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
