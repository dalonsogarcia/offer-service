package com.merchant.services.offersservice.entity;

public enum OfferStatus {
    ACTIVE, EXPIRED, CANCELLED;

    public static OfferStatus fromStringValue(final String value) {
        for (OfferStatus offerStatus : OfferStatus.values()){
            if (value.equalsIgnoreCase(offerStatus.name())) {
                return offerStatus;
            }
        }
        return null;
    }
}
