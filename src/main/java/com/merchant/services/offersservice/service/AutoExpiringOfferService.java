package com.merchant.services.offersservice.service;

import com.google.common.collect.Lists;
import com.merchant.services.offersservice.entity.Offer;
import com.merchant.services.offersservice.entity.OfferStatus;
import com.merchant.services.offersservice.exception.ResourceNotFoundException;
import com.merchant.services.offersservice.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class AutoExpiringOfferService implements OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public AutoExpiringOfferService(final OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void expireOffers() {
        offerRepository.expireOffers(LocalDateTime.now(ZoneOffset.UTC));
    }

    @Transactional
    public List<Offer> getAllOffers() {
        return Lists.newArrayList(offerRepository.findAll());
    }

    @Transactional
    public Offer getOfferById(final long id) {
        return Optional.ofNullable(
                offerRepository.findOne(id))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Offer with id %s not found", id)));
    }

    @Transactional
    public long createOffer(final Offer newOffer) {
        return offerRepository.save(newOffer).getId();
    }

    @Transactional
    public Offer updateOffer(final Offer updatedOffer, final long existingOfferId) {
        getOfferById(existingOfferId);
        updatedOffer.setId(existingOfferId);
        return offerRepository.save(updatedOffer);
    }

    @Transactional
    public void cancelOffer(final long id) {
        final Offer offer = getOfferById(id);
        offer.setStatus(OfferStatus.CANCELLED);
        offerRepository.save(offer);
    }

}
