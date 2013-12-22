import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class userLinkScanner implements Runnable {

    private String after = null;
    private static ArrayList<String> links = new ArrayList<String>();
    private static ArrayList<String> linksAlbum = new ArrayList<String>();

    public static ArrayList<String> getLinks() {

        return links;

    }

    public static ArrayList<String> getAlbumLinks() {

        return linksAlbum;

    }

    public void run() {
        System.out.println("uLS method run executed");
        try {
            makeLinks();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void makeLinks() throws Exception {
        URL reddit;
        gui tata = new gui();

        for (int i=0;i<tata.getNOfPages();i++) {
            if (tata.getUOrSub().equals("u")) {
                reddit = new URL("http://www.reddit.com/user/" + tata.getSource() + "/submitted/.json?sort=" + tata.getSorting() + "&t=" + tata.getSortingTime() + "&after=" + after);
            } else {
                reddit = new URL("http://www.reddit.com/r/" + tata.getSource() + "/" + tata.getSorting() + "/.json?sort=" + tata.getSorting() + "&t=" + tata.getSortingTime() + "&after=" + after);
            }

            System.out.println(reddit);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(reddit.openStream()));

            String inputLine = in.readLine();
            in.close();

            Object obj= JSONValue.parse(inputLine);
            JSONObject array=(JSONObject)obj;
            JSONObject data = (JSONObject)array.get("data");
            after = (String) data.get("after");
            JSONArray children = (JSONArray) data.get("children");

            for (Object childObj:children) {

                JSONObject zero = (JSONObject) childObj;
                JSONObject data1 = (JSONObject) zero.get("data");
                String author = (String) data1.get("url");
                String domain = (String) data1.get("domain");
                if (domain.equals("imgur.com") || domain.equals("i.imgur.com")) {
                    if (!links.contains(author) && !author.contains("/a/")) {
                        links.add(author);
                    }
                    if (author.contains("/a/") && !linksAlbum.contains(author)) {
                        linksAlbum.add(author);
                    }
                }
            }

        }

    Thread downloadThread = new Thread(new imgDownloader());
    downloadThread.start();

   }

}
