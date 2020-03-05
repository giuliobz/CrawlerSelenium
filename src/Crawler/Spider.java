package Crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;



public class Spider {

    private int numberOfScrolls;
    private List<String> imgUrlGoogle;
    private List<String> imgUrlBing;
    private List<String> imgUrlFlickr;

    public Spider(){
        double tmp = 100000;
        this.numberOfScrolls = (int)(tmp);
        this.imgUrlGoogle = new LinkedList<String>();
        this.imgUrlBing = new LinkedList<String>();
        this.imgUrlFlickr = new LinkedList<String>();
    }

    public void webSearch(String currentSearch, String fileName) throws InterruptedException, IOException {
        currentSearch.replace(" ", "+");
        SaveUrlImage saveUrlImage = new SaveUrlImage();
        Path path = Paths.get(System.getProperty("user.dir")+ "/crawler-lib/geckodriver-v0.24.0-linux64/geckodriver");
        System.setProperty("webdriver.gecko.driver", String.valueOf(path));
        FirefoxOptions option = new FirefoxOptions();
        option.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(option);
        System.out.println("Starting web search for " + currentSearch +" images");
        System.out.println("Search in Bing images");
        searchBing(driver, currentSearch);
        saveUrlImage.writeUrl(this.imgUrlBing, "Bing", fileName);
        System.out.println("Search in Google images");
        searchGoogle(driver, currentSearch);
        saveUrlImage.writeUrl(this.imgUrlGoogle, "Google", fileName);
        System.out.println("Search in Flickr images");
        searchFlickr(driver, currentSearch);
        saveUrlImage.writeUrl(this.imgUrlFlickr, "Flickr", fileName);
    }

    private List<WebElement> search(FirefoxDriver driver, String url, String webPage) throws InterruptedException {
        int scriptIteration = 0;
        if (webPage == "Google"){
            scriptIteration = 1000;
        }else if(webPage == "Bing"){
            scriptIteration = 10000;
        }else if(webPage == "Flickr"){
            scriptIteration = 1000;
        }else{
            System.out.println("Error occour stop all");
            return null;
        }

        Path path = Paths.get(System.getProperty("user.dir")+ "/crawler-lib/geckodriver-v0.24.0-linux64/geckodriver");
        System.setProperty("webdriver.gecko.driver", String.valueOf(path));

        driver.get(url);
        for (int j = 0; j < numberOfScrolls; ++j){
            for (int i = 0; i < scriptIteration ; ++i) {
                driver.executeScript("window.scrollBy(0, 1000000)");
                Thread.sleep(2);
            }

            Thread.sleep(5);

            try {
                driver.findElementByXPath("//input[@value='Altri risultati']").click();
            }
            catch(Exception e) {
                System.out.println("Less images found : " + e.toString());
                break;
            }
        }
        List<WebElement> images = new LinkedList<>();
        List<WebElement> images2 = new LinkedList<>();
        if (webPage == "Google"){
            images = driver.findElementsByXPath("//div[contains(@class,'rg_meta')]");
        }else if(webPage == "Bing"){
            images = driver.findElements(By.tagName("img"));
        }else if(webPage == "Flickr"){
            images = driver.findElementsByTagName("a");
        }
        return images;
    }

    private void searchBing(FirefoxDriver driver,String currentSearch) throws  InterruptedException{

        String url = "https://www.bing.com/images/search?q="+ currentSearch +"&qs=n&form=QBLH&scope=images&sp=-1&pq="+ currentSearch +"&sc=8-5&sk=&cvid=A2C3EB89E0184575B83A17B2771F4672";
        List<WebElement> images = search(driver, url, "Bing");
        if(images != null){
            System.out.println("Images found with Bing search are : " + images.size());
        }else{
            System.out.println("Error during search");
            return;
        }
        for (int i = 0 ; i < images.size() ; ++i){
            WebElement imm = images.get(i);
            String image = imm.getAttribute("src");
            if(!(image.indexOf("data")>-1 | image.indexOf("png")>-1 | image.indexOf("svg") > -1)){
                this.imgUrlBing.add(image);
            }
        }
    }

    private void searchFlickr(FirefoxDriver driver, String currentSearch) throws InterruptedException{
        String url = "https://www.flickr.com/search/?text=" + currentSearch;
        List<WebElement> images= search(driver, url, "Flickr");
        if(images != null){
            System.out.println("Element found with Flickr search are : " + images.size());
        }else{
            System.out.println("Error during search");
            return;
        }
        int countImage = 0;
        for (int i = 0 ; i < images.size() ; ++i){
            WebElement imm = images.get(i);
            String tmp = imm.getAttribute("href");
            if(tmp.indexOf("photo")>-1){
                if(!(tmp.indexOf("tags")>-1 | tmp.indexOf("albums")>-1 | tmp.indexOf("#comments")>-1)) {
                    if (!(this.imgUrlFlickr.contains(tmp))) {
                        countImage++;
                        this.imgUrlFlickr.add(tmp);
                    }
                }
            }
        }
        System.out.println("Possible image found are : " + countImage);
        this.imgUrlFlickr = processFlirkResult(driver);
        System.out.println("Total image found : " + this.imgUrlFlickr.size());
    }

    private List<String> processFlirkResult(FirefoxDriver driver){
        StreamImage st = new StreamImage();
        List<String> urlsImage = new LinkedList<>();
        int element = this.imgUrlFlickr.size();
        int j = 0;
        for (int i = 0; i < element; ++i){
            try{
                driver.get(this.imgUrlFlickr.get(i));
                if(driver.findElements(By.tagName("img")).size() > 0){
                    WebElement img = driver.findElementByTagName("img");
                    String tmp = img.getAttribute("src");
                    if(tmp.indexOf("jpg") > -1 | tmp.indexOf("jpeg") > -1 | tmp.indexOf("png") > -1){
                        System.out.println("Processing " + j);
                        urlsImage.add(tmp);
                        j += 1;
                    }
                }
            }catch (org.openqa.selenium.WebDriverException e){

            }
            finally {
            }
        }
        return urlsImage;
    }

    private void searchGoogle(FirefoxDriver driver, String currentSearch) throws InterruptedException {
        String url = "https://www.google.com/search?client=ubuntu&hs=shY&channel=fs&biw=1533&bih=748&tbm=isch&sa=1&ei=KDFxXIG5Ge6lrgT6k7_gCw&q=" + currentSearch + "&oq=" + currentSearch + "&gs_l=img.3..35i39l2j0l4j0i67j0l3.2613.3451..3917...0.0..0.185.766.0j5....3..1....1..gws-wiz-img.6fzj1KVAOqA";
        List<WebElement> images= search(driver, url, "Google");
        if(images != null){
            System.out.println("Images found with Google search are : " + images.size());
        }else{
            System.out.println("Error during search");
            return;
        }
        for (int i = 0 ; i < images.size() ; ++i){
            WebElement imm = images.get(i);
            String tmp = imm.getAttribute("innerHTML");
            String subString = substring(tmp, "http", ",");
            this.imgUrlGoogle.add(subString);
        }

    }

    public String substring(String str, String begin, String end){
        int firstIndex = str.indexOf(begin);
        if(firstIndex == -1)
            return null;
        firstIndex -= 4;
        int secondIndex = str.indexOf(end, firstIndex+1);
        if(secondIndex == -1)
            return null;
        secondIndex -= 1;
        return str.substring(firstIndex+begin.length(), secondIndex);
    }

}
