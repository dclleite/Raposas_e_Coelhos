/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raposaecoelho;

/**
 *
 * @author samir
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
        simulator.simulate(300);
    }
    
}
