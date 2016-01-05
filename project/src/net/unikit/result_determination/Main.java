package net.unikit.result_determination;

import net.unikit.result_determination.controllers.NewResultDeterminationController;

import java.io.IOException;

/**
 * Created by Andreas Berks on 10.11.2015.
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("********* Start Programm *********");
        try {
            NewResultDeterminationController resController = new NewResultDeterminationController();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("********* Quit Programm *********");
    }
}
