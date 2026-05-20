package 기능;

import javax.swing.JFrame;

public class 창 extends JFrame {
    public 창() {
        setTitle("기본 창");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}