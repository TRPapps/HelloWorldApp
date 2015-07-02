package com.trpapps.helloworldapp;

/**
 * Created by prudnick on 7/2/2015.
 */

public class Beer {
    public Beer(String name, String alc) {
        this.name = name;
        this.alc = alc;
    }

    public String getName() {
        return name;
    }

    private String name;
    private String alc;
}
