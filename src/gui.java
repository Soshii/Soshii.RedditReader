import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gui extends JFrame implements ActionListener {

    private JTextField source = new JTextField();
    private JTextField nOfPages = new JTextField();
    private JButton go = new JButton("GO!");
    private JLabel sortingLabel = new JLabel("Choose sorting time");
    private JLabel sortingTypeLabel = new JLabel("Choose sorting time frame");
    private JLabel nOfPagesLabel = new JLabel("<html>Choose how many pages <br> of links to scan. 1 page = ~100 links</html>");
    private String[] uOs = {"User name", "Subreddit name"};
    private JComboBox uOrSub = new JComboBox(uOs);
    private String[] sortingA = {"Hot", "New", "Rising", "Controversial", "Top"};
    private JComboBox sorting = new JComboBox(sortingA);
    private String[] sortingTimeA = {"Hour", "Day", "Week", "Year", "All"};
    private JComboBox sortingTime = new JComboBox(sortingTimeA);
    private static JLabel status = new JLabel("");
    JFrame frame;

    static int UOrSub;
    static String SourceStr;
    static int SortingInt;
    static int SortingTimeInt;
    static int NOf;

    public String getUOrSub() {
        if (gui.UOrSub == 0) {
            return "u";
        }
        return "s";
    }

    public String getSource() {
        return gui.SourceStr;
    }

    public String getSorting() {
        switch (gui.SortingInt) {
            case 0: return "hot";
            case 1: return "new";
            case 2: return "rising";
            case 3: return "controversial";
            case 4: return "top";
            default: return "hot";
        }
    }

    public String getSortingTime() {
        switch (gui.SortingTimeInt) {
            case 0: return "hour";
            case 1: return "day";
            case 2: return "week";
            case 3: return "year";
            case 4: return "all";
            default: return "all";
        }
    }

    public int getNOfPages() {
        return gui.NOf;
    }

    //SETTERS
    public void setUOrSub(int u) {
        gui.UOrSub = u;
    }

    public void setSource(String src) {
        gui.SourceStr = src;
    }

    public void setSorting(int k) {
        gui.SortingInt = k;
    }

    public void setSortingTime(int l) {
        gui.SortingTimeInt = l;
    }

    public void setNOfPages(int NOfInt) {
        gui.NOf = NOfInt;
    }

    public static void setStatus(String stat) {
        gui.status.setText(stat);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (source.getText().equals("")) {
                JOptionPane.showMessageDialog(frame,"You need to enter subreddit or user name.", "Enter required information", JOptionPane.WARNING_MESSAGE);
                return;
            } else if ((nOfPages.getText()).equals("") || (nOfPages.getText().equals("0")))   {
                JOptionPane.showMessageDialog(frame,"You need amount of pages (atleast one)", "Enter required information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            setUOrSub(uOrSub.getSelectedIndex());
            setSource(source.getText());
            setSorting(sorting.getSelectedIndex());
            setSortingTime(sortingTime.getSelectedIndex());
            setNOfPages(Integer.parseInt(nOfPages.getText()));


            Thread linkGet = new Thread(new userLinkScanner());
            linkGet.start();

            gui.setStatus("Getting links...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void makeUI() {

        //MAIN IU FRAMES AND PANELS SETUP
        frame = new JFrame("Reddit Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createLineBorder(Color.gray));
        JPanel labels = new JPanel();
        JPanel widgets = new JPanel();
        GridLayout layout = new GridLayout(5, 1, 20, 5);
        layout.setHgap(10);
        frame.getContentPane().add(main);
        main.add(labels, BorderLayout.WEST);
        main.add(widgets, BorderLayout.EAST);
        labels.setLayout(layout);
        widgets.setLayout(layout);
        main.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        widgets.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

        //WIDGETS LABELS ETC
        uOrSub.setSelectedIndex(1);
        go.addActionListener(this);
        labels.add(uOrSub);
        labels.add(sortingLabel);
        labels.add(sortingTypeLabel);
        labels.add(nOfPagesLabel);
        labels.add(status);
        //WIDGETS
        source.requestFocus();
        widgets.add(source);
        widgets.add(sorting);
        widgets.add(sortingTime);
        widgets.add(nOfPages);
        widgets.add(go);

        //FRAME DISPLAY
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}
