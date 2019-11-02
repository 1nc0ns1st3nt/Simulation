package SimulationLib;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dyh1g19
 */

public class Appliance {
    public final String name;
    public final FuelType type;
    public final Float powerRate;
    
    public Appliance(String name, FuelType type, Float powerRate) {
        this.name = name;
        this.type = type;
        this.powerRate = powerRate;
    }
    
    public String getName() {
        return this.name;
    }
}
