# Offers Service

The offers service is a RESTful web application that allows merchants to create and manage product and/or services offers, specifically, it implements the following functionality:
* Creation of offers
* Query all the stored offers
* Update offers
* Cancel offers
* Automatic expiration of offers

## Offer overview

| Field           | Description                                                | Type             | Accepted values              |
|-----------------|:-----------------------------------------------------------|:-----------------|:-----------------------------|
| id              | Offer identifier                                           | long             | System generated             |
| description     | Offer description                                          | String           | Any text                     |
| expirationDate  | Date on which the offer will expire                        | LocalDatetime    | Any future date              |
| price           | Numeric value representing the cost of the offer           | double           | Any positive number          |
| status          | Possible status of the offer                               | OfferStatus      | ACTIVE, CANCELLED or EXPIRED | 
| price           | Price currency                                             | MerchantCurrency | GBP, USD or EUR              |   

## Available operations

The operations are implemented as standard HTTP requests and will return standard HTTP status codes, it is recommended to use a REST client like Insomnia or Postman to interact with the system.

### Retrieve existing offers

#### Example request

```
    curl --request GET \
      --url http://localhost:40200/offers/ \
      --header 'content-type: application/json' \
      --data '{
        "description":"Updated description",
        "expirationDate":"2018-04-17T16:28:12.507",
        "price":19.99,
        "status":"ACTIVE",
        "currency":"GBP"
        }'
```

#### Example response

```
    HTTP/1.1 200 
    Content-Type: application/json;charset=UTF-8
    
    [
        {
            "id":1,
            "description":"Updated description",
            "expirationDate":"2018-04-17T16:28:12.507",
            "price":19.99,
            "status":"ACTIVE",
            "currency":"GBP"
        }
    ]
```

### Create new offer

#### Example request

```
curl --request POST \
     --url http://localhost:40200/offers \
     --header 'content-type: application/json' \
     --data '{
        "description":"One offer",
        "expirationDate":"2018-04-17T16:28:12.507",
        "price":19.99,
        "status":"ACTIVE",
        "currency":"GBP"
       }'
   ```

### Example response
```
    HTTP/1.1 201 
    Location: /offers/1
    Content-Length: 0
```

### Update offer

#### Example request

```
curl --request PUT \
     --url http://localhost:40200/offers/1 \
     --header 'content-type: application/json' \
     --data '{
        "description":"Updated description",
        "expirationDate":"2018-04-17T16:28:12.507",
        "price":19.99,
        "status":"ACTIVE",
        "currency":"GNB"
       }'
   ```

#### Example response
```
    HTTP/1.1 200 
    Content-Type: application/json;charset=UTF-8
    
    { 
        "id":1, 
        "description":"Updated description",
        "expirationDate":"2018-04-17T16:28:12.507",
        "price":19.99,
        "status":"ACTIVE",
        "currency":"GBP"
    }
```

### Cancel offer ###

#### Example request

```
    curl --request DELETE \
      --url http://localhost:40200/offers/1/cancel \
      --header 'content-type: application/json'
```

#### Example response

```
    HTTP/1.1 200 
    Content-Type: text/plain;charset=UTF-8
    
    { 
        "message" : "Offer cancelled"
    }
```

### Note about identifiers 
On a production database using auto incremented sequential identifiers should be discouraged, we have used sequential generation of identifiers for ease of testing. 