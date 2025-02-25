package tetris;

public class TileView {
    
    GameController gameController;
    GameState gameState;
    Tile[][] TileView;

    public TileView(GameState gameState, GameController gameController) {
        this.gameState = gameState;
        TileView = new Tile [20][10];
        
        clearTileView();
    }

    public void clearTileView(){
        for (int i = 0; i < TileView.length; i++) {
            for (int j = 0; j < TileView[i].length; j++) {
                TileView[i][j] = Tile.EMPTY;
            }
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void showtileView(){

        System.out.printf("%4s", "");
        for (int i = 0; i < TileView[0].length; i++) {
            System.out.print((i+1)+" ");
        }
        System.out.println();
        
        for (int i = 0; i <TileView.length; i++) {
            System.out.printf("%2d |", i+1);
            for (int j = 0; j < TileView[i].length; j++) {
                System.out.print(TileView[i][j].getSymbol());
            }
            System.out.println("|");
        }
        System.out.println("    " + drawHorLine("=="));

    }

    public String drawHorLine(String line){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<TileView[0].length; i++){
            sb.append(line);
        }
        return sb.toString();
    }

    public void setPatternAt(Tile tile, int row, int col){
        try{
            this.TileView[row][col] = tile;
        } catch (ArrayIndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public String getPatternAt(int row, int col){
        return this.TileView[row][col].getSymbol();
    }

    public boolean hasBlockAt(Tile tile, int row, int col){
        return TileView[row][col] == tile; 
    }

    private boolean isCompleteRow(int row){

        boolean fullyOccupied = false;
        for (int j = 0; j < TileView[row].length; j++) {
            if (TileView[row][j] != Tile.INACTIVE){
                // fullyOccupied = true;
                // System.out.println("col: " + j + "is occupied");
                fullyOccupied = false;
                break;
            } else{
                // System.out.println("col: " + j + " is empty");
                fullyOccupied = true;
                // break;
            }
        }
        
        // System.out.println("fullyOccupied at row " + row + ": " + fullyOccupied);
        return fullyOccupied; //if theres an empty tile, row is not complete
    }

    private void destroyRow(int row){

        for (int i = 0; i < TileView[row].length; i++) {
            TileView[row][i] = Tile.EMPTY;
        }

        //shift down the rest of the rows above the destroyed row.
        TileView[0][0] = Tile.EMPTY; //set topmost as empty
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < TileView[0].length; j++) {
                TileView[i][j] = TileView[i-1][j];
            }
        }

        System.out.println("[debug] destroyed row");
        
    }

    public void checkRows(){
        for (int i = 0; i < TileView.length; i++) {
            if (isCompleteRow(i)){
                destroyRow(i);
                gameState.incScore();
                gameController.setInstantlyShift(true);
            }
        }
    }

    public int getHeightDiff(int x, int y){
        int finalY = 0;
        boolean noBlocks=true;

        for (int i = 0; i < TileView.length; i++) { //iterate every row at x=correspondingX and shift them down
            // System.out.println("[debug]checking row: " + i);

            if(TileView[i][x]==Tile.INACTIVE){ //if meets an inactive block, set as finalY
                finalY=i-1;
                noBlocks=false;
                // System.out.println("[debug] INACTIVE detected, finalY: " + finalY);
                break;
            }
        }
        
        if(noBlocks) //if theres no blocks in the way, set finalY to the floor row.
            finalY = TileView.length-1; //21-1
        
        // System.out.println("[debug][getHieghtDiff()] finalY: " + finalY);
        if (finalY-y<=0)
            return -1;
        else
            return finalY-y;
    }

    public void eraseByPattern(Tile pattern){
        //iterate every tile
        for (int i = 0; i < TileView.length; i++) {
            for (int j = 0; j < TileView[i].length; j++) {
                if (TileView[i][j] == pattern){
                    TileView[i][j] = Tile.EMPTY;
                }
            }
        }
    }

    public void switchPattern(Tile oldPattern, Tile newPattern){
        for (int i = 0; i < TileView.length; i++) {
            for (int j = 0; j < TileView[i].length; j++) {
                if (TileView[i][j] == Tile.GHOST){
                    setPatternAt(Tile.INACTIVE, i, j);
                }
            }
        }
    }
    

}
