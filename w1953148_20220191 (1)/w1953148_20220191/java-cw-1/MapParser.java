import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MapParser {
    public static GameMap parseMap(String path) 
        throws IOException, FileNotFoundException, IllegalArgumentException
    {
        Path filePath = Paths.get(path);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(path);
        }

        List<String> lines = Files.readAllLines(filePath);

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        int width = lines.get(0).length();
        int height = lines.size();

        if (width == 0 || height == 0) {
            throw new IllegalArgumentException("Empty map");
        }

        GameMap map = new GameMap(width, height);

        int y = 0;
        for (String line : lines) {
            if (line.length() != width) {
                throw new IllegalArgumentException("Inconsistent line length");
            }

            for (int i = 0; i < width; i++) {
                map.setCell(i, y, CellType.fromSymbol(line.charAt(i)));
            }
            y++;
        }

        return map;
    }
}
