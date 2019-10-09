package aiknowledgebank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author sanggon.choi
 */
public class NimAI {

    private NimMove[][] bank;
    private ArrayList<int[]> moves;
    
    public NimAI(int sticks, int max){
        bank = new NimMove[sticks][max];
        for(int i = 0; i < sticks; i++){
            for(int j = 1; j <= max; j++){
                bank[i][j-1] = new NimMove(j,1);
            }
        }
        moves = new ArrayList();
    }
    
    public int makeMove(int sticksLeft){
        int allWeights = weight(sticksLeft);
        int choice = (int)(Math.random()*allWeights);
        int total = 0;
        int remove = 0;
        for(int n = 0; n < bank[sticksLeft-1].length; n++){
            total += bank[sticksLeft-1][n].getWeight();
            if(total > choice){
                remove = bank[sticksLeft-1][n].getValue();
                break;
            }
        }
        if(remove > sticksLeft){
            remove = sticksLeft;
        }
        int[] move = {sticksLeft, remove};
        moves.add( move );
        return remove;
    }
    
    public void record(){
        for(int i = 0; i < moves.size(); i++){
            int removed = moves.get(i)[1];
            int left = moves.get(i)[0];
            bank[left-1][removed-1].changeWeight(1);
        }
    }
    
    public void clear(){
        moves = new ArrayList();
    }
    
    public void printBank(){
        for(int i = 0; i < bank.length; i++){
            for(int j = 0; j < bank[i].length; j++){
                System.out.println("Sticks left = " + (i+1) + " " + bank[i][j].getValue() + " has weight " + bank[i][j].getWeight());
            }
        }
        
    }
    private int weight(int sticksLeft){
        int total = 0;
        for(int n = 0; n < bank[sticksLeft-1].length; n++){
            total += bank[sticksLeft-1][n].getWeight();
        }  
        return total;
    }
    
    public class NimMove{
        private int value;
        private int weight;
        public NimMove(int v, int w){
            value = v;
            weight = w;
        }
        public int getWeight(){
            return weight;
        }
        public int getValue(){
            return value;
        }
        public void changeWeight(int amount){
            weight += amount;
        }
    }
    
    public static void main() {
        int sticks = 20;
        int max = 3;
        int rounds = 1000000;
        
        System.out.println("learning...");
        
        NimAI trainer1 = new NimAI(sticks, max);
        NimAI trainer2 = new NimAI(sticks, max);
        
        for(int n = 0; n < rounds; n++){
            int sticksLeft = sticks;
            boolean player1 = true;
            
            while(sticksLeft > 0){
                int remove;
                if(player1){
                    remove = trainer1.makeMove(sticksLeft);
                }else{
                    remove = trainer2.makeMove(sticksLeft);
                }
                sticksLeft -= remove;
                player1 = !player1;
            }
            
            if(player1){
                trainer1.record();
            }else{
                trainer2.record();
            }
            
            trainer1.clear();
            trainer2.clear();
        }
        System.out.println("Done");
        
        boolean playGame = true;
        while(playGame){
            Scanner input = new Scanner(System.in);
            System.out.println("Which player will you be (1/2)?");
            boolean player1 = input.nextLine().equals("1");
            
            NimAI computer;
            if(player1){
                computer = trainer2;
            }else{
                computer = trainer1;
            }
            boolean playersTurn = player1 ? true : false;
            int sticksLeft = sticks;
            
            while(sticksLeft > 0){
                int remove = 0;
                if(playersTurn){
                    System.out.println("There are " + sticksLeft + " stick left, how many do you want?");
                    remove = input.nextInt();
                    while(remove < 1 || remove > max){
                        System.out.println("You can only choose between 1 and " + max + " sticks");
                        System.out.println("There are " + sticksLeft + " stick left, how many do you want?");
                        remove = input.nextInt();
                    }
                }else{
                    remove = computer.makeMove(sticksLeft);
                    System.out.println("I will take: " + remove);
                }
                
                sticksLeft -= remove;
                playersTurn = !playersTurn;
            }
            
            if(playersTurn){
                System.out.println("Good job! You won.");
            }else{
                System.out.println("You lose.");
                computer.record();
            }
            computer.clear();
            
            System.out.println("Do you want to play again (y/n)?");
            playGame = input.nextLine().equalsIgnoreCase("y");
        }
    }
}
