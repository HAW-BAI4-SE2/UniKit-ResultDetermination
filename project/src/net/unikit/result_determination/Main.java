package net.unikit.result_determination;

import net.unikit.result_determination.controllers.ResultDeterminationController;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;

import java.io.IOException;

/**
 * Created by Andreas Berks on 10.11.2015.
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("********* Start Programm *********");
        try {
            ResultDeterminationController resController = new ResultDeterminationController();


            AlgorithmSettings settings = new AlgorithmSettings() {
                @Override
                public int hashCode() {
                    return super.hashCode();
                }
            };

            resController.createAllocationPlan(settings);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("********* Quit Programm *********");
    }
}
