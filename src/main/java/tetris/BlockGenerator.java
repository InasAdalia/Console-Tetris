package tetris;

public class BlockGenerator {

    protected GameView gameView;
    protected TileLayout layout;
    
    public BlockGenerator(GameView gameView, TileLayout layout){
        this.layout = layout;
        this.gameView = gameView;
    }

    public Block generateBlock(String type){
        switch (type.toUpperCase()) {
            case "SMASHBOY":
                return new Smashboy(gameView, layout);
            case "ELLER":
                return new ElleR(gameView, layout);
            case "ELLEL":
                return new ElleL(gameView, layout);
            case "ZACKL":
                return new ZackL(gameView, layout);
            case "ZACKR":
                return new ZackR(gameView, layout);
            case "LONG":
                return new Long(gameView, layout);
            case "TEE":
                return new Tee(gameView, layout);
            default:
                return null;
        }
    }
}
