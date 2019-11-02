/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simtest;
import SimulationLib.Simulator;
import SimulationLib.FuelType;
import java.util.Enumeration;

/**
 *
 * @author dyh1g19
 */
public class SimTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Simulator sim = new Simulator("resources/cost.csv", "resources/costRate.csv");
        sim.addAppliance("Kettle");
        sim.passHour(1);
        
        System.out.println("Gas: " + sim.totalCostOf(FuelType.Gas).toString());
        System.out.println("Electricity: " + sim.totalCostOf(FuelType.Electricity).toString());
    }
}
