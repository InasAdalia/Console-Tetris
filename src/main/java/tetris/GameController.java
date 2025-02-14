package tetris;

import java.util.Random;

public class GameController {

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

    Thread speedGame = new Thread(new Runnable(){
        @Override
        public void run() {
            while (!gameState.isPaused()){
                try {
                    
                    genAllBlocks();
                    
                    // if(activeBlock.willColideBottom()){
                    //     gameState.setGameover(activeBlock.hasCollided); //DOESNT WORK
                    //     break;
                    // }
                    
                    if(!activeBlock.hasCollided){
                        // activeBlock.move(movements[random.nextInt(movements.length)]);
                        // activeBlock.rotate(rotations[random.nextInt(rotations.length)]);
                        if (canStartMove)
                            activeBlock.move("DOWN");
                        canStartMove = true;
                    } else {
                        //shift queue
                        blockQueue[0] = blockQueue[1]; 
                        // blockQueue[1] = null;
                        canStartMove = false;

                        // //draw active block
                        // activeBlock.drawBlock();
                    }    
                    
                    // layout.checkRows();

                    Thread.sleep((long) (gameState.getGameSpeed() * gameState.getRefreshTime()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    
    public GameController(GameView gameView, GameState gameState, TileLayout layout){
        this.gameView = gameView;
        this.gameState = gameState;
        this.layout = layout;
        this.generator = new BlockGenerator(gameView, layout);
    }

    public void genAllBlocks(){
        if (blockQueue[0] == null) {
            blockQueue[0] = (generator.generateBlock(blockTypes[random.nextInt(blockTypes.length)]));
            activeBlock = blockQueue[0];
            
            canStartMove = false;
            // gameView.addMessage("generating active block " + activeBlock.getType() + "\n");
            // gameView.addMessage("generating queue[0] " + blockQueue[0].getType() + "\n");
        }  else if (blockQueue[0]==blockQueue[1]){
            activeBlock = blockQueue[0];
            blockQueue[1] = null;
            canStartMove = false;
            activeBlock.setForQueue(false);
            // gameView.addMessage("shifting blocks, active block: " + activeBlock.getType() + "\n");
        }

        if (blockQueue[1] == null){
            
            blockQueue[1] = (generator.generateBlock(blockTypes[random.nextInt(blockTypes.length)]));
            blockQueue[1].setForQueue(true);
        
            // gameView.addMessage("generating queue[1]: " + blockQueue[1].getType() + "\n");
            gameView.setQueue(blockQueue[1]);
        }

        blockQueue[1].drawBlock();
        // activeBlock.drawBlock();
    }

    public void moveActiveBlock(String direction){
        if(activeBlock != null){
            if (direction.equals("DOWN")||direction.equals("RIGHT")||direction.equals("LEFT")){
                activeBlock.move(direction);
            }else if (direction.equals("ROTATER")||direction.equals("ROTATEL")){                
                activeBlock.rotate(direction);
            }        
            gameView.addMessage("key pressed: " + direction + "\n");
        }
    }

    public void startGame(){
        speedGame.start();
    }

   

}
