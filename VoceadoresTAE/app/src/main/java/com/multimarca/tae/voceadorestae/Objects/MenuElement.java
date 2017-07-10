package com.multimarca.tae.voceadorestae.Objects;

import android.content.Intent;


public class MenuElement {

    private String __Titulo;
    private String __Name;
    private Intent __intentClass;


    public MenuElement(String titulo, String name, Intent intentClass) {
        __Titulo = titulo;
        __Name = name;
        __intentClass = intentClass;
    }


    public Intent get__intentClass() {
        return __intentClass;
    }


    public String get__Titulo() {
        return __Titulo;
    }

    public String get__Name() {
        return __Name;
    }
}


