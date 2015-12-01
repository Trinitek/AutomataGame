package com.daexsys.automata.world.terrain.climate;

public class Climate {

    public static final Climate ANTARCTICA = new Climate("antarctica", 0, 0, 10);
    public static final Climate WINTER_FOREST = new Climate("winter forest", 18, 50, 25);
    public static final Climate SNOW_FIELDS = new Climate("snow fields", 30, 80, 40);
    public static final Climate FOREST = new Climate("forest", 45, 80, 50);
    public static final Climate TEMPERATE_PLAINS = new Climate("plains", 50, 60, 55);
    public static final Climate RAINFOREST = new Climate("rainforest", 60, 99, 55);
    public static final Climate SAVANNA = new Climate("savanna", 80, 20, 60);
    public static final Climate DESERT = new Climate("desert", 100, 0, 70);
    public static final Climate BOILING_MESA = new Climate("boiling mesa", 125, 0, 80);

    private String name;
    private int temperature;
    private int humidity;
    private int percentDaytime;

    public Climate(String name, int temperature, int humidity, int percentDaytime) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.percentDaytime = percentDaytime;
    }

    public String getName() {
        return name;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPercentDaytime() {
        return percentDaytime;
    }
}
