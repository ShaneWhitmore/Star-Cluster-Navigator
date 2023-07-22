package com.example.spacenavigator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static com.example.spacenavigator.Converter.*;
import static com.example.spacenavigator.Map.stars;

public class MainPageController implements Initializable
{


    @FXML
    private Slider slider;

    @FXML
    private Button randomColButton;


    @FXML
    private Button BWButton;

    @FXML
    private Button findButton;

    @FXML
    private ListView<String> imageDetails;

    @FXML
    private Button refineButton;

    @FXML
    private Button processMapButton;


    @FXML
    private Button fileButton;

    @FXML
    private Button goButton;

    @FXML
    private ImageView rgbImage;

    ArrayList<Integer> numStars = new ArrayList<>();
    FileChooser fileChooser = new FileChooser();
    public static Image img;


    public void selectImageFromFiles(ActionEvent actionEvent)
    {
        File file = fileChooser.showOpenDialog(new Stage());

        img = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            img = new Image(fis);
          //  img = new Image(fis, rgbImage.getFitWidth(), rgbImage.getFitHeight(), false, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rgbImage.setImage(img);
    }

    public void defineThreshold(MouseEvent mouseEvent)
    {

    }



    public void calculateColor(ActionEvent actionEvent)
    {
        Image n = findValue();
        rgbImage.setImage(n);
        float sliderData = (float) slider.getValue();
        circleTheStar(rgbImage, sliderData);
        numberOfStars();
        starSize();
    }

    public void randomColourStars(ActionEvent actionEvent)
    {
        Image wI = rgbImage.getImage();
        randomColour(wI);

        rgbImage.setImage(wI);
    }

    public void numberOfStars()
    {
        int amount=0;
        for(int star:stars.keySet())
        {
            if(stars.get(star).size() > slider.getValue())
            {
                amount+=1;
            }
        }
        System.out.println("The number of circled stars in this image is " + amount);
    }


    public void starSize()
    {
        int size = 0;
        for(int i=0;i<numStars.size();i++)
        {
            size =numStars.get(i).hashCode();
            System.out.println("The size of star " + i + " is " + size + " pixels.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        fileChooser.setInitialDirectory(new File()); /*Insert File Destination*/
    }


}
