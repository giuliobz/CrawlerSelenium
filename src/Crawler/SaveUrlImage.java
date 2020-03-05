package Crawler;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class SaveUrlImage {

    private File imageUrls;
    private boolean done;

    public SaveUrlImage(){
        this.done = false;
    }

    private void createFile(String searchWord) throws IOException {
        this.imageUrls = new File(System.getProperty("user.dir") + "/Database/"+ searchWord +".txt");

        if(imageUrls.exists()){
            System.out.println("This Dataset is already made, make new search");
        }else{
            if(!(this.imageUrls.exists())){
                System.out.println("create " + searchWord + " file");
                this.imageUrls.createNewFile();
                this.done = true;
            }
        }
    }

    public void writeUrl(List<String> imageUrl, String Web, String fileName) throws IOException {
        if(!(this.done)){
            createFile(fileName);
        }
        boolean append = true;
        FileWriter fileWriter = new FileWriter(this.imageUrls, append);
        System.out.println("Starting write url from " + Web);
        for (int i = 0; i < imageUrl.size(); ++i){
            String tmp = imageUrl.get(i);
            fileWriter.write(tmp + "\n");
            fileWriter.flush();
        }
        System.out.println("End write url from " + Web);
        fileWriter.close();

    }

}
