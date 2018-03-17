package com.merchant.services.offersservice.acceptance;

import com.merchant.services.offersservice.TestApplication;
import com.merchant.services.offersservice.entity.Offer;
import com.merchant.services.offersservice.repository.OfferRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = TestApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class OfferRouteSteps {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OfferRepository offerRepository;
    private ResponseEntity<String> postResponseEntity;
    private ResponseEntity<Offer[]> collectionResponseEntity;
    private ResponseEntity<Offer> offerResponseEntity;
    private ResponseEntity<String> deleteResponseEntity;

    private final Random randomNumberGenerator = new Random();

    @Given("^there are (.+) offers in the database$")
    public void theOffersDatabaseHasBeenInitialized(final int initialOffers) throws Throwable {
        if (initialOffers == 0) {
            offerRepository.deleteAll();
        } else {
            long offersInDatabase = offerRepository.count();
            if (offersInDatabase < initialOffers) {
                for (int i = 0; i < initialOffers - offersInDatabase; i++) {
                    offerRepository.save(new Offer(String.format("Offer %s", i), LocalDateTime.now(ZoneOffset.UTC).plusDays(30), generateRandomPrice(i + 1)));
                }
            }

            if (offersInDatabase > initialOffers) {
                while (offerRepository.count() > initialOffers) {
                    try {
                        offerRepository.delete(offerRepository.count());
                    } catch (EmptyResultDataAccessException ex) {
                        // Just skip
                        break;
                    }
                }
            }
        }
    }

    private double generateRandomPrice(final int basePrice) {
        return ((randomNumberGenerator.nextInt(20 -1) + 1) * basePrice) - 0.01;
    }

    @When("^a (.+) request is made to (.+) with offer description: (.+), expiration date: (.+), price: (.+), status: (.+) and currency: (.+)$")
    public void aHttpMethodRequestIsMadeToOfferControllerPath(final HttpMethod httpMethod, final String offerControllerPath,
                                                              final String description, final String expirationDate, final String price,
                                                              final String offerStatusString, final String currency) {
        final String url = String.format("http://localhost:%s/offers/%s", serverPort, offerControllerPath);

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);

        final String requestString = getRequestString(description, expirationDate, price, offerStatusString, currency);
        switch (httpMethod){
            case GET:
                // Distinguish between a request to the root collection and an individual resource
                if (offerControllerPath.length() > 1) {
                    offerResponseEntity = restTemplate.getForEntity(url, Offer.class);
                    collectionResponseEntity = null;
                    postResponseEntity = null;
                } else {
                    collectionResponseEntity = restTemplate.getForEntity(url, Offer[].class);
                    offerResponseEntity = null;
                    postResponseEntity = null;
                }
                break;
            case POST:
                postResponseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(requestString, headers), String.class);
                collectionResponseEntity = null;
                offerResponseEntity = null;
                break;
            case PUT:
                final HttpEntity<String> requestEntity = new HttpEntity<>(requestString, headers);
                postResponseEntity = restTemplate.exchange(url, httpMethod, requestEntity, String.class);
                offerResponseEntity = null;
                collectionResponseEntity = null;
                break;
            case DELETE:
                deleteResponseEntity = restTemplate.exchange(url, httpMethod, new HttpEntity<>(""),String.class);
            default:
                break;
        }
    }

    private String getRequestString(final String description, final String expirationDate, final String price,
                                    final String offerStatusString, final String currency) {
        return String.format("{\n" +
                "\"description\":\"%s\",\n" +
                "\"expirationDate\":\"%s\",\n" +
                "\"price\":%s,\n" +
                "\"status\":\"%s\",\n" +
                "\"currency\":\"%s\"" +
                "\n}", description.equalsIgnoreCase("na") ? "" : description,
                expirationDate.equalsIgnoreCase("na") ? "" : expirationDate,
                price.equalsIgnoreCase("na") ? "" : price,
                offerStatusString.equalsIgnoreCase("na") ? "" : offerStatusString,
                currency.equalsIgnoreCase("na") ? "" : currency);
    }

    @Then("^the service should reply with status code (.+)$")
    public void theServiceShouldReplyWithStatusCodeHttpStatusCode(final HttpStatus expectedStatus) throws Throwable {
        if (offerResponseEntity != null) {
            assertEquals(expectedStatus, this.offerResponseEntity.getStatusCode());
        } else if (collectionResponseEntity != null){
            assertEquals(expectedStatus, this.collectionResponseEntity.getStatusCode());
        } else if (postResponseEntity != null) {
            assertEquals(expectedStatus, this.postResponseEntity.getStatusCode());
        } else {
            assertEquals(expectedStatus, this.deleteResponseEntity.getStatusCode());
        }
    }

    @And("^the number of offers returned should be (.)+$")
    public void theNumberOfElementsReturnedShouldBeNumberOfElements(final String numberOfOffers) throws Throwable {

        if (!numberOfOffers.equals("na") && collectionResponseEntity != null) {
            final int numberOfElements = Integer.parseInt(numberOfOffers);
            final Offer[] offers = collectionResponseEntity.getBody();
            assertEquals(numberOfElements, offers.length);
        }
    }

    @And("^the location header value is (.+)$")
    public void theLocationHeaderValueIsLocationHeaderValue(final String locationHeaderValue) throws Throwable {
        if (!locationHeaderValue.equalsIgnoreCase("na")) {
            if (offerResponseEntity != null) {
                assertEquals(locationHeaderValue, this.offerResponseEntity.getHeaders().getLocation().toString());
            } else if (collectionResponseEntity != null){
                assertEquals(locationHeaderValue, this.collectionResponseEntity.getHeaders().getLocation().toString());
            } else if (postResponseEntity != null) {
                assertEquals(locationHeaderValue, this.postResponseEntity.getHeaders().getLocation().toString());
            } else {
                assertEquals(locationHeaderValue, this.deleteResponseEntity.getHeaders().getLocation().toString());
            }
        }
    }
}
