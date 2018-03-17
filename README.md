# Offers Service #


The offers service is a RESTful web application that allows merchants to create and manage product and/or services offers, specifically, it implements the following functionality:
* Creation of offers
* Query all the stored offers
* Update offers
* Cancel offers
* Automatic expiration of offers

## Offer overview ##

| Field          | Description                         | Type          |
|----------------|:------------------------------------|:--------------|
| id             | Offer identifier                    | long          |
| description    | Offer description                   | String        |
| expirationDate | Date on which the offer will expire | LocalDatetime |    


### Note about identifiers 
On a production database using auto incremented sequential identifiers should be discouraged, we have used sequential generation of identifiers for ease of testing. 