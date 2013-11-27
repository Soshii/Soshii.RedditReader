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

public class imgDownloader extends gui  {

    static gui imgDownloader;

    public static void downloadImages(ArrayList<String> links) {
        imgDownloader = new gui();
        Reader.UIcko.setStatus("Downloading images...");

        try {
            int p=0;
            String lin;
            new File("C:\\imgs\\" + imgDownloader.getSource()).mkdirs();
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
                p++;
                String ext = link.substring(27);
                if (ext.equals("gif")) {
                    continue;
                }
                ImageIO.write(image, ext, new File("C:\\imgs\\" + imgDownloader.getSource() + "\\image" + p + "." + ext));
                Reader.UIcko.setStatus("File number " + p + " written.");

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

    public static void  downloadAlbumImages(ArrayList<String> linksAlbum) {
        gui tata = new gui();
        try {
            ArrayList<String> linksSubAlbum = new ArrayList<String>();
            int p=0;
            new File("C:\\imgs\\" + tata.getSource()).mkdirs();
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
                        p++;
                        ImageIO.write(image, "png", new File("C:\\imgs\\" + tata.getSource()+ "\\albumImage" + p + ".png"));
                        Reader.UIcko.setStatus("File number \" + p + \" written.");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                linksSubAlbum.clear();
                httpClient.getConnectionManager().shutdown();
            }
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
