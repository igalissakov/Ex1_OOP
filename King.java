public class King extends ConcretePiece {

    //default constructor
    public King(Player player){
        super.player = player;
        super.pieceType = "♔"; //2654 = White King unicode
        super.pieceName = "K7";
    }

}

