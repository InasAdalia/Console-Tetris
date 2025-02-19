package tetris;

import java.util.Random;

public class GameController {

    BlockList blockList;
    GameState gameState;
    GameView gameView;
    TileLayout layout;
    BlockGenerator generator;
    Block activeBlock; //null
    Random random = new Random();
    boolean canStartMove = false, canDrawBlocks = true;

    public GameController(GameView gameView, GameState gameState, TileLayout layout) {
        this.gameView = gameView;
        this.gameState = gameState;
        this.layout = layout;
        this.generator = new BlockGenerator(gameView, layout);
        this.blockList = new BlockList(generator, gameView); //shuffledList is already prepared atp.
    }
    
    Thread runGame = new Thread(new Runnable(){
        @Override
        public void run() {
            while (!gameState.isPaused()){
                try {

                    // System.out.println(blockList.printBlockList());

                    if (canDrawBlocks){  //if destroyLine is called then instantly go to shiftQueue
                        blockList.getNextBlock().drawBlock();
                        blockList.getActiveBlock().drawBlock();
                        // System.out.println("[debug] thread calling drawBlock");
                    }
                    
                    if(!blockList.getActiveBlock().hasCollided){ //auto move down as long as it hasnt collided
                        if (blockList.canStartMove())
                            blockList.getActiveBlock().move("DOWN");
                        blockList.setStartMove(true);
                    } else {
                        blockList.shiftQueue(); // 5,6 > 6,0   tempBlock = 6 > renewList
                        // System.out.println("[debug] shiftQueue called");
                    }    
                    
                    canDrawBlocks=true; //reset
                    Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    Thread runSpecial = new Thread(new Runnable(){
        @Override
        public void run() {
            while (!gameState.isPaused()){
                try {

                    // System.out.println(blockList.printBlockList());

                    if (canDrawBlocks){  //if destroyLine is called then instantly go to shiftQueue
                        blockList.getNextBlock().drawBlock();
                        blockList.getActiveBlock().drawBlock();
                        // System.out.println("[debug] thread calling drawBlock");
                    }
                    
                    if(!blockList.getActiveBlock().hasCollided){ //auto move down as long as it hasnt collided
                        if (blockList.canStartMove())
                            blockList.getActiveBlock().move("DOWN");
                        blockList.setStartMove(true);
                    } else {
                        blockList.shiftQueue(); // 5,6 > 6,0   tempBlock = 6 > renewList
                        // System.out.println("[debug] shiftQueue called");
                    }    
                    
                    canDrawBlocks=true; //reset
                    Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public void setCanDrawBlocks(boolean bool){ //destroyLine wil set this to false
        canDrawBlocks = bool;
    }
    
    public void moveActiveBlock(String direction){
        if(blockList.getActiveBlock() != null){
            
            blockList.getActiveBlock().move(direction);

            // gameView.addMessage("[debug] key pressed: " + direction + "\n");
        }
    }

    public void startGame(){
        runGame.start();
    }

    public void startSpecial(){
        runSpecial.start();
    }

   

}
