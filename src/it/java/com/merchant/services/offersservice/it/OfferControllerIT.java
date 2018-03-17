package com.merchant.services.offersservice.it;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.merchant.services.offersservice.controller.OfferController;
import com.merchant.services.offersservice.entity.MerchantCurrency;
import com.merchant.services.offersservice.entity.Offer;
import com.merchant.services.offersservice.entity.OfferStatus;
import com.merchant.services.offersservice.service.OfferService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class OfferControllerIT {

    private MockMvc mockMvc;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(offerController)
                .build();
    }

    @Test
    public void testGetAllOffers() throws Exception {
        List<Offer> offerList = getOfferList();

        when(offerService.getAllOffers()).thenReturn(offerList);

        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(offerList.size())));

        verify(offerService, times(1)).getAllOffers();
        verifyNoMoreInteractions(offerService);
    }

    @Test
    public void testGetAllOffers_noOffers() throws Exception {
        List<Offer> offerList = Collections.EMPTY_LIST;

        when(offerService.getAllOffers()).thenReturn(offerList);

        mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(offerList.size())));

        verify(offerService, times(1)).getAllOffers();
        verifyNoMoreInteractions(offerService);
    }

    @Test
    public void testGetOneOffer() throws Exception {
        List<Offer> offerList = getOfferList();

        when(offerService.getOfferById(Mockito.anyLong())).thenReturn(offerList.get(0));

        mockMvc.perform(get("/offers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)));

        verify(offerService, times(1)).getOfferById(1);
        verifyNoMoreInteractions(offerService);
    }

    @Test
    public void testCreateOffer() throws Exception {
        List<Offer> offerList = getOfferList();

        when(offerService.createOffer(Mockito.any(Offer.class))).thenReturn(1L);

        String content = toJsonString(offerList.get(0));
        mockMvc.perform(post("/offers")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/offers/1"));

        verify(offerService, times(1)).createOffer(Mockito.any(Offer.class));
        verifyNoMoreInteractions(offerService);
    }

    @Test
    public void testCreateOffer_invalid() throws Exception {
        List<Offer> offerList = getOfferList();


        final Offer invalidOffer = offerList.get(0);
        invalidOffer.setPrice(-99.99);
        String content = toJsonString(invalidOffer);
        mockMvc.perform(post("/offers")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(offerService);
    }

    @Test
    public void testUpdateOffer() throws Exception {

        final Offer updatedOffer = getOfferList().get(0);
        updatedOffer.setStatus(OfferStatus.CANCELLED);
        when(offerService.updateOffer(Mockito.any(Offer.class), Mockito.anyLong())).thenReturn(updatedOffer);

        final String content = toJsonString(updatedOffer);

        mockMvc.perform(put("/offers/{id}", 1)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("CANCELLED")));

        verify(offerService, times(1)).updateOffer(Mockito.any(Offer.class), Mockito.anyLong());
        verifyNoMoreInteractions(offerService);
    }

    @Test
    public void testUpdateOffer_Invalid() throws Exception {

        final Offer updatedOffer = getOfferList().get(0);
        updatedOffer.setDescription("");
        final String content = toJsonString(updatedOffer);

        mockMvc.perform(put("/offers/{id}", 1)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCancelOffer() throws Exception {

        doNothing().when(offerService).cancelOffer(Mockito.anyLong());

        mockMvc.perform(delete("/offers/{id}/cancel", 1))
                .andExpect(status().isOk());

        verify(offerService, times(1)).cancelOffer(Mockito.anyLong());
        verifyNoMoreInteractions(offerService);
    }

    @Test
    public void testAttemptDelete() throws Exception {


        mockMvc.perform(delete("/offers/{id}", 1))
                .andExpect(status().isMethodNotAllowed());

        verifyZeroInteractions(offerService);
    }

    private List<Offer> getOfferList() {
        Offer offer1 = new Offer("Offer 1", LocalDateTime.now(ZoneOffset.UTC).plusMonths(2), 34.67, OfferStatus.ACTIVE, MerchantCurrency.GBP);
        offer1.setId(1L);
        Offer offer2 = new Offer("Offer 2", LocalDateTime.now().plusYears(3), 56.67);
        offer2.setId(2L);
        return Arrays.asList(offer1, offer2);
    }

    private String toJsonString(final Offer offer) throws JsonProcessingException {
        return new ObjectMapper().
                registerModule(new JavaTimeModule())
                .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                .writeValueAsString(offer);
    }
}
