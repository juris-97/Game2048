package com.java.game;
import com.javarush.engine.cell.*;
import java.util.Arrays;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int [][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        isGameStopped = false;
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame(){
        setScore(score = 0);
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void createNewNumber(){

        if(getMaxTileValue() == 2048)
            win();

        int randX;
        int randY;

        do{
            randX = getRandomNumber(SIDE);
            randY = getRandomNumber(SIDE);
        }while(gameField[randY][randX] != 0);

        int randCellValue = getRandomNumber(10);
        gameField[randY][randX] = randCellValue < 9 ? 2 : 4;

    }

    private void setCellColoredNumber(int x, int y, int value){
        Color color = getColorByValue(value);
        setCellValueEx(x, y, color, value == 0 ? "" : Integer.toString(value));
    }

    private boolean compressRow(int[] row){

        int count = 0;  // Count of non-zero elements
        boolean isCompressed = false;

        for (int i = 0; i < row.length; i++){
            if (row[i] != 0){
                row[count++] = row[i]; // here count is
            }
        }

        while (count < row.length){
            if(row[count] != 0){
                row[count] = 0;
                isCompressed = true;
            }count++;
        }

        return isCompressed;
    }

    private boolean mergeRow(int [] row){
        boolean isRowMerged = false;

        for (int i = 1; i < row.length; i++) {
            if(row[i] == row[i - 1] && row[i] != 0){
                row[i - 1] = row[i] * 2;
                row[i] = 0;
                isRowMerged = true;
                setScore(score += row[i - 1]);
            }
        }

        return isRowMerged;
    }

    @Override
    public void onKeyPress(Key key) {

        if(isGameStopped){
            if(key == Key.SPACE){
                isGameStopped = false;
                createGame();
                drawScene();
            }else
                return;
        }

        if(!canUserMove()){
            gameOver();
            return;
        }

        if(key == Key.LEFT)
            moveLeft();
        else if(key == Key.RIGHT)
            moveRight();
        else if(key == Key.UP)
            moveUp();
        else if(key == Key.DOWN)
            moveDown();
        else
            return;

        drawScene();
    }

    private boolean canUserMove(){

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if(gameField[i][j] == 0)
                    return true;
            }
        }

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < SIDE; i++) {
                if(mergeRow(gameField[i]) || compressRow(gameField[i]))
                    return true;
            }
            rotateClockwise();
        }


        return false;
    }

    private void moveLeft(){
        boolean isMoved1;
        boolean isMoved2;
        boolean isMerged;
        boolean addNewNum = false;

        for (int i = 0; i < gameField[0].length; i++) {
            isMoved1 = compressRow(gameField[i]);
            isMerged = mergeRow(gameField[i]);
            isMoved2 = compressRow(gameField[i]);

            if(isMerged || isMoved1 || isMoved2)
                addNewNum = true;
        }

        if(addNewNum)
            createNewNumber();
    }

    private void moveRight(){
        for (int i = 0; i < 2; i++)
            rotateClockwise();

        moveLeft();

        for (int i = 0; i < 2; i++)
            rotateClockwise();
    }

    private void moveUp(){

        for (int i = 0; i < 3; i++)
            rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown(){
        rotateClockwise();
        moveLeft();

        for (int i = 0; i < 3; i++)
            rotateClockwise();
    }

    private void rotateClockwise(){
        int [][] rotatedGameField = new int[SIDE][SIDE];
        int k = 0; int l = 0;

        for(int x = 0; x < SIDE; x++, k++){
            for(int y = SIDE - 1; y >= 0; y--, l++){
                rotatedGameField[k][l] = gameField[y][x];
            }
            l = 0;
        }
        gameField = rotatedGameField;
    }

    private int getMaxTileValue(){
        int maxTotal = 0;
        for (int i = 0; i < SIDE; i++) {
            int maxRow = Arrays.stream(gameField[i]).max().getAsInt();
            if(maxTotal < maxRow)
                maxTotal = maxRow;
        }
        return maxTotal;
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.GOLD, "YOU WIN", Color.BLACK, 40);
    }


    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU LOOSE", Color.OLDLACE, 40);
    }

    private Color getColorByValue(int number){
        if(number == 2)
            return Color.LIGHTPINK;
        else if(number == 4)
            return Color.PURPLE;
        else if(number == 8)
            return Color.BLUE;
        else if(number == 16)
            return Color.LIGHTBLUE;
        else if(number == 32)
            return Color.GREEN;
        else if(number == 64)
            return Color.LIGHTGREEN;
        else if(number == 128)
            return Color.ORANGE;
        else if(number == 256)
            return Color.ORANGERED;
        else if(number == 512)
            return Color.RED;
        else if(number == 1024)
            return Color.PINK;
        else if(number == 2048)
            return Color.DARKORCHID;
        else
            return Color.ALICEBLUE;
    }
}
