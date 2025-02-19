package tetris;

public class App {

    public static void main(String[] args) throws Exception {

        
        GameState gameState = new GameState();
        TileLayout layout = new TileLayout(gameState, null);
        GameView gameView = new GameView(layout, gameState);
        GameController gameController = new GameController(gameView, gameState, layout);
        TetrisInput tetrisInput = new TetrisInput(gameController);
        layout.setGameController(gameController);
        
        gameView.showGameLayout();
        tetrisInput.startKeyListener(); 
        // gameController.startGame();
        gameController.startSpecial();
    
        //TODO: fix drop block glitch
    }
}
