package tetris;

public class BlockGenerator {

    protected GameView gameView;
    protected TileView tileView;
    
    public BlockGenerator(GameView gameView, TileView tileView){
        this.tileView = tileView;
        this.gameView = gameView;
    }

    public Block generateBlock(String type){
        switch (type.toUpperCase()) {
            case "SMASHBOY":
                return new Smashboy(gameView, tileView);
            case "ELLER":
                return new ElleR(gameView, tileView);
            case "ELLEL":
                return new ElleL(gameView, tileView);
            case "ZACKL":
                return new ZackL(gameView, tileView);
            case "ZACKR":
                return new ZackR(gameView, tileView);
            case "LONG":
                return new Long(gameView, tileView);
            case "TEE":
                return new Tee(gameView, tileView);
            default:
                return null;
        }
    }
}
