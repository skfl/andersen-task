## Hey, i'm here to help you

### End-points

*     /book/all?sort=xx  ( xx => id, name, price, status )

*     /book/{bookId}/status  ( request format: {"status":"xx"} , xx => out_of_stock, available)

*     /request/all

*     /request/new ( request format: {"bookId":"1"} )

*     /request/book/{bookId} 

*     /request/quantity-of-requests

*     /order/all?sort=xx  ( xx => id, cost, status, time )

*     /order/{orderId}

*     /order/new ( request format: {"bookIds":["1","2"]} )

*     /order/{orderId}/status ( request format: {"status":"xx"} , xx => canceled, completed )