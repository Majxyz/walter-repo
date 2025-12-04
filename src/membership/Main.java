package membership;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NUÑEZTRACKER frame = new NUÑEZTRACKER();
                frame.setVisible(true);
            }
        });
    }
}


