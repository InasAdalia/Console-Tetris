package tetris;

import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        GameState gameState = new GameState();
        TileView tileView = new TileView(gameState, null);
        GameView gameView = new GameView(tileView, gameState);
        GameController gameController = new GameController(gameView, gameState, tileView);
        TetrisInput tetrisInput = new TetrisInput(gameController, gameState);
        tileView.setGameController(gameController);
        
        

        while (!exit){
            gameView.showGameView();
            System.out.println("CHOOSE GAME: \n1.Normal \n2.Special \n3.How To Play \n4.Exit");
            try {
                int gameChoice = Integer.parseInt(System.console().readLine());
            
                switch (gameChoice) {
                    case 1:
                        tetrisInput.startKeyListener();
                        gameController.startGame(); 
                        break;
                    case 2:
                        tetrisInput.startKeyListener();
                        gameController.startSpecial();
                        break;
                    case 3:
                        System.out.println("MOVEMENTS: [use arrow keys]" +  
                        "\nROTATE LEFT: [Z] \nROTATE RIGHT: [X] \nDROP BLOCK: [SPACE] \nPAUSE/RESUME: [ESC] \n\nenter 0 to go back");
                        int choice = Integer.parseInt(System.console().readLine());
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                System.out.println("invalid input");
            }
            
        }

        System.exit(0);
    
    
        //TODO: restart
        //TODO: kemas code
    }
}
