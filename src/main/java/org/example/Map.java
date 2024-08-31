package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Random;

public class Map extends JPanel {
    private static final Random RANDOM = new Random();
    private static final int HUMAN_DOT = 1;
    private static final int AI_DOT = 2;
    private static final int EMPTY_DOT = 0;
    private static final int PADDING = 10;

    private int gameStateType;
    private static final int STATE_GAME = 0;
    private static final int STATE_WITH_HUMAN = 1;
    private static final int STATE_WITH_AI = 2;
    private static final int STATE_DRAW = 3;

    private static final String MSG_WITH_HUMAN = "Победил игрок!";
    private static final String MSG_WITH_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private int width, height, cellWidth, cellHeigth;
    private int mode, fieldSizeX, fieldSizeY,winLen;
    private int[][] field;
    private boolean gameWork;

    Map() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (gameWork) {
                    update(e);
                }
            }
        });
    }

    private void initMap() {
        field = new int[fieldSizeX][fieldSizeY];
    }
    public void startNewGame(int mode, int sizeX, int sizeY, int winLen) {
        this.mode = mode;
        this.fieldSizeX  = sizeX;
        this.fieldSizeY = sizeY;
        this.winLen = winLen;

        initMap();
        gameWork = true;
        gameStateType = STATE_GAME;

        repaint();
    }

    private void update(MouseEvent mouseEvent) {
        int x = mouseEvent.getX() / cellWidth;
        int y = mouseEvent.getY() / cellHeigth;
        if (!isValidCell(x,y) || !isEmptyCell(x,y)) {
            return;
        }
        field[y][x] = HUMAN_DOT;
        if (checkEndGame(HUMAN_DOT,STATE_WITH_HUMAN)) {
            return;
        }
        aiTurn();
        repaint();
        checkEndGame(AI_DOT,STATE_WITH_AI);
    }
    private void testBoard() {
        for (int i = 0; i<3; i++) {
            System.out.println(Arrays.toString(field[i]));
        }
        System.out.println();
    }
    private boolean isValidCell(int x, int y) {
        return x >=0 && x  < fieldSizeX && y >=0 && y < fieldSizeY;
    }
    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }
    private void aiTurn() {
        int x,y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x,y));
        field[y][x] = AI_DOT;
    }
    private boolean isMapFull() {
        for (int i = 0; i<fieldSizeY; i++) {
            for (int j = 0; j<fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameStateType = gameOverType;
            repaint();
            return true;
        } else if (isMapFull()) {
            this.gameStateType = STATE_DRAW;
            repaint();
            return true;
        }
        return false;
    }
    private boolean checkWin(int dot) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j<fieldSizeY; j++) {
                if (checkLine(i,j,1,0,winLen,dot)) return true;
                if (checkLine(i,j,1,1,winLen,dot)) return true;
                if (checkLine(i,j,0,1,winLen,dot)) return true;
                if (checkLine(i,j,1,-1,winLen,dot)) return true;
            }
        }
        return false;
    }
    private boolean checkLine(int x, int y, int vx, int vy, int len, int dot) {
        int far_x = x + (len - 1) * vx;
        int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) {
            return false;
        }
        for (int i = 0; i<len; i++) {
            if (field[y+i*vy][x+i*vx]!=dot) {
                return false;
            }
        }
        return true;
    }
    @Override
    protected  void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameWork) {
            render(g);
        }
    }
    private void render(Graphics g) {
        width = getWidth();
        height = getHeight();
        cellWidth = width / fieldSizeX;
        cellHeigth = height / fieldSizeY;

        g.setColor(Color.BLACK);
        for (int h = 0; h < fieldSizeX; h++) {
            int y = h * cellHeigth;
            g.drawLine(0,y,width,y);
        }
        for (int w = 0; w<fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x,0,x,height);
        }
        for (int y=0;y<fieldSizeY;y++) {
            for (int x=0;x<fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) {
                    continue;
                }
                if (field[y][x] == HUMAN_DOT) {
                    g.drawLine(x * cellWidth + PADDING, y * cellHeigth + PADDING,
                            (x + 1) * cellWidth - PADDING, (y + 1) * cellHeigth - PADDING);
                    g.drawLine(x * cellWidth + PADDING, (y + 1) * cellHeigth - PADDING,
                            (x + 1) * cellWidth - PADDING, y * cellHeigth + PADDING);
                } else if (field[y][x] == AI_DOT) {
                    g.drawOval(x * cellWidth + PADDING, y * cellHeigth + PADDING,
                            cellWidth - PADDING * 2, cellHeigth - PADDING * 2);
                } else {
                    throw  new RuntimeException("unchecked value " + field[y][x] + " in cell: x=" + x + " y=" + y);
                }
            }
        }
        if (gameStateType != STATE_GAME) {
            showMessage(g);
        }
    }
    private void showMessage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,getHeight()/2, getWidth(),70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("TimesNewRoman", Font.BOLD, 48));
        switch (gameStateType) {
            case STATE_DRAW -> g.drawString(MSG_DRAW,180,getHeight() / 2 + 60);
            case STATE_WITH_HUMAN -> g.drawString(MSG_WITH_HUMAN,20,getHeight()/2 + 60);
            case STATE_WITH_AI -> g.drawString(MSG_WITH_AI,70 , getHeight() / 2 + 60);
            default -> throw new RuntimeException("Unchecked gameOverState: " + gameStateType);
        }
        gameWork = false;
    }
}
