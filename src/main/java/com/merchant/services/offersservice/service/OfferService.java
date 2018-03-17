package com.merchant.services.offersservice.service;

import com.merchant.services.offersservice.entity.Offer;

import java.util.List;

public interface OfferService {

    List<Offer> getAllOffers();

    Offer getOfferById(long id);

    long createOffer(Offer newOffer);

    Offer updateOffer(Offer updatedOffer, long existingOfferId);

    void cancelOffer(long id);
}
