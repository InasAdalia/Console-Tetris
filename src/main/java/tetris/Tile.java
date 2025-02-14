package tetris;

public enum Tile {
    BLOCK("[]"),
    INACTIVE("[]"),
    EMPTY("  ");


    boolean isActive= true;

    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";    //no color
    public static final String ANSI_ORANGE = "\u001B[33m"; // Orange
    public static final String ANSI_BLUE = "\u001B[34m";   // Blue

    private final String symbol;

    Tile(String symbol) {
        this.symbol = symbol;
        this.isActive = true;
    }


    public String getSymbol() {
        switch (this) {
            case BLOCK:
                return ANSI_ORANGE + symbol + ANSI_RESET;
            case INACTIVE:
                return ANSI_BLUE + symbol + ANSI_RESET;
            default:
                return symbol; // No color for EMPTY or other tiles
        }
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setInactive(){
        this.isActive = false;
    }

}
