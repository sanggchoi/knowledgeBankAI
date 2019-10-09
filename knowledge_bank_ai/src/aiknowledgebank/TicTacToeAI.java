package aiknowledgebank;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @author sanggon.choi
 */
public class TicTacToeAI{
    
    private ArrayList<TMove>[] bank_;
    private ArrayList<int[]> moves_;
    private int dimensions_;
    private int boardSize_;
    private int turn_;
    private int boardState_; 
    
    public TicTacToeAI(int player,int dimensions){
        dimensions_ = dimensions;
        boardSize_ = dimensions_*dimensions_;
        boardState_ = 0;
        turn_ = player;
        bank_ = new ArrayList[(int)Math.pow(3,dimensions_*dimensions_)];
        moves_ = new ArrayList();
        initialize();
    }
    
    public int getTurn(){
        return turn_;
    }
    
    public void setBoardState(int state){
        boardState_ = state;
    }
    
    public void initialize(){
        for(int n = 0;n < bank_.length;n++){
            ArrayList<TMove> empty = new ArrayList();
            for(int n2 = 0;n2 < boardSize_;n2++){
                if(isEmpty(n2,n)){
                    empty.add(new TMove(n2));
                }
            }
            bank_[n] = empty;
        }
    }
    
    public TMove getMove(int state){
        ArrayList<TMove> moveList = bank_[state];
        int[] weights = new int[moveList.size()];
        int totalWeight = 0;
        for(int n = 0;n < moveList.size();n++){
            TMove pMove = moveList.get(n);     
            int weight;
            if(turn_ == 1){
                weight = pMove.circle_;
            }else{
                weight = pMove.cross_;
            }
            weights[n] = weight;
            totalWeight += weight;
        }
        int choice = (int)(Math.random()*totalWeight);
        totalWeight = 0;
        for(int n = 0;n < weights.length;n++){
            totalWeight += weights[n];
            if(choice < totalWeight){
                choice = n;
                break;
            }
        }
        return moveList.get(choice);
    }
    
    public void changeTurn(){
        if(turn_ == 1){
            turn_ = 2;
        }else{
            turn_ = 1;
        }
    }
    
    public void makeMove(int shape,int loc){
        boardState_ += shape*Math.pow(3,loc);
    }
    
    public int emptyIndex(int emptyLoc,int state){
        String s = Integer.toString(state,3);
        String newS = new String();
        for(int n = s.length();n < boardSize_;n++){
            newS += "0";
        }
        newS += s;
        int index = 0;
        for(int n = boardSize_-1;n > boardSize_-1-emptyLoc;n--){
            if(newS.charAt(n) == '0'){
                index++;
            }
        }
        return index;
    }
    
    public boolean isEmpty(int loc,int state){
        if(loc < boardSize_ && loc > -1){
            String s = Integer.toString(state,3);
            if(loc < s.length()){
                return s.charAt(s.length()-loc-1) == '0';
            }
            return true;
        }else{
            return false;
        }
    }
    
    public void record(ArrayList<int[]> moves,int weight){
        for(int n = 0;n < moves.size();n++){
            int[] arr = moves.get(n);  
            if(arr[2] == turn_){
                bank_[arr[0]].get(emptyIndex(arr[1],arr[0])).addIndex(arr[2],weight);
            }
        }
    }
    
    public void drawBoard(){
        String state = Integer.toString(boardState_,3);
        String newS = new String();
        for(int n = state.length();n < boardSize_;n++){
            newS += "0";
        }
        newS += state;
        System.out.print("  ");
        for(int n = 1;n <= dimensions_;n++){
            System.out.print(n+" ");
        }
        System.out.println();
        for(int n = 0;n < dimensions_;n++){
            System.out.print(n+1+" ");
            for(int n2 = 0;n2 < dimensions_;n2++){
                System.out.print(getShape(newS.charAt(newS.length()-(n*dimensions_)-n2-1))+" ");
            }
            System.out.println();
        }
    }
    
    public String getShape(char c){
        if(c == '1'){
            return "O";
        }else if(c == '2'){
            return "X";
        }
        return "_";
    }
    
    public boolean winningMove(){
        char[][] board = new char[dimensions_][dimensions_];
        String s = Integer.toString(boardState_,3);
        String newS = new String();
        for(int n = s.length();n < boardSize_;n++){
            newS += "0";
        }
        newS += s;
        for(int n = 0;n < dimensions_;n++){
            for(int n2 = 0;n2 < dimensions_;n2++){
                board[n][n2] = newS.charAt((n*dimensions_)+n2);
            }
        }
        for(int n = 0;n < dimensions_;n++){
            for(int n2 = 1;n2 < dimensions_;n2++){
                if(board[n][0] != board[n][n2] || board[n][0] == '0'){
                    break;
                }else if(n2 == dimensions_-1){
                    return true;
                }
            }
        }
        for(int n = 0;n < dimensions_;n++){
            for(int n2 = 1;n2 < dimensions_;n2++){
                if(board[0][n] != board[n2][n] || board[0][n] == '0'){
                    break;
                }else if(n2 == dimensions_-1){
                    return true;
                }
            }
        }
        for(int n = 1;n < dimensions_;n++){
            if(board[0][0] != board[n][n] || board[0][0] == '0'){
                break;
            }else if(n == dimensions_-1){
                return true;
            }
        }
        for(int n = 1;n < dimensions_;n++){
            if(board[dimensions_-1][0] != board[dimensions_-1-n][n] || board[dimensions_-1][0] == '0'){
                break;
            }else if(n == dimensions_-1){
                return true;
            }
        }
        return false;
    }
    
    private class TMove{
        private int loc_;
        private int circle_;
        private int cross_;
        public TMove(int loc){
            loc_ = loc;
            circle_ = 1;
            cross_ = 1;
        }

        public void addIndex(int player,int value){
            if(player == 1){
                circle_ += value;
            }else if(player == 2){
                cross_ += value;
            }
        }
    }
    
    public void main(){
        int boardDimensions = 3;
        TicTacToeAI aiO = new TicTacToeAI(1,boardDimensions);
        TicTacToeAI aiX = new TicTacToeAI(2,boardDimensions);
        System.out.println("Learning...");
        for(int n = 0;n < 1000000;n++){
            turn_ = 1;
            boardState_ = 0;
            for(int n2 = 0;n2 < boardSize_;n2++){
                TMove move;
                if(turn_ == aiO.getTurn()){
                    move = aiO.getMove(boardState_);
                }else{
                    move = aiX.getMove(boardState_);
                }
                moves_.add(new int[]{boardState_,move.loc_,turn_});
                makeMove(turn_,move.loc_);
                if(winningMove()){
                    if(turn_ == aiO.getTurn()){
                        aiO.record(moves_,1);
                    }else{
                        aiX.record(moves_,1);
                    } 
                    break;
                }
                changeTurn();
            }
            moves_.clear();
        }
        System.out.println("Done");
        Scanner sc = new Scanner(System.in);
        System.out.println("Go first(O), or second(X)?");
        String s;
        do{
            s = sc.nextLine();
        }while(s.compareTo("X") != 0 && s.compareTo("O") != 0);
        TicTacToeAI ai;
        if(s.equals("X")){
            ai = aiO;
        }else{
            ai = aiX;
        }
        turn_ = 1;
        boardState_ = 0;
        for(int n = 0;n < boardSize_;n++){
            drawBoard();
            if(turn_ == ai.getTurn()){
                TMove move = ai.getMove(boardState_);
                makeMove(turn_,move.loc_);
            }else{
                System.out.println("Make a move, any move");
                int r, c;
                do{
                    r = sc.nextInt();
                    c = sc.nextInt();
                }while(r > dimensions_ && r < 1 && c > dimensions_ && c < 1 && isEmpty(((r-1)*3)+c-1,boardState_));
                makeMove(turn_,((r-1)*dimensions_)+c-1);
            }
            if(winningMove()){
                drawBoard();
                if(turn_ == ai.getTurn()){
                    System.out.println("You lose");
                }else{
                    System.out.println("You win");
                }
                return;
            }
            changeTurn();
        }
        drawBoard();
        System.out.println("Drawn Game");
    }
}