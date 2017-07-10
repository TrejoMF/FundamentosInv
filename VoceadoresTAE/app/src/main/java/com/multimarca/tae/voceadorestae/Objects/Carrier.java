package com.multimarca.tae.voceadorestae.Objects;

import java.util.Arrays;
import java.util.List;

/**
 * Created by erick on 12/28/15. Multimarca
 */
public class Carrier {

    private String Name;
    private String Code;
    private String Prices;
    private String Observations;
    private int    Type;
    private int    Id;


    public Carrier(int id, String name, String code, String prices, String observations, int type) {
        Id = id;
        Name = name;
        Code = code;
        Prices = prices;
        Observations = observations;
        Type = type;
    }


    public String getName() {
        return Name;
    }


    public String getCode() {
        return Code;
    }

    public int getId() {
        return Id;
    }

    public int getType() {
        return Type;
    }
    public List<String> getPricesList(){
        return Arrays.asList(Prices.split(","));
    }
    public List<String> getObservationPrices(){
        return Arrays.asList(Observations.split(","));
    }
}
