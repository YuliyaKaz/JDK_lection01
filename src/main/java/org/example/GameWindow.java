package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameWindow extends  JFrame {
    private static final int WINDOW_HIGHT = 555;
    private static final int WINDOW_WIDTH = 507;

    private JButton btnStart = new JButton("New Game");
    private JButton btnExit = new JButton("Exit");
    private SettingWindow settingWindow;
    private Map map;

    GameWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HIGHT);
        setLocationRelativeTo(null);
        setTitle("TicTacToe");

        setResizable(false);
        btnStart = new JButton("New Game");
        btnExit = new JButton("Exit");
        settingWindow = new SettingWindow(this);
        map = new Map();

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingWindow.setVisible(true);
            }
        });
        JPanel panBottom = new JPanel(new GridLayout(1,2));
        panBottom.add(btnStart);
        panBottom.add(btnExit);

        add(panBottom,BorderLayout.SOUTH);
        add(map);

        setVisible(true);
    }
    void startNewGame(int mode,int sizeX, int sizeY, int winLen) {
        map.startNewGame(mode,sizeX,sizeY,winLen);
    }
}
