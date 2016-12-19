# lab13

In this lab, you are required to complete the code of *Conway's Game of Life*. You will practice your JavaFX knowledge, especially the use of timer.

![](https://github.com/yuanchuan/game-of-life/raw/master/screencast/demo3.gif)

## Conway's Game of Life

The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970.

The "game" is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves, or, for advanced "players", by creating patterns with particular properties.

### Rules

Every cell interacts with its eight *neighbours*, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:

1. Any live cell with fewer than two live neighbours dies, as if caused by underpopulation (模拟生命数量稀少).
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation (模拟生命数量过多).
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction (模拟繁殖).

## Complete the Code

In this lab, you have three options:

1. Start from zero. Complete all of the code by yourself. This is the recommended option, because students in previous years were all required to do so. You only need to display the UI and handle the basic logic correctly.
2. Start from the code TA provides. The application has only one frame. You need to implement the `updateCell` method and a timer to run the game automatically.
3. Based on the second option, you can select some advanced features to implement. Look at the animation gif on top of this document. There is a textview showing current generation, a button to pause and play, a next button, and a clear button. These all add to the complexity of the code and you are free to challege yourself.

### How to implement a timer?

1. `java.util.Timer`. Here is a sample:


   ```java
   import java.awt.Toolkit;
   import java.util.Timer;
   import java.util.TimerTask;

   /**
    * Simple demo that uses java.util.Timer to schedule a task to execute once 5
    * seconds have passed.
    */

   public class ReminderBeep {
     Toolkit toolkit;

     Timer timer;

     public ReminderBeep(int seconds) {
       toolkit = Toolkit.getDefaultToolkit();
       timer = new Timer();
       timer.schedule(new RemindTask(), seconds * 1000);
     }

     class RemindTask extends TimerTask {
       public void run() {
         System.out.println("Time's up!");
         toolkit.beep();
         //timer.cancel(); //Not necessary because we call System.exit
         System.exit(0); //Stops the AWT thread (and everything else)
       }
     }

     public static void main(String args[]) {
       System.out.println("About to schedule task.");
       new ReminderBeep(5);
       System.out.println("Task scheduled.");
     }
   }
   ```


2. `Timeline`. Refer to your textbook for help. TAs haven't used this before.


   ## Submission

   **Deadline:** Tuesday, 2016.12.20 23:59:59 (UTC+8)

   Upload your work to:

   ```
   ftp://10.132.141.33/classes/16/161 程序设计A （戴开宇）/WORK_UPLOAD/lab13
   ```
