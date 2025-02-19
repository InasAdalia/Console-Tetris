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
    boolean canStartMove = false, canDrawBlocks = true;

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

                    //check condition, if line is destroyed then skip drawBlock, just shiftQueue
                    if (canDrawBlocks){
                        blockList.getNextBlock().drawBlock();
                        blockList.getActiveBlock().drawBlock();
                        System.out.println("[debug] thread calling drawBlock");
                    }
                    
                    if(!blockList.getActiveBlock().hasCollided){
                        if (blockList.canStartMove())
                            blockList.getActiveBlock().move("DOWN");
                            System.out.println("[debug] thread's move down calling drawBlock");
                        blockList.setStartMove(true);
                    } else {
                        blockList.shiftQueue(); // 5,6 > 6,0   tempBlock = 6 > renewList
                        System.out.println("[debug] shiftQueue called");
                    }    
                    
                    canDrawBlocks=true; //reset
                    Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public void setCanDrawBlocks(boolean bool){
        canDrawBlocks = bool;
    }
    

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
