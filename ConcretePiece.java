public abstract class ConcretePiece implements Piece{

    protected Player player; //player that own this piece
    protected String pieceType; //type of piece
    protected String pieceName;

    //empty constructor
    public ConcretePiece(){
    }

    //taking toString function that extends by the Object class, and override it to return the name of the piece.
    @Override
    public String toString(){return this.pieceName;}

    @Override
    public Player getOwner() {
        return this.player;
    }

    @Override
    public String getType(){
        return this.pieceType;
    }



}
