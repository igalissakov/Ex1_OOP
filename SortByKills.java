import java.util.Comparator;

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