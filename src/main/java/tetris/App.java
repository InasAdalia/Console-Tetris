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
        
        //1. TODO: make rotation check collision first

        //2. TODO: press space to drop block

        //3. TODO: when reaching line 20, wait one tick before deactivating, but within that one thick before deactivating, 
        //if player moves, the block should still continue moving down and listen for boundaries
    }
}
