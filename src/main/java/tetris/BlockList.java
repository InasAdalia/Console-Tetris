package tetris;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockList {

    BlockGenerator blockGenerator;
    private Block[] blockList, shuffledList;
    private int counter;
    private Block tempBlock;
    private boolean canStartMove; //for active block
    
    public BlockList(BlockGenerator blockGenerator, GameView gameView){
        this.blockGenerator = blockGenerator;
        this.tempBlock = null;
        this.counter=0;

        shuffleList();
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

    public Block getBlock(int index){
        return shuffledList[index];
    }

    // public void initActiveBlock(){
    //     Block block;
    //     block = counter == 6? shuffledList[6] : shuffledList[counter%7]; 
    //     block.setForQueue(false);
    //     block.setIsFirstSpawn(!canStartMove);
    // }

    public Block getActiveBlock(){
        
        Block block;
        if (counter == 6){
            //nextBlock will be zero. so should have renewed list atp or before this
            block = tempBlock;
        } else
            block = shuffledList[counter%6]; 
        block.setForQueue(false);
        block.setIsFirstSpawn(!canStartMove);

        return block;
    }

    public Block getNextBlock(){
        Block block;
        block = shuffledList[(counter+1)%7];
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
        if (counter+1 <=6){
            counter++;
        } else {
            counter=0;
        }
    }

    public void shiftQueue(){
        int prevCounter = counter;
        // System.out.println("[debug] prevCounter: " + prevCounter);
        incCounter();
        canStartMove = false;
        
        // System.out.println("[debug] current counter: " + counter);
        if (counter==6){
            renewList();
        }
    }

    public void renewList(){
        tempBlock = shuffledList[6];
        // System.out.println("[debug] Shuffling...tempBlock: " + tempBlock);
        shuffleList();
        // System.out.println("[debug] SHUFFLEDLIST: " + printBlockList());
    }

}
