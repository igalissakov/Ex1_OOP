public class Movements {

    private final Position CURRENT_POSITION;
    private final Position PREVIOUS_POSITION;
    private final boolean IS_PLAYER_TWO_TURN;
    private final Piece PIECE;

    private Position[] capturedPosition;
    private Piece[] capturedPieces;


    public Movements(Position current, Position previous,Piece piece, boolean player2Turn){
        this.CURRENT_POSITION = new Position(current); //position after the move
        this.PREVIOUS_POSITION = new Position(previous); //position after the change
        this.PIECE = piece;
        this.IS_PLAYER_TWO_TURN = player2Turn; //check player turn.
        this.capturedPosition = new Position[4];
        this.capturedPieces = new Piece[4];
    }


    public Position getCurrentPosition(){ return this.CURRENT_POSITION;}

    public Position getPreviousPosition(){ return this.PREVIOUS_POSITION;}

    public boolean getPlayerTurn(){ return this.IS_PLAYER_TWO_TURN;}

    public Position[] getCapturedPosition() {
        return this.capturedPosition;
    }

    public Piece[] getCapturedPieces() {
        return this.capturedPieces;
    }

    public Piece getPiece(){
        return this.PIECE;
    }

    //if enemy get captured, saving its positions.
    public void setCapture(Position pos,Piece piece){
        for (int i = 0; i < 4; i++){
            if(this.capturedPosition[i] == null){
                this.capturedPosition[i] = new Position(pos);
                this.capturedPieces[i] = piece;
                //todo: increase and decrease number of captures
                break;
            }
        }
    }


}
