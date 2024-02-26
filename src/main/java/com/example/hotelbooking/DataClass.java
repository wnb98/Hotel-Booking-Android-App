package com.example.hotelbooking;
public class DataClass {
    private String dataTitle;
    private String dataDesc;
    private String dataLang;
    private String imageUrl;


    //-------------------------------------------



    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLang;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    //------------------------------





    public DataClass(String dataTitle, String dataDesc, String dataLang, String imageUrl ) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.imageUrl = imageUrl;
        //------------------------------



    }
}
