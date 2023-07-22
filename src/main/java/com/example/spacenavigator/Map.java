package com.example.spacenavigator;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {


    static HashMap<Integer, ArrayList<Integer>> stars = new HashMap<>();
    static HashMap<Integer, Color> colourMap = new HashMap<>();


    public static void insertStar(Integer root, Integer numberOf)
    {
        ArrayList<Integer> elementValues = new ArrayList<>();

        if(stars.containsKey(root))
        {
            elementValues=stars.get(root);
            elementValues.add(numberOf);
        }
        else
        {
            elementValues.add(numberOf);
            stars.put(root,elementValues);
        }
    }

    public static void insertColour(Integer root, Color color)
    {
        colourMap.put(root,color);
    }
}

