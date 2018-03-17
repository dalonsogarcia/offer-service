package com.merchant.services.offersservice.controller;

import com.merchant.services.offersservice.entity.Offer;
import com.merchant.services.offersservice.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@Validated
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(final OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public List<Offer> getOffers() {
        return offerService.getAllOffers();
    }

    @PostMapping
    public ResponseEntity<Void> createOffer(final @RequestBody @ValidOffer Offer offer) {
        final Long offerId = offerService.createOffer(offer);
        return ResponseEntity.created(
                UriComponentsBuilder.fromPath("/offers/" + offerId)
                        .build()
                        .toUri())
                .build();
    }

    @GetMapping("/{id}")
    public Offer getOfferById(final @PathVariable long id){
        return offerService.getOfferById(id);
    }

    @PutMapping("/{id}")
    public Offer updateOffer(final @RequestBody @ValidOffer Offer offer, final @PathVariable long id) {
        return offerService.updateOffer(offer, id);
    }

    @DeleteMapping("/{id}/cancel")
    public String cancelOffer(final @PathVariable long id) {
        offerService.cancelOffer(id);
        return ResponseEntity.ok("{\"message\" : \"Offer cancelled\"}").getBody();
    }
}
