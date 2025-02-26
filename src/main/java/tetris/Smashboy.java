package tetris;

public class Smashboy extends Block {

    TileView tileView;
    int[] posX = {0,0,0,0}; //3,4,3,4
    int[] posY = {0,0,0,0};  //0,0,1,1
    int[][] shape = {  // Initialize shape here
        {1, 1},
        {1, 1}
    };
    int[][][] shapes = {
        shape
    };


    public Smashboy(GameView gameView, TileView tileView) {
        super(gameView, tileView);           
        setBlock(shapes, shape, "Smashboy", posX, posY);
    }
}