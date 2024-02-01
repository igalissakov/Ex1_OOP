import java.io.IOException;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private final int BOARD_SIZE = 11;
    private final int NUMBER_OF_PIECES = 37;
    private final int NUMBER_OF_PIECES_PLAYER1 = 13;

    //board settings
    private Piece[][] boardPieces; //board main game
    private String[][] boardStepsCounter; //board count steps on every square, count if the piece did not step there already.
    private int[] captureCounterArray; //capture counter, index array 0 - 12, def player 1, 13 - 36 att player 2.
    private int[] disPiecesArray; //counting the distance walked every piece, index array 0 - 12, def player 1, 13 - 36 att player 2.


    private Stack<Movements> movementsStack;
    private ConcretePlayer player1; //defender
    private ConcretePlayer player2; //attacker (first move in new game)
    private Position kingPosition;
    private boolean player2Turn;
    private boolean undoFlag;
    private boolean newGameFlag;

    //constructor, initialize game logic
    public GameLogic(){
        this.player1 = new ConcretePlayer(false); //defender
        this.player2 = new ConcretePlayer(true); //attacker (first move in new game)
        this.boardPieces = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
        initGame();
    }

    //initial the board to a new game.
    private void initGame(){
        resetBoard(); //reset the board to null;
        this.boardStepsCounter = new String[BOARD_SIZE][BOARD_SIZE];
        this.captureCounterArray = new int[NUMBER_OF_PIECES]; //number of pieces - default value is 0.
        this.disPiecesArray = new int[NUMBER_OF_PIECES]; //counting the distance walked every piece - default value is 0.
        this.player2Turn = true; //set attacker - player 2 first turn.
        this.undoFlag = false; //if undo is on, true at the end return false.
        this.newGameFlag = true; //new game started
        this.movementsStack = new Stack<Movements>(); //init the stacks of moves
        this.kingPosition = new Position(5,5);
        createPlayer1Pieces(); //create defender - player 1 pawns and king
        createPlayer2Pieces();  //create attacker - player 2 pawns
    }

    //reset the board pieces, all the cells defined as null.
    private void resetBoard(){
        for (int x = 0; x < BOARD_SIZE; x++){
            for (int y = 0; y < BOARD_SIZE; y++){
                boardPieces[x][y] = null;
            }
        }
    }

    //update boards - main board: boardPieces, steps counter board: stepsBoardCounter
    //NOTE! capture counter
    private void updateGameBoards(Piece piece, Position pos){
        //update the main board game
        this.boardPieces[pos.getX()][pos.getY()] = piece;
        if(newGameFlag) this.movementsStack.add(new Movements(pos,pos,piece,this.player2Turn));
        //update the steps board game, count how many different pieces, steps at specific square.
        if(piece != null) {
            if (this.boardStepsCounter[pos.getX()][pos.getY()] == null) { //if the square is null, add piece step to the counter else
                this.boardStepsCounter[pos.getX()][pos.getY()] = piece.toString() + ",";
            } else if (!(this.boardStepsCounter[pos.getX()][pos.getY()].contains(piece.toString()))) //if the square is not null, check if it contains the piece string, if not add it.
                this.boardStepsCounter[pos.getX()][pos.getY()] += piece.toString() + ",";
        }
    }

    private void updateDistPieces(Piece piece, Position curr, Position prev){
        if(piece != null){
            if(!(undoFlag)) {
                this.disPiecesArray[pieceIndex(piece)] += curr.dist(prev);
            }else{
                this.disPiecesArray[pieceIndex(piece)] += (-1*curr.dist(prev));
            }
        }

    }

    //update the number of piece captured, when undo decrease when capture increase.
    private void updateCaptureCounter(Piece piece, int numOfCaptured){
        if(piece != null){
            this.captureCounterArray[pieceIndex(piece)] += numOfCaptured;
        }
    }

    //calculate the index at array of every piece at the board
    private int pieceIndex(Piece piece){
        if(piece != null) {
            if(piece.getType().equals("♔")) return 6; //king constant index at the array(KING is number 7 but since the array start from 0 king is index 6)
            String pieceString = piece.toString().substring(1);
            if(piece.getOwner().isPlayerOne()) {
                return Integer.parseInt(pieceString) - 1; // -1 becuase the string index start from 1 and the array from 0
            }else{
                return Integer.parseInt(pieceString) + NUMBER_OF_PIECES_PLAYER1 - 1; // +13 because the first 13 piece are player 1 piece, and -1 same reason as wrote at condition 1.
            }
        }return -1;
    }

    // creating the defender(pawns and king) - Player 1
    /*                   [3,5]
                    [4,4][4,5][4,6]
               [5,3][5,4][5,5][5,6][5,7]
                    [6,4][6,5][6,6]
                         [7,5]
  */
    private void createPlayer1Pieces(){
        for(int y = 3; y < 8; y++) {
            for(int x = 3; x < 8; x++) {
                //[5,5] KING
                if(x == 5 && y == 5) {  //set King at [5,5]
                    updateGameBoards(new King(getFirstPlayer()), new Position(x,y));
                }
                //Pawns(around the King) - [4,4],[4,5],[4,6],[5,4],[5,6],[6,4],[6,5],[6,6]
                else if(x > 3 && y > 3 && x < 7 && y < 7) {
                    updateGameBoards(new Pawn(getFirstPlayer()), new Position(x,y));
                }
                //Pawns - [3,5], [5,3], [5,7], [7,5]
                else if((x + 2 == y || y + 2 == x)){
                    updateGameBoards(new Pawn(getFirstPlayer()), new Position(x,y));
                }
            }
        }
    }

    //  creating the attacker - Player 2
    private void createPlayer2Pieces(){
        int consStartIndex = 0;
        int consEndIndex = 10;

        for(int y = 3; y < 8; y++){ //[3,0]...[7,0]
            updateGameBoards(new Pawn(getSecondPlayer()),new Position(y,consStartIndex));
        }

        updateGameBoards(new Pawn(getSecondPlayer()),new Position(5,1));//[1,5]

        for(int x = 3; x < 8; x++) { // [3,0],[3,10]..[5,0][5,1][5,9],[5,10]....[7,0][7,10]

            if (x == 5) { //[5,0][5,1][5,9],[5,10]
                updateGameBoards(new Pawn(getSecondPlayer()),new Position(consStartIndex,x));
                updateGameBoards(new Pawn(getSecondPlayer()),new Position(consStartIndex+1,x));
                updateGameBoards(new Pawn(getSecondPlayer()),new Position(consEndIndex-1,x));
                updateGameBoards(new Pawn(getSecondPlayer()),new Position(consEndIndex,x));
            }else { // [3,0],[3,10]......[7,0][7,10]
                updateGameBoards(new Pawn(getSecondPlayer()),new Position(consStartIndex,x));
                updateGameBoards(new Pawn(getSecondPlayer()),new Position(consEndIndex,x));
            }
        }
        updateGameBoards(new Pawn(getSecondPlayer()),new Position(5,9));//[9,5]
        for(int i = 3; i < 8; i++){
            updateGameBoards(new Pawn(getSecondPlayer()),new Position(i,consEndIndex));
        }
        this.newGameFlag = false;
    }

    //check if the move is valid, return true if not.
    private boolean isInvalidMove(Position a, Position b){
        // position out of boundaries bigger then the size of the board
        if(a.getX() >= getBoardSize() || b.getX() >= getBoardSize() || a.getY() >= getBoardSize() || b.getY() >= getBoardSize()){
            return true;
        }

        // position out of boundaries smaller than the size of the board
        if(a.getX() < 0 || b.getX() < 0  || a.getY() < 0  || b.getY() < 0 ){
            return true;
        }

        if(a.getX() != b.getX() && a.getY() != b.getY()){ //illegal move (move not in a row or column)
            return true;
        }

        if(a.getX() == b.getX() && a.getY() == b.getY()){ //same position (click on the same position)
            return true;
        }

        //illegal move for pawns: [0,0],[0,10],[10,0],[10,10]
        if(getPieceAtPosition(a) instanceof Pawn) {
            if (b.getX() == 0 && b.getY() == 0) return true; // [0,0]
            if (b.getX() == 10 && b.getY() == 0) return true;// [10,0]
            if (b.getX() == 0 && b.getY() == 10) return true;// [0,10]
            return b.getX() == 10 && b.getY() == 10;// [10,10]
        }
        return false;
    }

    //check whose turn is it, and the owner of the piece
    private boolean isItMyPieceAndTurn(Position a){
        if(getPieceAtPosition(a) == null) return false;
        boolean isPlayer1Piece = getPieceAtPosition(a).getOwner().isPlayerOne();//is it the first player piece?
        //case1: player 2 turn and player 1 piece = illegal move, case2: player 1 turn and player 2 piece = illegal move - every case like this will return true.
        return ((isSecondPlayerTurn() && !(isPlayer1Piece)) || (!(isSecondPlayerTurn()) && isPlayer1Piece));
    }

    private boolean checkCapture(Position a, int stepX, int stepY){
        Position step = new Position(a, stepX, stepY);
        if (!(isInvalidMove(a,step))){ //get in, only if the step is in a valid attack position

            //if the position at step is null, it can occur because the piece at the boarders of the board game
            // in the other hand if it's the king that god cornered we need to check for player 2 win opportunity
            if (!(getPieceAtPosition(step) == null)){
                if (!(isItMyPieceAndTurn(step)) && getPieceAtPosition(step) instanceof  Pawn) {
                    //if a is the king and next step is not player1 piece, then king is blocked.
                    //at move function we made sure that after player2 move, the turn will change
                    //so the king will always be at the same turn and check for enemy's surround him
                    if(getPieceAtPosition(a) instanceof  King) return true;
                    Position step2 = new Position(step, stepX, stepY);

                    if (isInvalidMove(a, step2)) {
                        capture(step); //capture if the piece got cornered
                        return true;

                    } else if (isItMyPieceAndTurn(step2) && getPieceAtPosition(step2) instanceof  Pawn) {
                        capture(step); //capture the piece if we have 2 pieces between the enemy piece
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        //if the step checked is an invalid move, and it's the king, return true since he is being blocked
        //and it's a winning option
        return getPieceAtPosition(a) instanceof King;
    }

    private void capture(Position posCapture){
        this.movementsStack.peek().setCapture(posCapture,getPieceAtPosition(posCapture)); //adding to the last movement, the piece that been captured
        this.boardPieces[posCapture.getX()][posCapture.getY()] = null; //changing the captured position to null, so the piece will be removed from the game board.
    }

    //define if its valid move, updating the board(2D array), checking if the enemy has defeated according to the new position.
    @Override
    public boolean move(Position a, Position b){
        //check illegal moves, if illegalMove return true, then return false.(also illegal specific for pawns)
        if(isInvalidMove(a,b) || !(isItMyPieceAndTurn(a))){
            return false;
        }

        //calculate valid logic
        boolean isHorizontal = a.getY() != b.getY(); //check the direction move, the direction is horizontal if the columns are not equal
        int startPosition = isHorizontal ? a.getY() : a.getX(); //define the index, start position of the piece, depend on the direction.
        int endPosition = isHorizontal ? b.getY() : b.getX();   //define the index, end position of the piece, depend on the direction.
        int FixedPosition = isHorizontal ? a.getX() : a.getY(); //saving the fixed position according to the direction of the movement
        int step = startPosition < endPosition ? 1 : -1; //step = 1; //define if to move forward or backward.

        //loop, check if every position from the start to the end is clear from pieces, else return false - illegal move
        for (int checkPosition = startPosition + step; checkPosition != endPosition+step; checkPosition = checkPosition + step) {
            //depend on the direction, define row and column index's of the next square, for checking if there are pieces between 2 positions.
            int column = isHorizontal ? FixedPosition : checkPosition; //horizontal movement
            int row = isHorizontal ? checkPosition : FixedPosition; //vertical movement
            Position newPosition = new Position(row, column); //define the new position
            if (getPieceAtPosition(newPosition) != null) { //check if there are pieces at this position
                return false; //return false if there is a piece at the road to position b.
            }
        }

        //checking if it's the king and updating king position before changing the board
        if(getPieceAtPosition(a) instanceof King) this.kingPosition = b; //if it's the king we need to update the field representing its position

        //updating the board according to the position moves
        updateGameBoards(getPieceAtPosition(a),b);
        updateGameBoards(null,a);
        updateDistPieces(getPieceAtPosition(b),b,a); //after update the board game, the piece is at position b
        //add the move to stack, if its undo move then its poped again at undo function
        if(!(undoFlag)) {
            //the current position is b and the previous position is a
            this.movementsStack.push(new Movements(b, a, getPieceAtPosition(b), this.player2Turn));

            //since the king cant capture, check if it's not the king, only then calling checkCapture function
            //to check if there are pieces of the other player that have been captured.
            //checking if its king position even after I update, because the function check
            //the position king field, that is updated only at the else.
            if (getPieceAtPosition(b) instanceof Pawn) {
                //todo: update numOfCapture if return true + update the number of captured by every piece
                int numOfCapture = 0;
                if (checkCapture(b, 1, 0)) numOfCapture++;
                if (checkCapture(b, -1, 0)) numOfCapture++;
                if (checkCapture(b, 0, 1)) numOfCapture++;
                if (checkCapture(b, 0, -1)) numOfCapture++;
                if (numOfCapture > 0) updateCaptureCounter(getPieceAtPosition(b), numOfCapture);
            }
        }

        this.player2Turn = !(this.player2Turn); //change the player turns
       isGameFinished();

        return true;
    }


    //return the piece that located at given position in the table(2D array)
    @Override
    public Piece getPieceAtPosition(Position position){
        return boardPieces[position.getX()][position.getY()];
    }

    //return player 1
    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    //return player 2
    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    //return true if its player2 turn, else false
    @Override
    public boolean isSecondPlayerTurn(){
        return this.player2Turn;
    }

    //check if the game is finished, according to the turn check win for player 1 or 2, and increase number of wins.
    @Override
    public boolean isGameFinished(){
        //changing the turn before isGameFinished function, that why the boolean is set at NOT
        if(!(isSecondPlayerTurn())){
            if (checkCapture(this.kingPosition,1,0) & checkCapture(this.kingPosition,-1,0)
                & checkCapture(this.kingPosition,0,1) & checkCapture(this.kingPosition,0,-1)) {
                GameMetadata gameMetadata = new GameMetadata(this.movementsStack, this.boardStepsCounter,this.disPiecesArray, this.captureCounterArray, !this.player2Turn);
                gameMetadata.sortAndPrint();
                return true;
            }
            return false;
        }else{
            //checking when its the first player turn if the condition for winning occurs.
            if (getPieceAtPosition(new Position(0,0)) != null
                    || getPieceAtPosition(new Position(10,0)) != null
                    || getPieceAtPosition(new Position(0,10)) != null
                    || getPieceAtPosition(new Position(10,10)) != null ){
                GameMetadata gameMetadata = new GameMetadata(this.movementsStack, this.boardStepsCounter,this.disPiecesArray, this.captureCounterArray, !this.player2Turn);
                gameMetadata.sortAndPrint();
                return true;
            }
            else return false;
        }
    }

    // reset the board pieces(Array 2D), and the information of the players.
    @Override
    public void reset() {
        if(!isGameFinished()) {
            //if game isn't finished and reset was activated, It's because we want to reset the game and the player data
            this.player1 = new ConcretePlayer(false); //defender - player 1
            this.player2 = new ConcretePlayer(true); //attacker - player 2 (first move in new game)
        }else { // else
            // if its player 2 turns, player 1's won
            // since the turn change after the move, the player at the last move won. this.player1.addWin(); //increase number of wins
            if(this.player2Turn ) {
                this.player1.addWin();
            }else {
                this.player2.addWin();
            }
            //activating the metadata statics sorting and printing after the game is finished(not restarted)
            }
        //reset all the proper settings for a new logic game
        initGame();
    }

    //return to the last move that has been made.
    @Override
    public void undoLastMove(){
    //todo: Idea, export the last object of move at the stack, consider another stack of enemy's to export, also change the player turn.
        this.undoFlag = true;
        if(!(this.movementsStack.isEmpty())) {
            Movements lastMove = this.movementsStack.pop(); //remove and return the last move that made and saved t the stack
            Position currentPos = lastMove.getCurrentPosition(); //current position
            Position previousPos = lastMove.getPreviousPosition(); //previous position
            this.player2Turn = lastMove.getPlayerTurn(); //return the last player turn

            move(currentPos, previousPos); //return to the last position

            //if it's not the king, we need to check if their any captured enemy we need to return.
            if(getPieceAtPosition(previousPos) instanceof  Pawn){
                Position[] capturedPos = lastMove.getCapturedPosition();
                Piece[] capturedPieces = lastMove.getCapturedPieces();
                for(int i = 0; i < 4; i++){
                    if(capturedPos[i] == null || capturedPieces[i] == null){
                        break;
                    }else{
                        //edit number of captures made by a piece
                        //returning the piece that was captured
                        //todo: NOTE TO REMEMMBER capture save in movement so when using pop the object disappear with his number of captures.
                        //todo: if I wont fix the problame at movement change need to be done.
                        updateGameBoards(capturedPieces[i],capturedPos[i]);
                        updateCaptureCounter(getPieceAtPosition(previousPos),-1);
                    }
                }
            }
            // since the adding moves to the stack is placed at the move function and also changing the player turn
            // we got to make sure the previous move won't be added again to the stack after being popped,
            // and also make sure this is the turn of the previous player, by changing once
            //for valid move, piece and turn the same, and also return again after the move has been made.

            this.player2Turn = lastMove.getPlayerTurn();
        }
        this.undoFlag = false;
    }

    //return the size of the board.
    @Override
    public int getBoardSize(){
        return this.BOARD_SIZE;
    }
}
