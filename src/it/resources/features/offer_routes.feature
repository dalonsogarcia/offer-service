@offer_routes
Feature: Offer Controller Routes tests

  Scenario Outline: Confirm the Offer controller endpoints produce the correct responses
    Given there are <initialOffers> offers in the database
    When a <httpMethod> request is made to <path> with offer description: <description>, expiration date: <expirationDate>, price: <price> and status: <offerStatus>
    Then the service should reply with status code <statusCode>
    And the number of offers returned should be <numberOfOffers>
    And the location header value is <locationHeader>
    Examples:
      | initialOffers | httpMethod | path      | description           | expirationDate          | price  | offerStatus | statusCode         | numberOfOffers | locationHeader |
      | 0             | GET        | /         | na                    | na                      | 0.0    | na          | OK                 | 0              | na             |
      | 1             | GET        | /         | na                    | na                      | 0.0    | na          | OK                 | 1              | na             |
      | 1             | GET        | /1        | na                    | na                      | 0.0    | na          | OK                 | na             | na             |
      | 2             | GET        | /         | na                    | na                      | 0.0    | na          | OK                 | 2              | na             |
      | 2             | GET        | /2        | na                    | na                      | 0.0    | na          | OK                 | na             | na             |
      | 2             | GET        | /3        | na                    | na                      | 0.0    | na          | NOT_FOUND          | na             | na             |
      | 2             | PUT        | /2        | Updated description   | 2018-04-17T16:28:12.507 | 9.99   | CANCELLED   | OK                 | na             | na             |
      | 2             | PUT        | /3        | Reupdated description | 2018-04-17T16:28:12.507 | 9.99   | EXPIRED     | NOT_FOUND          | na             | na             |
      | 2             | PUT        | /         | Reupdated description | 2018-04-17T16:28:12.507 | 9.99   | ACTIVE      | METHOD_NOT_ALLOWED | na             | na             |
      | 2             | PUT        | /2        | na                    | 2018-04-17T16:28:12.507 | 19.99  | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | PUT        | /2        | A description         | 2015-04-17T16:28:12.507 | 19.99  | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | PUT        | /2        | A description         | na                      | 19.99  | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | PUT        | /2        | A description         | 2015-04-17              | 19.99  | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | PUT        | /2        | A description         | 2018-04-17T16:28:12.507 | -19.99 | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | PUT        | /2        | A description         | 2018-04-17T16:28:12.507 | 19.99  | BANANA      | BAD_REQUEST        | na             | na             |
      | 2             | POST       | /         | New offer             | 2018-04-17T16:28:12.507 | 19.99  | ACTIVE      | CREATED            | na             | /offers/3      |
      | 2             | POST       | /         | New offer             | 2018-04-17T16:28:12.507 | 19.99  | CANCELLED   | CREATED            | na             | /offers/4      |
      | 2             | POST       | /         | New offer             | 2018-04-17T16:28:12.507 | 19.99  | RANDOM      | BAD_REQUEST        | na             | na             |
      | 2             | POST       | /         | New offer             | 2017-04-17T16:28:12.507 | 19.99  | EXPIRED     | BAD_REQUEST        | na             | na             |
      | 2             | POST       | /         | New offer             | 2017-14-17T16:28:12.507 | 19.99  | EXPIRED     | BAD_REQUEST        | na             | na             |
      | 2             | POST       | /         | New offer             | na                      | 19.99  | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | POST       | /         | New offer             | 2018-04-17              | 19.99  | ACTIVE      | BAD_REQUEST        | na             | na             |
      | 2             | POST       | /1        | New offer             | 2018-04-17T16:28:12.507 | 19.99  | ACTIVE      | METHOD_NOT_ALLOWED | 2              | na             |
      | 2             | POST       | /         | na                    | 2018-04-17T16:28:12.507 | 19.99  | ACTIVE      | BAD_REQUEST        | 3              | na             |
      | 2             | POST       | /         | New offer             | 2018-04-17T16:28:12.507 | -2.0   | ACTIVE      | BAD_REQUEST        | 3              | na             |
      | 2             | DELETE     | /1/cancel | na                    | na                      | 0.0    | na          | OK                 | 1              | na             |
      | 2             | DELETE     | /2/cancel | na                    | na                      | 0.0    | na          | OK                 | 2              | na             |
      | 2             | DELETE     | /3/cancel | na                    | na                      | 0.0    | na          | NOT_FOUND          | 2              | na             |