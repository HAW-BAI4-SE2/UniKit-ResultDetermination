package net.unikit.result_determination.tests;

import net.unikit.result_determination.controllers.ResultDeterminationController;
import net.unikit.result_determination.models.implementations.AlgorithmSettingsImpl;
import net.unikit.result_determination.models.interfaces.AlgorithmSettings;
import net.unikit.result_determination.models.interfaces.AllocationPlan;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by abq307 on 18.11.2015.
 */
public class ResultDeterminationControllerTest {

    ResultDeterminationController controller;

    @Before
    public void setUp(){
        try{
            controller = new ResultDeterminationController();
        } catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Test
    /*
     * TODO muss noch vernünftig gemacht werden
     */
    public void testCreateAllocationPlan() throws Exception {
        AlgorithmSettings settings = new AlgorithmSettingsImpl();
        AllocationPlan allocPlan = controller.createAllocationPlan(settings);

        assertTrue(allocPlan != null);
    }
}