package com.bazhan;

public class City {
    private String name;
    private String state;
    private int population;

    public City(String name, String state, int population){
        this.name=name;
        this.population=population;
        this.state=state;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public String getState() {
        return state;
    }
}
