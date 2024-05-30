public enum CellType {
    SNOW('.'),
    ROCK('0'),
    START('S'),
    FINISH('F');

    private final char symbol;

    CellType(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static CellType fromSymbol(char symbol) {
        for (CellType cell : values()) {
            if (cell.symbol == symbol) {
                return cell;
            }
        }

        throw new IllegalArgumentException("Unknown symbol: " + symbol);
    }
}
