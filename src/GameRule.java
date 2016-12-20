import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhongyi on 19/12/2016.
 * Game rule.
 */
class GameRule {
    String name, pattern;
    int width, height;
    ArrayList<Integer[]> patternMap;

    GameRule(String name, int width, int height, String pattern) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.pattern = pattern;
        this.patternMap = new ArrayList<>();
    }

    void initMap() {
        String[] rows = this.pattern.replace("!", "").split("\\$");
        Pattern b = Pattern.compile("^([0-9]*)b");
        Pattern o = Pattern.compile("^([0-9]*)o");
        int currentRow = 0;
        for (String row : rows) {
            int step = 0;
            int currentCol = 0;
            while (currentCol < row.length()) {
                Matcher bMatcher = b.matcher(row.substring(currentCol, row.length()));
                Matcher oMatcher = o.matcher(row.substring(currentCol, row.length()));
                if (bMatcher.find()) {
                    int inc;
                    if (Objects.equals(bMatcher.group(0), "b")) {
                        inc = 1;
                    } else {
                        inc = Integer.parseInt(bMatcher.group(1));
                    }
                    step += inc;
                    currentCol += bMatcher.group(0).length();
                } else if (oMatcher.find()) {
                    int inc;
                    if (Objects.equals(oMatcher.group(0), "o")) {
                        inc = 1;
                    } else {
                        inc = Integer.parseInt(oMatcher.group(1));
                    }
                    for (int i = 0; i < inc; i++) {
                        Integer[] liveCell = new Integer[]{currentRow, step};
                        this.patternMap.add(liveCell);
                        step += 1;
                    }
                    currentCol += oMatcher.group(0).length();
                } else if (currentCol == row.length() - 1) {
                    break;
                }
            }
            currentRow += 1;
        }
    }
}
