import javax.swing.*;

public class Reader {

    static public gui UIcko;

    public static void main(String[] args) throws Exception {
        try {
            if (System.getProperty("os.name").contains("windows") || System.getProperty("os.name").contains("Windows")) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        UIcko = new gui();
        //ahoj
        UIcko.makeUI();
    }

}
