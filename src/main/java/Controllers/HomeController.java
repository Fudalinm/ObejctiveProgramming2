package Controllers;

import Domain.Meme;
import Domain.Sites;
import Module.DriverConfigurationModule;
import Services.DriverConfigurationService;
import Services.GalleryManager;
import com.google.inject.Guice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ImageView[] images;
    private static final int IMAGE_COUNT = 5;

    @FXML
    private AnchorPane container;

    @FXML
    private void handlePrev(){
        this.galleryManager.getGallery().swipeGalleryLeft();
        this.primaryPicture.setImage(new Image(new ByteArrayInputStream(this.galleryManager.getGallery().getCurrentMeme().get().getContent())));
        centerList();
    }
    @FXML
    private void handleNext(){
        this.galleryManager.getGallery().swipeGalleryRight();
        this.primaryPicture.setImage(new Image(new ByteArrayInputStream(this.galleryManager.getGallery().getCurrentMeme().get().getContent())));
        centerList();
    }


    @FXML
    ImageView primaryPicture;


    @FXML
    private void handleSaveMeme(){

    }
    @FXML
    private void handleAddToFavorites(){

    }

    private GalleryManager galleryManager;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try{
            this.galleryManager = Guice.createInjector(new DriverConfigurationModule())
                                       .getInstance(DriverConfigurationService.class)
                                       .get(Sites.KWEJK);

            images = new ImageView[IMAGE_COUNT];
            centerList();
            this.primaryPicture.setImage(images[0].getImage());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private synchronized void centerList(){
        double layoutY = 600.0;
        double layoutX = 80.0;
        double padding = 100.0/IMAGE_COUNT;
        double fitHeight = 150.0; //stale
        double fitWidth = 670.0/IMAGE_COUNT;

        List<Meme> list = galleryManager.getGallery().getCurrentList();

        images = new ImageView[list.size()];

        for(int j=0; j<IMAGE_COUNT; j++){
            Image img = new Image(new ByteArrayInputStream(list.get(j).getContent()));
            images[j] = new ImageView(img);
            images[j].setImage(img);
            images[j].setFitHeight(fitHeight);
            images[j].setFitWidth(fitWidth);
            images[j].setY(layoutY);
            images[j].setX(padding+layoutX);
            layoutX += fitWidth + padding;
            container.getChildren().add(images[j]);
        }
    }

    public void setGalleryManager(GalleryManager gm) {
        this.galleryManager = gm;
    }
}