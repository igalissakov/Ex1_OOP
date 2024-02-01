import java.util.Comparator;

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