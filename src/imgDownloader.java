import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class imgDownloader extends gui implements Runnable {

    static gui imgDownloader;

    static int p=0;

    public void run() {
        downloadImages(userLinkScanner.getLinks());
        downloadAlbumImages(userLinkScanner.getAlbumLinks());

    }

    public static void downloadImages(ArrayList<String> links) {
        imgDownloader = new gui();

        try {
            String lin;

            if (System.getProperty("os.name").contains("windows") || System.getProperty("os.name").contains("Windows")) {
                new File("C:\\imgs\\" + imgDownloader.getSource()).mkdirs();
            } else if (System.getProperty("os.name").contains("OS X") || System.getProperty("os.name").contains("os x")) {
                new File("/Users/" + System.getProperty("user.name") + "/Desktop/" + imgDownloader.getSource()).mkdirs();
            }
            System.out.println(links.size());

            for (String s:links) {
                String[] tokens = s.split("\\.");
                if (s.contains("http://i.imgur")) {
                    lin = tokens[2].substring(3);
                } else {
                    lin = tokens[1].substring(3);
                }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("https://api.imgur.com/3/image/" + lin + ".json");
            getRequest.addHeader("Authorization", "Client-ID df87216782fd34d");

            HttpResponse response = httpClient.execute(getRequest);

            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + response.getStatusLine().getStatusCode());
                }
            } catch (RuntimeException e) {
                System.out.println("Error code: " + response.getStatusLine().getStatusCode());
                System.out.println("Faulty URL: " + s);
            }


            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


            String inputLine = br.readLine();
            br.close();

            Object obj= JSONValue.parse(inputLine);
            JSONObject array=(JSONObject)obj;
            JSONObject data = (JSONObject)array.get("data");
            String link = (String) data.get("link");

            BufferedImage image;
            try {
                URL adresa = new URL(link);
                image = ImageIO.read(adresa);
                //System.out.println(link);
                String[] explode = link.split("/");
                String[] secondExplode = explode[explode.length-1].split("\\.");
                String ext = secondExplode[secondExplode.length-1];
                //System.out.println(ext);
                if (ext.equals("gif")) {
                    continue;
                }
                if (System.getProperty("os.name").contains("windows") || System.getProperty("os.name").contains("Windows")) {
                    ImageIO.write(image, ext, new File("C:\\imgs\\" + imgDownloader.getSource() + "\\image" + p + "." + ext));
                } else if (System.getProperty("os.name").contains("OS X") || System.getProperty("os.name").contains("os x")) {
                    ImageIO.write(image, ext, new File("/Users/" + System.getProperty("user.name") + "/Desktop/" + imgDownloader.getSource() + "/image" + p + "." + ext));
                }
                p++;
                gui.setStatus("File number " + p + " written.");

            } catch (IOException e) {
                e.printStackTrace();
            }

            httpClient.getConnectionManager().shutdown();
            }
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void downloadAlbumImages(ArrayList<String> linksAlbum) {
        try {
            ArrayList<String> linksSubAlbum = new ArrayList<String>();
            //int p=0;

            if (System.getProperty("os.name").contains("windows") || System.getProperty("os.name").contains("Windows")) {
                new File("C:\\imgs\\" + imgDownloader.getSource()).mkdirs();
            } else if (System.getProperty("os.name").contains("OS X") || System.getProperty("os.name").contains("os x")) {
                new File("/Users/" + System.getProperty("user.name") + "/Desktop/" + imgDownloader.getSource()).mkdirs();
            }

            for (String s:linksAlbum) {
                String lin = s.substring(19,24);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet("https://api.imgur.com/3/album/" + lin + ".json");
                getRequest.addHeader("Authorization", "Client-ID df87216782fd34d");

                HttpResponse response = httpClient.execute(getRequest);

                try {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + response.getStatusLine().getStatusCode());
                    }
                } catch (RuntimeException e) {
                    System.out.println("Error code: " + response.getStatusLine().getStatusCode());
                    System.out.println("Faulty URL: " + s);
                }

                BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));


                String inputLine = br.readLine();
                br.close();

                Object obj= JSONValue.parse(inputLine);
                JSONObject array=(JSONObject)obj;
                JSONObject data = (JSONObject)array.get("data");

                BufferedImage image;
                JSONArray images = (JSONArray) data.get("images");
                for (Object imgs : images) {
                    JSONObject zero = (JSONObject) imgs;
                    String linkZero = (String) zero.get("link");
                    linksSubAlbum.add(linkZero);
                }
                try {
                    for (String url : linksSubAlbum){
                        URL adresa = new URL(url);
                        image = ImageIO.read(adresa);
                        //System.out.println(link);
                        String[] explode = url.split("/");
                        String[] secondExplode = explode[explode.length-1].split("\\.");
                        String ext = secondExplode[secondExplode.length-1];
                        //System.out.println(ext);
                        if (ext.equals("gif")) {
                            continue;
                        }
                        if (System.getProperty("os.name").contains("windows") || System.getProperty("os.name").contains("Windows")) {
                            ImageIO.write(image, ext, new File("C:\\imgs\\" + imgDownloader.getSource() + "\\image" + p + "." + ext));
                        } else if (System.getProperty("os.name").contains("OS X") || System.getProperty("os.name").contains("os x")) {
                            ImageIO.write(image, ext, new File("/Users/" + System.getProperty("user.name") + "/Desktop/" + imgDownloader.getSource() + "/image" + p + "." + ext));
                        }
                        p++;
                        gui.setStatus("File number " + p + " written.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                linksSubAlbum.clear();

                httpClient.getConnectionManager().shutdown();
            }

            gui.setStatus("Completed!");
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
