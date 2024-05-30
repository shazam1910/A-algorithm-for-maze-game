import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class SnowGlider {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SnowGlider <map-file>");
            return;
        }

        GameMap map = null;

        try {
            map = MapParser.parseMap(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("File content is not in correct format");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Could not read file due to I/O error");
            System.exit(1);
        }

        // By this point, map must be initialized or program must already exit
        // Following line is added only to convince the compiler
        if (map == null) System.exit(1);

        PathFinder pathFinder = new PathFinder(map);

        long startTime = System.nanoTime();
        List<GameMap.GameCoordinate> path = pathFinder.findPath();
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        if (path == null) {
            System.out.println("No path found");
        }

        for (int i = 0; i < path.size(); i++) {
            System.out.print((i + 1) + ". ");
            if (i == 0) {
                System.out.println("Start at " + path.get(i));
                continue;
            }

            GameMap.GameCoordinate prev = path.get(i - 1);
            GameMap.GameCoordinate current = path.get(i);
            System.out.println(
                "Move " + 
                GameMap.getMoveDirection(prev, current) +
                " to " + path.get(i)
            );
        }

        System.out.println((path.size() + 1) + ". Done!");
        System.out.println();
        System.out.println("Time taken: " + duration + " ms");
    }
}