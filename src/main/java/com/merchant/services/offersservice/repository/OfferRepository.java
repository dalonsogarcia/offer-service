package com.merchant.services.offersservice.repository;

import com.merchant.services.offersservice.entity.Offer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * {@link Offer} Repository to interact with the Offers table, defines custom queries.
 */
@Repository
public interface OfferRepository extends CrudRepository<Offer, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Offer set status = 'EXPIRED' where expiration_date < :date")
    void expireOffers(@Param("date") LocalDateTime date);
}
