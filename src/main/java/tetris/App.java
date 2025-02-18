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
    
        
        gameController.startGame();
    
        //3. TODO: fix algorithm for block queue
        // - theres a problem with second round logic.
    }
}
