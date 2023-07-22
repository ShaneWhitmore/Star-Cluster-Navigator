package com.example.spacenavigator;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.tools.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.spacenavigator.MainPageController.img;
import static com.example.spacenavigator.Map.*;

public class Converter {

    //  WritableImage wI = new WritableImage(width, height);
    public static int[] arrayPixel;
    public static int arrayLocation;

    static ArrayList<Integer> numStars = new ArrayList<>();

    static int width = (int) img.getWidth();
    static int height = (int) img.getHeight();


    public static Image findValue() {
        PixelReader pr = img.getPixelReader();

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        WritableImage wI = new WritableImage(width, height);

        arrayPixel = new int[width * height];

        //double threshold = slider.getValue();

        for (int row = 0; row < height; row++) {
            for (int columb = 0; columb < width; columb++) {
                double red = pr.getColor(columb, row).getRed();
                double blue = pr.getColor(columb, row).getBlue();
                double green = pr.getColor(columb, row).getGreen();

                double red2 = Color.BLACK.getRed();
                double blue2 = Color.BLACK.getBlue();
                double green2 = Color.BLACK.getGreen();


                double difference = Math.sqrt(Math.pow(red2 - red, 2) + Math.pow(blue2 - blue, 2) + Math.pow(green2 - green, 2));
                //  https://en.wikipedia.org/wiki/Color_difference
                int arrayLocation = row * width + columb;

                if (difference > .5) {
                    wI.getPixelWriter().setColor(columb, row, Color.WHITE);
                    arrayPixel[arrayLocation] = arrayLocation;
                } else {
                    wI.getPixelWriter().setColor(columb, row, Color.BLACK);
                    arrayPixel[arrayLocation] = -1;
                }
            }
        }
        processArray(arrayPixel); //turning array into single line
        displayArray(arrayPixel); //debugger to make sure it is reading colours correctly
        noiseReduction();
        fillMap();
        sort();


        return wI; //returning the image after being processed
    }

    public static void displayArray(int[] arrayPixel) {
        int width = (int) img.getWidth();

        for (int i = 0; i < arrayPixel.length; i++) {
            System.out.print(group(arrayPixel, i) + " ");

            if ((i + 1) % width == 0) {
                System.out.println("");
            }
        }
    }

    public static void joinPixels(int[] arrayPixel, int pixel1, int pixel2) {
        arrayPixel[group(arrayPixel, pixel2)] = group(arrayPixel, pixel1);
    }

    public static int group(int[] arrayPixel, int arrayLocation) {
        if (arrayPixel[arrayLocation] == -1) {
            return arrayPixel[arrayLocation];
        }
        return arrayPixel[arrayLocation] == arrayLocation ? arrayLocation : group(arrayPixel, arrayPixel[arrayLocation]);
    }


    public static void fillMap() {
        Integer root = 0;
        Integer numberOf = 0;

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();

        for (int row = 0; row < height; row++) {
            for (int columb = 0; columb < width; columb++) {
                if (arrayPixel[row * width + columb] >= 0) {
                    Map.insertStar(group(arrayPixel, row * width + columb), row * width + columb);
                }
            }
        }
        System.out.println(stars);

        for (int node : stars.keySet()) {
            System.out.println(node + " " + stars.get(node));
        }
    }

    public static void processArray(int[] arrayPixel) {

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        int pixel1 = 0;
        int pixel2 = 0;

        for (int row = 0; row < height; row++) {
            int pixelTobeCheck = 0;
            for (int columb = 0; columb < width; columb++) {
                pixelTobeCheck = row * width + columb;

                if (arrayPixel[pixelTobeCheck] >= 0) {
                    if (pixelTobeCheck + 1 < arrayPixel.length && arrayPixel[pixelTobeCheck + 1] >= 0) {
                        joinPixels(arrayPixel, pixelTobeCheck, pixelTobeCheck + 1);
                    }
                    //check under
                    if ((pixelTobeCheck + width) < arrayPixel.length)//moving down a line
                    {
                        if (arrayPixel[pixelTobeCheck + width] >= 0) {
                            joinPixels(arrayPixel, pixelTobeCheck, pixelTobeCheck + width);
                        }
                    }
                }
            }

            System.out.println("Root of " + pixelTobeCheck + " is " + group(arrayPixel, pixelTobeCheck));


            //System.out.println(arrayPixel[pixelTobeCheck]);
            // (pixeltocheck+1)%width == 0 //moving down a line


            //hitting 0 going dowm
            // if(pixeltobechecked+width)>array.length
        }
        System.out.println("Array Processed");
        System.out.println(arrayPixel);
    }






    public static void noiseReduction()
    {

        for(int root:stars.keySet())
        {
            for(int i=0;i< stars.size();i++)
            {
                if(stars.get(i).size()<=100)
                {
                    stars.put(root,null);
                }
            }
        }
    }












    public static void circleTheStar(ImageView iv , float sliderData/*Integer root, ArrayList<Integer> pixel*/)
    {
        //run through every root and find the left most, right most, top and bottum pixel
        // add startx and min x and devide by 2
        // repeat for y
        //do centerx-leftmostx = rad
        //do centery-topy = rad
        //which ever one is greater pass that to the radius
        //pass Circle(centerx,centery, radius)

        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        WritableImage wI = new WritableImage(width, height);

        List<Circle> cirList= new ArrayList<>();
        for(Node c : ((Pane) iv.getParent()).getChildren())
        {
            if (c instanceof Circle)
            {
                cirList.add((Circle) c);
            }
        }
        ((Pane) iv.getParent()).getChildren().removeAll(cirList);


        for (int root : stars.keySet()) {
            double startx = width, starty = height, endx = 0, endy = 0;
            double radius;
            ArrayList<Integer> elements = stars.get(root);
            double centerx = 0;
            double centery = 0;

            Bounds bounds = iv.getLayoutBounds();

            double xScale = bounds.getWidth() / iv.getImage().getWidth();
            double yScale = bounds.getHeight() / iv.getImage().getHeight();

            int starsize = elements.size();
            if (starsize > sliderData) {


                for (int i = 0; i < elements.size(); i++) {
                    double findStartx = elements.get(i) % width;
                    double findStarty = elements.get(i) / width;


                    if (findStartx < startx) {
                        startx = findStartx;
                    }

                    if (findStartx > endx) {
                        endx = findStartx;
                    }


                    if (findStarty < starty) {
                        starty = findStarty;
                    }

                    if (findStarty > endy) {
                        endy = findStarty;
                    }


                }


                startx *= xScale;
                starty *= yScale;
                endx *= xScale;
                endy *= yScale;

                centerx = (startx + endx) / 2;
                centery = (starty + endy) / 2;


                double xradius = centerx - startx;
                double yradius = centery - starty;


                if (xradius > yradius) {
                    radius = xradius;
                } else {
                    radius = yradius;
                }

                System.out.println(radius);
                //((Pane)iv.getParent()).getChildren().clear();
                Circle circle = new Circle(centerx, centery, radius);


                circle.setFill(Color.TRANSPARENT);
                circle.setStroke(Color.AQUA);
                circle.setStrokeWidth(2);

                circle.setTranslateX(iv.getLayoutX());
                circle.setTranslateY(iv.getLayoutY());

//            int size = 0;
//            for(int i=0;i<numStars.size();i++)
//            {
//                size =numStars.get(i).hashCode();
//                System.out.println("The size of star " + i + " is " + size + " pixels.");
//            }
                Tooltip tooltip1 = new Tooltip("Star size " + starsize + " pixel units");


                Tooltip.install(circle, tooltip1);
                ((Pane) iv.getParent()).getChildren().add(circle);
            }
        }

    }


    public static WritableImage randomColour(Image image)
    {
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
            WritableImage wI;// = new WritableImage(image);
         wI = (WritableImage) image;
        for (int root : stars.keySet())
        {
            insertColour(root,Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            ArrayList<Integer> star = stars.get(root);
            Color color = colourMap.get(root);
            for (int i = 0; i < star.size(); i++)
            {

                int r=star.get(i)/width;
                int c = star.get(i)%width;
                wI.getPixelWriter().setColor(c,r,color );
            }
        }
        return wI;
    }


    public static void sort()
    {
        int starArray[];

        for(int star:stars.keySet())
        {
//            stars.get(stars.keySet()).size();
//            if()
//            {
//
//            }
//            else
//            {
//
//            }
        }
    }
}















//
//ceollection.min and max min gives top max gives bottum
//
//        group like pixels , go through array and if the size of the group is less than a desired value,
//        st it to 0 and create a new list with only the big groups of data
//
//circle the stars and return back to coloured image