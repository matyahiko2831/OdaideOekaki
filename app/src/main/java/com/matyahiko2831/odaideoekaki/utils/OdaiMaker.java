package com.matyahiko2831.odaideoekaki.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by kondoukazuya on 2016/02/20.
 */
public class OdaiMaker {

    private static List<String> お題A = new ArrayList<>(Arrays.asList(
            "かっこいい",
            "フランダースの",
            ""));

    private static List<String> お題B = new ArrayList<>(Arrays.asList(
            "犬",
            "猫",
            "スヌーピー"));



    /**
     *
     * */
    public static String getOdai(){

        Random rnd = new Random();

        int indexお題A = rnd.nextInt(お題A.size());
        int indexお題B = rnd.nextInt(お題B.size());

        return お題A.get(indexお題A) + お題B.get(indexお題B);
    }
}
