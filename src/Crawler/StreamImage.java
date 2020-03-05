package Crawler;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

public class StreamImage {

/**
    public void streamImageFromURL(List<String> imageURL, String Web){
        Iterator it = imageURL.iterator();
        int i = 0;
        String paths = "/home/bazza/IdeaProjects/crawlerSolenium/" + Web + "Image" ;
        boolean success = (new File(paths)).mkdir();
        if(success){
            System.out.println("Create folder succesfully");
        }else{
            System.out.println("The folder exists");
        }
        int element = imageURL.size();
        while(it.hasNext()){
            String path = (String) it.next();
            URLConnection con = null;
            InputStream in = null;
            try {
                URL url = new URL(path);
                System.out.println(url);
                con = url.openConnection();
                con.setConnectTimeout(1000);
                con.setReadTimeout(1000);
                in = con.getInputStream();
                Image img = ImageIO.read(in);
                if(img != null){
                    saveImage(img, i, paths);
                }
            }catch (IOException e){

            }
            finally {
                i++;
            }
            element -= 1;
            System.out.println("It remains " + element + " image");
        }
    }
*/
    public void saveImageStream(String urlImage, int i, String savePath, String fileName){
        try {
            URLConnection con = null;
            InputStream in = null;
            URL url = new URL(urlImage);
            con = url.openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            in = con.getInputStream();
            Image img = ImageIO.read(in);
            if(img != null){
                saveImage(img, i, savePath, fileName);
            }
        }catch (IOException | IllegalArgumentException e){

        }
    }

    private void saveImage(Image image, int i, String savePath, String fileName) throws IOException {
        File fl = new File(savePath + fileName +"_"+ i + ".jpg");
        ImageIO.write((RenderedImage) image, "jpg", fl);
        System.out.println("Image " + i + " saved");
    }
}
