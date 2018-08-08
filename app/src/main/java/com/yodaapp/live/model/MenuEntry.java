package com.yodaapp.live.model;


public class MenuEntry {

    public String titleId;
    public int colorId;
    public String descriptionId;
    public Class mClass;

    public MenuEntry(String titleId, int colorId, String descriptionId, Class aClass) {
        this.titleId = titleId;
        this.colorId = colorId;
        this.descriptionId = descriptionId;
        mClass = aClass;
    }
}
