package tetris;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class TileLayout {
    
    GameState gameState;
    int initRow=0, initCol=3;
    Tile[][] tileLayout;

    public TileLayout(GameState gameState) {
        this.gameState = gameState;
        tileLayout = new Tile [21][11];
        
        for (int i = 0; i < tileLayout.length; i++) {
            for (int j = 0; j < tileLayout[i].length; j++) {
                tileLayout[i][j] = Tile.EMPTY;
            }
        }
    }

    public void showLayout(){

        System.out.printf("%4s", "");
        for (int i = 0; i < tileLayout[0].length; i++) {
            System.out.print(i+" ");
        }
        System.out.println();
        
        for (int i = 0; i < tileLayout.length; i++) {
            System.out.printf("%2d |", i);
            for (int j = 0; j < tileLayout[i].length; j++) {
                System.out.print(tileLayout[i][j].getSymbol());
            }
            System.out.println("|");
        }
        System.out.println("    " + drawHorLine("=="));

    }

    public String drawHorLine(String line){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<tileLayout[0].length; i++){
            sb.append(line);
        }
        return sb.toString();
    }

    public void setPatternAt(Tile tile, int row, int col){
        try{
            this.tileLayout[row][col] = tile;
        } catch (ArrayIndexOutOfBoundsException e){
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public String getPatternAt(int row, int col){
        return this.tileLayout[row][col].getSymbol();
    }

    public boolean hasBlockAt(Tile tile, int row, int col){
        return tileLayout[row][col] == tile; 
    }

    private boolean isCompleteRow(int row){

        boolean fullyOccupied = false;
        for (int j = 0; j < tileLayout[row].length; j++) {
            if (tileLayout[row][j] != Tile.INACTIVE){
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

        for (int i = 0; i < tileLayout[row].length; i++) {
            tileLayout[row][i] = Tile.EMPTY;
        }

        //shift down the rest of the rows above the destroyed row.
        tileLayout[0][0] = Tile.EMPTY; //set topmost as empty
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < tileLayout[0].length; j++) {
                tileLayout[i][j] = tileLayout[i-1][j];
            }
        }
        
    }

    public void checkRows(){
        for (int i = 0; i < tileLayout.length; i++) {
            if (isCompleteRow(i)){
                destroyRow(i);
                gameState.incScore();
            }
        }
    }

    public int getHeightDiff(int x, int y){
        int finalY = 0;
        boolean noBlocks=true;

         //iterate every row at x=correspondingX and shift them down
        for (int i = 0; i < tileLayout.length; i++) {
            // System.out.println("[debug]checking row: " + i);

            if(tileLayout[i][x]==Tile.INACTIVE){ //if meets an inactive block
                finalY=i-1;
                noBlocks=false;
                // System.out.println("[debug] INACTIVE detected, finalY: " + finalY);
                break;
            }
        }
        if(noBlocks) //if theres no blocks in the way, set finalY to the floor row.
            finalY = tileLayout.length-1; 
        
        // System.out.println("[debug][getHieghtDiff()] finalY: " + finalY);
        if (finalY-y<=0)
            return -1;
        else
            return finalY-y;
    }

    public void eraseByPattern(Tile pattern){
        //iterate every tile
        for (int i = 0; i < tileLayout.length; i++) {
            for (int j = 0; j < tileLayout[i].length; j++) {
                if (tileLayout[i][j] == pattern){
                    tileLayout[i][j] = Tile.EMPTY;
                }
            }
        }
    }

    public void switchPattern(Tile oldPattern, Tile newPattern){
        for (int i = 0; i < tileLayout.length; i++) {
            for (int j = 0; j < tileLayout[i].length; j++) {
                if (tileLayout[i][j] == Tile.GHOST){
                    setPatternAt(Tile.INACTIVE, i, j);
                }
            }
        }
    }
    

}
