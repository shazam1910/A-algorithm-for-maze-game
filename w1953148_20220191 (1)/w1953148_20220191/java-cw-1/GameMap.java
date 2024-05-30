public class GameMap {
    private int width;
    private int height;
    private CellType[] map;
    private GameCoordinate start_coordinate;
    private GameCoordinate finish_coordinate;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new CellType[width * height];
    }

    public CellType getCell(int x, int y) 
        throws IndexOutOfBoundsException 
    {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Invalid cell coordinates");
        }

        return map[y * width + x];
    }

    public CellType getCell(GameCoordinate coord) 
        throws IndexOutOfBoundsException
    {
        return getCell(coord.x, coord.y);
    }

    public void setCell(int x, int y, CellType cell) 
        throws IndexOutOfBoundsException
    {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Invalid cell coordinates");
        }

        map[y * width + x] = cell;

        if (cell == CellType.START) {
            start_coordinate = new GameCoordinate(x, y);
        } else if (cell == CellType.FINISH) {
            finish_coordinate = new GameCoordinate(x, y);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GameCoordinate getStartCoordinate() {
        return start_coordinate;
    }

    public GameCoordinate getFinishCoordinate() {
        return finish_coordinate;
    }

    public int getManhattanDistance(GameCoordinate from, GameCoordinate to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    private GameCoordinate getStopPoint(GameCoordinate start, 
        Direction direction) 
    {
        if (getCell(start) != CellType.SNOW &&
            getCell(start) != CellType.START &&
            getCell(start) != CellType.FINISH)
        {
            return null;
        }

        int x_increment, y_increment;

        x_increment = switch (direction) {
            case LEFT -> -1;
            case RIGHT -> 1;
            default -> 0;
        };

        y_increment = switch (direction) {
            case UP -> -1;
            case DOWN -> 1;
            default -> 0;
        };

        GameCoordinate current = start;
        int next_x, next_y;
        while (true) {
            next_x = current.x + x_increment;
            next_y = current.y + y_increment;

            try {
                if (getCell(next_x, next_y) == CellType.ROCK) {
                    return current;
                } else if (getCell(next_x, next_y) == CellType.FINISH) {
                    return new GameCoordinate(next_x, next_y);
                }

                current = new GameCoordinate(next_x, next_y);
            } catch (IndexOutOfBoundsException ignored) {
                return current;
            }            
        }
    }

    public GameCoordinate[] getNeighbors(GameCoordinate coord) {
        GameCoordinate[] neighbors = new GameCoordinate[4];
        Direction[] directions = {
            Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT
        };

        for (int i = 0; i < directions.length; i++) {
            GameCoordinate stop = getStopPoint(coord, directions[i]);
            neighbors[i] = stop.equals(coord) ? null : stop;
        }

        return neighbors;
    }

    public static String getMoveDirection(GameCoordinate from, GameCoordinate to) {
        Direction direction;
        if (from.x == to.x) {
            direction = from.y > to.y ? Direction.UP : Direction.DOWN;
        } else {
            direction = from.x > to.x ? Direction.LEFT : Direction.RIGHT;
        }

        return direction.getDirection();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(getCell(j, i).getSymbol());
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public record GameCoordinate(int x, int y) {
        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            GameCoordinate other = (GameCoordinate) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "(" + (x + 1) + ", " + (y + 1) + ")";
        }
    };

    private enum Direction {
        UP("up"), 
        DOWN("down"), 
        LEFT("left"), 
        RIGHT("right");

        private final String direction;

        Direction(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }
    }
}
