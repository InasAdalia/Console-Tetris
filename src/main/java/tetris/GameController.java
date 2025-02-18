package tetris;

import java.util.Random;

import org.jline.builtins.ssh.Ssh;

public class GameController {

    BlockList blockList;
    GameState gameState;
    GameView gameView;
    TileLayout layout;
    BlockGenerator generator;
    Block activeBlock; //null
    Block[] blockQueue = {null, null};
    String[] blockTypes = {"Smashboy", "ElleR", "ElleL", "ZackL", "ZackR", "Long", "Tee"};
    String[] movements = {"LEFT", "RIGHT", "NONE"};
    String[] rotations = {"ROTATER", "ROTATEL", "NONE"};
    Random random = new Random();
    boolean canStartMove = false;

    public GameController(GameView gameView, GameState gameState, TileLayout layout) {
        this.gameView = gameView;
        this.gameState = gameState;
        this.layout = layout;
        this.generator = new BlockGenerator(gameView, layout);
        this.blockList = new BlockList(generator, gameView); //shuffledList is already prepared atp.
    }
    
    Thread speedGame = new Thread(new Runnable(){
        @Override
        public void run() {
            while (!gameState.isPaused()){
                try {

                    System.out.println(blockList.printBlockList());
                    blockList.getActiveBlock().drawBlock();
                    blockList.getNextBlock().drawBlock();

                    
                    if(!blockList.getActiveBlock().hasCollided){
                        if (blockList.canStartMove())
                            blockList.getActiveBlock().move("DOWN");
                        blockList.setStartMove(true);
                    } else {
                        blockList.shiftQueue(); // 5,6 > 6,0   tempBlock = 6 > renewList
                        // canStartMove=false;
                    }    
                    
                    // layout.checkRows();
                    Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    

    public void moveActiveBlock(String direction){
        if(blockList.getActiveBlock() != null){
            
            blockList.getActiveBlock().move(direction);

            // gameView.addMessage("[debug] key pressed: " + direction + "\n");
        }
    }

    public void startGame(){
        
        speedGame.start();
    }

   

}
