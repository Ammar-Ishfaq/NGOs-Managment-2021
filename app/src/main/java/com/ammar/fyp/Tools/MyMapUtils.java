package com.ammar.fyp.Tools;

public class MyMapUtils {
    private static final MyMapUtils ourInstance = new MyMapUtils();

    public static MyMapUtils getInstance() {
        return ourInstance;
    }

    private MyMapUtils() {
    }

    /**
     * <h1>Share the Code of the </h1>
     *
     * @param longlat e.g: "74.348080,31.561920"
     * @return
     */
    public String getImgfromLongLat(String longlat) {
        String[] arrSplit = longlat.split(",");
        String tempMap = "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/" +
                "pin-s+555555(" +//icon name that need to be Hover
                arrSplit[0] + "," + arrSplit[1] +
                ")/" +
                arrSplit[0] + "," + arrSplit[1] +
                ",15.87" +
                ",2" +
                ",18" +
                "/400x400@2x" +
                "?access_token=" +
                Config.API;
        return tempMap;
    }
}
