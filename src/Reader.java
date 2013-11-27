import javax.swing.*;

public class Reader {

    static public gui UIcko;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println("error");
        }
        UIcko = new gui();

        UIcko.makeUI();
    }

}
