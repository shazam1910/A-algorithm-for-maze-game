
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class PathFinder {
    final GameMap map;

    public PathFinder(GameMap map) {
        this.map = map;
    }

    private List<GameMap.GameCoordinate> backtrace(
        Map<GameMap.GameCoordinate, GameMap.GameCoordinate> cameFrom,
        GameMap.GameCoordinate goal,
        GameMap.GameCoordinate start
    ) {
        List<GameMap.GameCoordinate> path = new ArrayList<>();

        GameMap.GameCoordinate current = goal;
        while (!current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }

        path.add(start);
        Collections.reverse(path);

        return path;
    }

    public List<GameMap.GameCoordinate> findPath() {
        Map<GameMap.GameCoordinate, Integer> g_cost = new HashMap<>();
        Map<GameMap.GameCoordinate, Integer> h_cost = new HashMap<>();

        Comparator<GameMap.GameCoordinate> comparator = 
            Comparator.comparingInt((GameMap.GameCoordinate coord) -> 
                g_cost.getOrDefault(coord, Integer.MAX_VALUE) + 
                h_cost.getOrDefault(coord, Integer.MAX_VALUE)
            );

        PriorityQueue<GameMap.GameCoordinate> newNodes =
            new PriorityQueue<>(comparator);
        Set<GameMap.GameCoordinate> visitedNodes = new HashSet<>();
        Map<GameMap.GameCoordinate, GameMap.GameCoordinate> cameFrom = new HashMap<>();
        
        GameMap.GameCoordinate start = map.getStartCoordinate();
        GameMap.GameCoordinate goal = map.getFinishCoordinate();

        g_cost.put(start, 0);
        h_cost.put(start, map.getManhattanDistance(start, map.getFinishCoordinate()));

        newNodes.add(start);
        while (!newNodes.isEmpty()) {
            GameMap.GameCoordinate current = newNodes.poll();
            visitedNodes.add(current);

            if (current.equals(goal)) break;

            GameMap.GameCoordinate[] neighbors = map.getNeighbors(current);
            for (int i = 0; i < neighbors.length; i++) {
                GameMap.GameCoordinate neighbor = neighbors[i];

                if (neighbor == null || visitedNodes.contains(neighbor)) {
                    continue;
                }

                int new_g_cost = g_cost.get(current) + 
                    map.getManhattanDistance(current, neighbor);
                
                if (new_g_cost >= g_cost.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }

                cameFrom.put(neighbor, current);

                g_cost.put(neighbor, new_g_cost);
                if (!h_cost.containsKey(neighbor)) {
                    h_cost.put(neighbor, map.getManhattanDistance(neighbor, goal));
                }

                if (newNodes.contains(neighbor)) {
                    newNodes.remove(neighbor);
                }

                newNodes.add(neighbor);
            }
        }

        return backtrace(cameFrom, goal, start);
    }
}
