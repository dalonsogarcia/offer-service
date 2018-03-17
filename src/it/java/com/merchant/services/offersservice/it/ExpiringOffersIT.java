package com.merchant.services.offersservice.it;

import com.google.common.collect.Lists;
import com.merchant.services.offersservice.TestApplication;
import com.merchant.services.offersservice.entity.Offer;
import com.merchant.services.offersservice.entity.OfferStatus;
import com.merchant.services.offersservice.exception.ResourceNotFoundException;
import com.merchant.services.offersservice.repository.OfferRepository;
import com.merchant.services.offersservice.service.OfferService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ExpiringOffersIT {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    private void loadOffers() {
        offerRepository.save(new Offer("Half price Spring Framework course", LocalDateTime.now(ZoneOffset.UTC).plusMonths(2),59.99, OfferStatus.ACTIVE));
        offerRepository.save(new Offer("25% off DEVOXX Conference tickets", LocalDateTime.now(ZoneOffset.UTC).plusDays(15),199.99, OfferStatus.ACTIVE));
        offerRepository.save(new Offer("2x1 O'REILLY Books", LocalDateTime.now(ZoneOffset.UTC).plusDays(30),39.99, OfferStatus.CANCELLED));
    }

    @After
    public void tearDown() {
        offerRepository.deleteAll();
    }

    @Test
    public void testExpiredOffer() {
        final Offer testOffer = new Offer("Test offer", LocalDateTime.now(ZoneOffset.UTC), 9.99, OfferStatus.ACTIVE);
        final long offerId = offerService.createOffer(testOffer);
        while(!LocalDateTime.now(ZoneOffset.UTC).isEqual(testOffer.getExpirationDate().plusSeconds(1))) {
            // Wait for the offer to expire
        }
        final Offer storedOffer = offerService.getOfferById(offerId);
        assertEquals(OfferStatus.EXPIRED, storedOffer.getStatus());
    }

    @Test
    public void testNotExpiredOffer() {
        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        final Offer testOffer = new Offer("Test offer", now.plusMonths(1), 9.99, OfferStatus.ACTIVE);
        final long offerId = offerService.createOffer(testOffer);
        while(!LocalDateTime.now(ZoneOffset.UTC).isEqual(now.plusSeconds(1))) {
            // Wait for the offer to expire
        }
        final Offer storedOffer = offerService.getOfferById(offerId);
        assertEquals(OfferStatus.ACTIVE, storedOffer.getStatus());
    }

    @Test
    public void testAllOffers() {
        loadOffers();
        assertEquals(3, offerService.getAllOffers().size());
    }

    @Test
    public void testAllOffers_NoOffers() {
        offerRepository.deleteAll();
        assertEquals(0, offerService.getAllOffers().size());
    }

    @Test
    public void testGetOfferById() {
        loadOffers();
        final Long offerId = Lists.newArrayList(offerRepository.findAll()).get(0).getId();
        assertNotNull(offerService.getOfferById(offerId));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetOfferById_NotFound() {
        offerService.getOfferById(40);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetOfferById_NoOffers() {
        offerRepository.deleteAll();
        offerService.getOfferById(2);
    }

    @Test
    public void testCreateOffer(){
        loadOffers();
        final long createdOfferId = offerService.createOffer(new Offer("Render conference tickets", LocalDateTime.now(ZoneOffset.UTC).plusMonths(2), 59.99, OfferStatus.ACTIVE));
        final Offer savedOffer = offerRepository.findOne(createdOfferId);
        assertEquals("Render conference tickets", savedOffer.getDescription());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreateOffer_emptyTitle(){
        offerService.createOffer(new Offer("", LocalDateTime.now(ZoneOffset.UTC).plusMonths(2), 59.99, OfferStatus.ACTIVE));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCreateOffer_negativePrice(){
        offerService.createOffer(new Offer("Render conference tickets", LocalDateTime.now(ZoneOffset.UTC).plusMonths(2), -59.99, OfferStatus.ACTIVE));
    }

    @Test
    public void testUpdateOffer(){
        loadOffers();
        final Long offerId = Lists.newArrayList(offerRepository.findAll()).get(0).getId();
        final Offer updatedOffer = offerService.updateOffer(
                new Offer("25% off DEVOXX Conference tickets", LocalDateTime.now(ZoneOffset.UTC).plusDays(15),299.99, OfferStatus.ACTIVE),
                offerId);
        assertEquals(299.99,updatedOffer.getPrice(), 0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateOffer_NotFound(){
        offerService.updateOffer(
                new Offer("25% off DEVOXX Conference tickets", LocalDateTime.now(ZoneOffset.UTC).plusDays(15),299.99, OfferStatus.ACTIVE),
                89);
    }

    @Test(expected = TransactionSystemException.class)
    public void testUpdateOffer_Invalid(){
        loadOffers();
        final Long offerId = Lists.newArrayList(offerRepository.findAll()).get(0).getId();
        offerService.updateOffer(
                new Offer("25% off DEVOXX Conference tickets", LocalDateTime.now(ZoneOffset.UTC).plusDays(15),-299.99, OfferStatus.ACTIVE),
                offerId);
    }
}
