/**
 * Created by Zhongyi on 17/12/2016.
 * View.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class GameView extends Application {
    private static final String ACTIVE_COLOR = "0x8cc665", DEFAULT_COLOR = "0xeeeeee";
    private static final int HEIGHT = 13, WIDTH = 61, CELL_SIZE = 10, CELL_MARGIN = 2, TIMER_DELAY = 200, TIMER_PERIOD = 50;
    private static final Background
            livingCellBackground = new Background(new BackgroundFill(Color.web(ACTIVE_COLOR), CornerRadii.EMPTY, Insets.EMPTY)),
            deadCellBackground = new Background(new BackgroundFill(Color.web(DEFAULT_COLOR), CornerRadii.EMPTY, Insets.EMPTY)),
            whiteBackground = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));

    /*
    * cellsView and cellsStatus are arranged in the following way.
    * x is in the ith row and jth column, so its index is [i][j].
    * |-------j-----------------|WIDTH
    * |                         |
    * i       x                 |
    * |                         |
    * |                         |
    * |-------------------------|
    * HEIGHT
    * */
    private Button[][] cellsView = new Button[HEIGHT][WIDTH];
    private boolean[][] cellsStatus = new boolean[HEIGHT][WIDTH];
    private GameRule gameRule;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(CELL_SIZE, CELL_SIZE, CELL_SIZE, CELL_SIZE));
        pane.setHgap(2);
        pane.setVgap(2);

        initGameMap(pane);

        pane.setBackground(whiteBackground);
        int stageWidth = (CELL_MARGIN + CELL_SIZE) * WIDTH + 2 * CELL_SIZE,
                stageHeight = (CELL_MARGIN + CELL_SIZE) * HEIGHT + 2 * CELL_SIZE;
        primaryStage.setScene(new Scene(pane, stageWidth, stageHeight));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Conway's Game of Life - " + this.gameRule.name);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();

        Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> updateCell());
            }
        }, TIMER_DELAY, TIMER_PERIOD);
    }

    /**
     * In you timer, call this method every 50ms (use TIMER_PERIOD).
     * Every time this method is called, the canvas shoule be updated to the latest generation.
     */
    private void updateCell() {
        boolean[][] newCellsStatus = new boolean[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                int neighbors = this.getNumberOfLivingNeighbors(i, j);
                if (getCellStatus(i, j)) {
                    if (neighbors == 2 || neighbors == 3) {
                        newCellsStatus[i][j] = true;
                        cellsView[i][j].setBackground(livingCellBackground);
                    } else {
                        newCellsStatus[i][j] = false;
                        cellsView[i][j].setBackground(deadCellBackground);
                    }
                } else {
                    if (neighbors == 3) {
                        newCellsStatus[i][j] = true;
                        cellsView[i][j].setBackground(livingCellBackground);
                    } else {
                        newCellsStatus[i][j] = false;
                        cellsView[i][j].setBackground(deadCellBackground);
                    }
                }
            }
        }
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cellsStatus[i][j] = newCellsStatus[i][j];
            }
        }
    }

    /**
     * @param x,y: coordinate
     * @return :  the number of living neighbor cells
     */
    private int getNumberOfLivingNeighbors(int x, int y) {
        int counter = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= HEIGHT || i < 0 || j < 0 || j >= WIDTH || (i == x && j == y)) {
                    continue;
                }
                if (getCellStatus(i, j)) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    private boolean getCellStatus(int x, int y) {
        return cellsStatus[x][y];
    }

    /**
     * @param x,y:   coordinate
     * @param value: true for living cell and false for dead cell
     */
    private void setCellStatus(int x, int y, boolean value) {
        cellsStatus[x][y] = value;
        if (value) {
            cellsView[x][y].setBackground(livingCellBackground);
        } else {
            cellsView[x][y].setBackground(deadCellBackground);
        }
    }

    /**
     * @param pane: GridPane
     */
    private void initGameMap(GridPane pane) {
        /*
          Randomly pick a map from presets.
          Based on the `Run Length Encoded` format: http://conwaylife.com/wiki/RLE.
          */
        GameRule[] gameRules = new GameRule[]{
                new GameRule("Gosper glider gun -- Bill Gosper 1970", 36, 9, "24bo11b$22bobo11b$12b2o6b2o12b2o$11bo3bo4b2o12b2o$2o8bo5bo3b2o14b$2o8bo3bob2o4bobo11b$10bo5bo7bo11b$11bo3bo20b$12b2o!"),
                new GameRule("Queen bee shuttle -- Bill Gosper 1970", 22, 7, "9bo12b$7bobo12b$6bobo13b$2o3bo2bo11b2o$2o4bobo11b2o$7bobo12b$9bo!"),
                new GameRule("Lightweight spaceship -- John Conway 1970", 5, 4, "bo2bo$o4b$o3bo$4o!"),
                new GameRule("Washerwoman -- Earl Abbe 1971", 56, 5, "o55b$2o4bo5bo5bo5bo5bo5bo5bo5bo5bob$3o2bobo3bobo3bobo3bobo3bobo3bobo3bobo3bobo3bobo$2o4bo5bo5bo5bo5bo5bo5bo5bo5bob$o!"),
                new GameRule("Ants -- Unknown", 44, 4, "2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o2b$2b2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o$2b2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o$2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o3b2o!"),
                new GameRule("Bi-clock -- Dale Edwin Cole 1971", 7, 7, "2bo4b$2o5b$2b2o3b$bo3bob$3b2o2b$5b2o$4bo!"),
                new GameRule("4-8-12 diamond -- Honeywell group 1971", 12, 9, "4b4o4b2$2b8o2b2$12o2$2b8o2b2$4b4o!"),
                new GameRule("Pinwheel -- Simon Norton 1970", 12, 12, "6b2o4b$6b2o4b2$4b4o4b$2obo4bo3b$2obo2bobo3b$3bo3b2ob2o$3bobo2bob2o$4b4o4b2$4b2o6b$4b2o!"),
                new GameRule("Dinner Table -- Robert Wainwright 1972", 13, 13, "bo11b$b3o7b2o$4bo6bob$3b2o4bobob$9b2o2b$6bo6b$4b2obo5b2$2bo3bo2bo3b$bob2o4bo3b$bo6bo4b$2o7b3ob$11bo!"),
                new GameRule("Tumbler -- George Collins 1970", 9, 5, "bo5bob$obo3bobo$o2bobo2bo$2bo3bo2b$2b2ob2o!"),
                new GameRule("Turning toads -- Dean Hickerson 1989", 37, 8, "15bo6bo14b$14b2o5b2o6b2o6b$6b3obobob2obobob2obobo10b$2b2obo6bobo4bobo4bobo2bob2o2b$o2bobo3bo18b4obo2bo$2obobo27bob2o$3bo29bo3b$3b2o27b2o!"),
                new GameRule("38P7.2 -- Nicolay Beluchenko 2009", 13, 11, "4bo3bo4b$o2bobobobo2bo$obo2bobo2bobo$bo2b2ob2o2bob$5bobo5b$2b2o5b2o2b$2bo7bo2b$4bo3bo4b$bo2bo3bo2bob$2b2o5b2o!"),
                new GameRule("Blonker -- Nicolay Beluchenko 2004", 12, 8, "o2b2o4bo$2o2bob2obo$4bobo$5b2o$7bo$7bo3bo$9bobo$10bo!"),
                new GameRule("Octagon 2 -- Arthur Taber 1971", 8, 8, "3b2o3b$2bo2bo2b$bo4bob$o6bo$o6bo$bo4bob$2bo2bo2b$3b2o!"),
                new GameRule("Pentadecathlon -- John Conway 1970", 10, 3, "2bo4bo2b$2ob4ob2o$2bo4bo!"),
                new GameRule("Pentapole -- Unknown 1970", 8, 8, "2o6b$obo5b2$2bobo3b2$4bobob$7bo$6b2o!"),
                new GameRule("Radial pseudo-barberpole -- Gabriel Nivasch 1994", 13, 13, "10b2ob$2o9bob$o8bo3b$2b2o3bobo3b2$3bobobo5b2$5bobobo3b2$3bobo3b2o2b$3bo8bo$bo9b2o$b2o!"),
                new GameRule("Cow -- Unknown", 40, 7, "2o7b2o2b2o2b2o2b2o2b2o2b2o2b2o5b$2o4bob3o2b2o2b2o2b2o2b2o2b2o2b2o3b2o$4b2obo29bobo$4b2o3b29o2b$4b2obo30bob$2o4bob3o2b2o2b2o2b2o2b2o2b2o2b2o2b2ob$2o7b2o2b2o2b2o2b2o2b2o2b2o2b2o!"),
                new GameRule("Ellison p4 HW emulator -- Scot Ellison 2010", 24, 9, "11b2o11b$4bo3bo6bo3bo4b$3bobo12bobo3b$3bobo12bobo3b$2obobob10obobob2o$2obo16bob2o$3bo16bo3b$3bobo4b2obo4bobo3b$4b2o4bob2o4b2o!"),
                new GameRule("Swine -- Scot Ellison 2011", 37, 10, "33bo$9bo2b2o7bo10bobo$o2b2o2bobobo5bo3bo7bo2b2o$o10bo2bobo3bo4bo2bobob2ob2o$o3bo4bo4b2obobo2bob2obo4b2o$3b2o4bob2obo2bobob2o4bo4bo3bo$2ob2obobo2bo4bo3bobo2bo10bo$3b2o2bo7bo3bo5bobobo2b2o2bo$2bobo10bo7b2o2bo$3bo!"),
                new GameRule("Caterer on figure eight -- Unknown", 18, 6, "4b2o6bo5b$2bob2o4bo3b4o$bo8bo3bo3b$4bo5bo7b$2obo9bo4b$2o9b2o!"),
                new GameRule("Almosymmetric -- Unknown 1971", 9, 8, "4bo4b$2o2bobo2b$obo6b$7b2o$bo7b$o6bob$2obobo3b$5bo!"),
                new GameRule("Carnival shuttle -- Robert Wainwright 1984", 38, 7, "33bo3bo$2o3b2o26b5o$bobobo3bo2bo6b2o3bo2bo7bo2b$b2ob2o2b2o3b2o4b2o2b2o3b2o4bobob$bobobo3bo2bo6b2o3bo2bo7bo2b$2o3b2o26b5o$33bo3bo!")};
        this.gameRule = gameRules[(int) (Math.random() * gameRules.length)];
        this.gameRule.initMap();

        /*
          Initialize all slots as dead cells.
          Add these slots (buttons) to the pane.
          */
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                cellsView[i][j] = new Button();
                cellsView[i][j].setPrefHeight(CELL_SIZE);
                cellsView[i][j].setMinHeight(CELL_SIZE);
                cellsView[i][j].setMaxHeight(CELL_SIZE);
                cellsView[i][j].setPrefWidth(CELL_SIZE);
                cellsView[i][j].setMinWidth(CELL_SIZE);
                cellsView[i][j].setMaxWidth(CELL_SIZE);
                setCellStatus(i, j, false);
                GridPane.setConstraints(cellsView[i][j], j, i);
                pane.getChildren().add(cellsView[i][j]);
            }
        }

        /*
         Load the map into the center of the canvas.
         */
        int OFFSET_Y = (WIDTH - this.gameRule.width) / 2;
        int OFFSET_X = (HEIGHT - this.gameRule.height) / 2;
        for (Integer[] liveCell : this.gameRule.patternMap) {
            setCellStatus(liveCell[0] + OFFSET_X, liveCell[1] + OFFSET_Y, true);
        }
    }

}
