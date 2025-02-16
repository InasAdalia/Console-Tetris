package tetris;

public class App {

    public static void main(String[] args) throws Exception {

        
        GameState gameState = new GameState();
        TileLayout layout = new TileLayout(gameState);
        GameView gameView = new GameView(layout, gameState);
        GameController gameController = new GameController(gameView, gameState, layout);
        TetrisInput tetrisInput = new TetrisInput(gameController);
        
        gameView.showGameLayout();
        tetrisInput.startKeyListener();
        
        
        gameController.genAllBlocks(); 
        gameController.startGame();
        
        //2. TODO: press space to drop block + ghost block

        //3. TODO: make algorithm for block queue
    }
}
