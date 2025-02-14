package tetris;

public class Smashboy extends Block {

    TileLayout layout;
    int[] posX = {0,0,0,0}; //3,4,3,4
    int[] posY = {0,0,0,0};  //0,0,1,1
    int[][] shape = {  // Initialize shape here
        {1, 1},
        {1, 1}
    };
    int[][][] shapes = {
        shape
    };


    public Smashboy(GameView gameView, TileLayout layout) {
        super(gameView, layout);           
        setBlock(shapes, shape, "Smashboy", posX, posY);
    }
}