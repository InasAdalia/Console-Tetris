package tetris;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// HANDLES ALGORITHM OF BLOCK QUEUING & SHUFFLING

public class BlockList {

    BlockGenerator blockGenerator;
    private Block[] shuffledList;
    private int counter;
    private Block tempBlock;
    private boolean canStartMove, specialMode; //for active block
    
    public BlockList(BlockGenerator blockGenerator, GameView gameView){
        this.blockGenerator = blockGenerator;
        this.tempBlock = null;
        this.counter=0;

    }

    public String printBlockList(){
        return ("[debug] counter/currently at: " + counter + " all posX: " + Arrays.toString(shuffledList));
    }

    public void shuffleList(){
        Block[] list = {
            blockGenerator.generateBlock("SMASHBOY"),
            blockGenerator.generateBlock("LONG"),
            blockGenerator.generateBlock("TEE"),
            blockGenerator.generateBlock("ELLEL"),
            blockGenerator.generateBlock("ELLER"),
            blockGenerator.generateBlock("ZACKL"),
            blockGenerator.generateBlock("ZACKR")
        };

        List<Block> tempList = Arrays.asList(list);
        Collections.shuffle(tempList);
        this.shuffledList = tempList.toArray(new Block[0]);
    }

    public void specialList(){
        Block[] list = {
            blockGenerator.generateBlock("TEE"),
            blockGenerator.generateBlock("ZACKL"),
            blockGenerator.generateBlock("ELLER"),
            blockGenerator.generateBlock("TEE"),
            blockGenerator.generateBlock("TEE"),
            blockGenerator.generateBlock("ELLER"),
            blockGenerator.generateBlock("ZACKR"),
            blockGenerator.generateBlock("ZACKR"),
            blockGenerator.generateBlock("TEE"),
            blockGenerator.generateBlock("TEE"),
            blockGenerator.generateBlock("LONG")
        };

        this.shuffledList = list;
        this.specialMode = true;
    }

    public Block getBlock(int index){
        return shuffledList[index];
    }

    public Block getActiveBlock(){
        Block block;
        if (specialMode){
            block = shuffledList[counter];
        }else{
            if (counter == 6){
                block = tempBlock;
            } else
                block = shuffledList[counter%6]; 
        }
        
        block.setForQueue(false);
        block.setIsFirstSpawn(!canStartMove);

        return block;
    }

    public Block getNextBlock(){
        Block block;
        if (specialMode){
            if (counter+1 > 10){
                block = shuffledList[counter];
            } else
                block = shuffledList[counter+1];
        } else {
            block = shuffledList[(counter+1)%7];
        }
        block.setForQueue(true);
        return block;
    }

    public Boolean canStartMove(){  //so that the newly spawned block waits 1 frame after spawning before auto-falling
        return canStartMove;
    }

    public void setStartMove(Boolean bool){
        canStartMove=bool;
    }

    private void incCounter(){
        if (specialMode){
            counter++;
        }else {
            if (counter+1 <=6){
                counter++;
            } else {
                counter=0;
            }
        }
        
    }

    public void shiftQueue(){
        incCounter();
        canStartMove = false;
        // System.out.println("[debug] current counter: " + counter);
        if (counter==6 && !specialMode){
            renewList();
        } else if (counter==10 && specialMode){
            System.exit(0);
        }
    }

    public void renewList(){
        tempBlock = shuffledList[6];
        // System.out.println("[debug] Shuffling...tempBlock: " + tempBlock);
        shuffleList();
        // System.out.println("[debug] SHUFFLEDLIST: " + printBlockList());
    }

}
