package ar.edu.iua.test;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",         
    glue = "ar.edu.iua.test",                    
    plugin = {"pretty", "html:target/cucumber-report.html"},
    monochrome = true
)
public class CucumberRunnerTest {
}