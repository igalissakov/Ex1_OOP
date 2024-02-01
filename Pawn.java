public class Pawn extends ConcretePiece {

    private static int pieceCounter = 0; //0-12 Def pieces player 1, 13-36 Att pieces player 2.

    public Pawn(Player player){
        //define player field
        super.player = player;
        //enter unicode of pawn
        super.pieceType = "♟"; //2659 = pawn unicode
        //naming the piece the way they are created, D1 - D13(NO D7 = KING = K7), A1 - A24.
        //toString implement at concretePiece and return the pieceName
        super.pieceName = setPieceName(); //unique key, is the piece name.
        pieceCounter++; //updating the piece counter
    }

    //piece counter.
    private int countPiecesHelper(){
        if(pieceCounter == 37) {pieceCounter = 0;} //when the last piece created, reseat the piece counter for the next game.
        else if(pieceCounter == 6) pieceCounter++; //since number 6 that is actually number 7 piece, saved for the king.
        return pieceCounter;
    }

    //naming the piece the way they are created, D1 - D13(NO D7 = KING = K7), A1 - A24.
    private String setPieceName(){
        String prefixAD = this.player.isPlayerOne() ? "D" : "A";
        String name =  this.player.isPlayerOne() ? prefixAD + (countPiecesHelper() + 1):
                prefixAD + (countPiecesHelper() - 12);
        return name;
    }



}

