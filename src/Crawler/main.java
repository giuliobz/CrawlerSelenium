package Crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class main {


    // args[0] web_searcher
    // args[1] save_folder
    public static void main(String[] args) throws InterruptedException, IOException {
        String web_search = args[0];
        String name_folder = args[1];

        Spider spider = new Spider();
        spider.webSearch("image of " + web_search, web_search);
        StreamImage streamImage = new StreamImage();
        String savePath = System.getProperty("user.dir") + "/Images/" + name_folder;

        //Creating a File object
        File file = new File(savePath);
        //Creating the directory
        boolean bool = file.mkdir();

        if (bool){

            System.out.println("Create foder");

        }else{
            System.out.println("Folder already exist");
            System.exit(0);
        }


        List<String> lst = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/Database/"+ web_search  + ".txt"));
        for(int i = 0; i < lst.size(); ++i ){
            String tmp = lst.get(i).replace("\n", "");
            streamImage.saveImageStream(tmp, i, savePath, web_search);
        }
    }
}
