package com.fs.cust.testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import org.junit.runner.RunWith;
import org.testng.annotations.AfterSuite;
import net.masterthought.cucumber.Configuration;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(Cucumber.class)
@CucumberOptions(//plugin={"json:target/cucumber.json","html:target","rerun:target/failedtests.txt"},
                 features="src/test/resources/features/",
                 tags="@custedt"
                 ,dryRun = true)
public class Customer_RunnerTest extends AbstractTestNGCucumberTests {

    @AfterSuite
    private void generateReport(){
        File reportOutputDirectory = new File("target/reports");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber.json");
        //String buildNumber = "1";
        String projectName= "Customer-API-TestAutomation";
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration - check javado details
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);

       // do not make scenario failed when step has status SKIPPED
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        //configuration.setBuildNumber (buildNumber),
        ReportBuilder reportBuilder = new ReportBuilder (jsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();
    }
}
