package tetris;

import java.util.Random;

public class GameController {

    BlockList blockList;
    GameState gameState;
    GameView gameView;
    TileView tileView;
    BlockGenerator generator;
    Block activeBlock; //null
    Random random = new Random();
    boolean canStartMove = false, canInstantlyShift = true, isFirstRun = true;
    Thread runGame = null;

    public GameController(GameView gameView, GameState gameState, TileView tileView) {
        this.gameView = gameView;
        this.gameState = gameState;
        this.tileView = tileView;
        this.generator = new BlockGenerator(gameView, tileView);
        this.blockList = new BlockList(generator, gameView); //shuffledList is already prepared atp.
    }
    
    // Thread runGame = new Thread(new Runnable(){
    //     @Override
    //     public void run() {
    //         while (!gameState.isPaused()){
    //             try {

    //                 // System.out.println(blockList.printBlockList());

    //                 if (canInstantlyShift){  //if destroyLine is called then instantly go to shiftQueue
    //                     blockList.getNextBlock().drawBlock();
    //                     blockList.getActiveBlock().drawBlock();
    //                     // System.out.println("[debug] thread calling drawBlock");
    //                 }
                    
    //                 if(!blockList.getActiveBlock().hasCollided){ //auto move down as long as it hasnt collided
    //                     if (blockList.canStartMove())
    //                         blockList.getActiveBlock().move("DOWN");
    //                     blockList.setStartMove(true);
    //                 } else {
    //                     blockList.shiftQueue(); // 5,6 > 6,0   tempBlock = 6 > renewList
    //                     // System.out.println("[debug] shiftQueue called");
    //                 }    
                    
    //                 canInstantlyShift=true; //reset
    //                 Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

    //             } catch (InterruptedException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    // });

    Thread runSpecial = new Thread(new Runnable(){
        @Override
        public void run() {
            while (!gameState.isPaused()){
                try {

                    // System.out.println(blockList.printBlockList());

                    if (!canInstantlyShift){  //if destroyLine is called then instantly go to shiftQueue
                        blockList.getNextBlock().blockRenderer.drawBlock();
                        blockList.getActiveBlock().blockRenderer.drawBlock();
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
                    
                    canInstantlyShift=false; //reset
                    Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public void setInstantlyShift(boolean bool){ //destroyLine wil set this to false
        canInstantlyShift = bool;
    }
    
    public void moveActiveBlock(String direction){
        if(blockList.getActiveBlock() != null && !gameState.isPaused()){
            
            blockList.getActiveBlock().move(direction);

            // gameView.addMessage("[debug] key pressed: " + direction + "\n");
        }
    }

    public void startGame(){
        if (isFirstRun)
            blockList.shuffleList();

        gameState.setPause(!gameState.isPaused());
        // System.out.println("[debug] isPaused: " + gameState.isPaused());
         
        if (!gameState.isPaused()){
            runGame = new Thread(new Runnable(){  // Create a NEW thread each time
                @Override
                public void run() {
                    while (!gameState.isPaused()){
                        try {
                            if (canInstantlyShift){  
                                blockList.getNextBlock().blockRenderer.drawBlock();
                                blockList.getActiveBlock().blockRenderer.drawBlock();
                            }
                            
                            if(!blockList.getActiveBlock().hasCollided){ 
                                if (blockList.canStartMove())
                                    blockList.getActiveBlock().move("DOWN");
                                blockList.setStartMove(true);
                            } else {
                                blockList.shiftQueue(); 
                            }    
                            
                            canInstantlyShift = true; //reset
                            Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));
    
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    
            runGame.start();
            isFirstRun = false;
            // System.out.println("[debug] resuming game..." );
        }
        
    }

    public void resetGame(){
        isFirstRun = true;
        gameView.resetMessage(false);
        gameView.addMessage("Game restarted, press P to start");
        gameView.resetGameView();
        if (!gameState.isPaused())
            startGame(); //pauses
    }

    public void startSpecial(){
        blockList.specialList();
        runSpecial.start();

        if (!gameState.isPaused()){
            
        }
        runSpecial = new Thread(new Runnable(){
            @Override
            public void run() {
                while (!gameState.isPaused()){
                    try {
    
                        // System.out.println(blockList.printBlockList());
    
                        if (canInstantlyShift){  //if destroyLine is called then instantly go to shiftQueue
                            blockList.getNextBlock().blockRenderer.drawBlock();
                            blockList.getActiveBlock().blockRenderer.drawBlock();
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
                        
                        canInstantlyShift=true; //reset
                        Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));
    
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

   

}
