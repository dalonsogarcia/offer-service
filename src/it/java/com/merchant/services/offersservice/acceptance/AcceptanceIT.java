package com.merchant.services.offersservice.acceptance;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(Cucumber.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@CucumberOptions(plugin = {"pretty",
        "html:target/cucumber/acceptance/api",
        "junit:target/cucumber/acceptance/api.xml",
        "json:target/cucumber/acceptance/acceptance.json"},
        features = {
                "classpath:features/offer_routes.feature",
                "classpath:features/offer_expiration.feature"
        },
        strict = true)
public class AcceptanceIT {
}
