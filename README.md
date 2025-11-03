Features

Multiple Coupon Types: Discount, B1G1 (Buy X Get Y), Cashback, Trip Discount -> (Yet to Implement B1G1 Logic, Trip Discount)
Flexible Condition System: Support for complex conditions with AND/OR logic (Compound Coupon Condition Fully not done)
Strategy Pattern: Easy to extend with new coupon types
MySQL Database: Persistent storage with JPA/Hibernate
RESTful API: Complete CRUD operations + coupon application endpoints

API Endpoints: 

**Create Coupon : **

POST /api/coupon
Content-Type: application/json
Payload:
{
  "code": "SAVE10",
  "type": "DISCOUNT",
  "description": "10% off on orders above ₹500",
  "expiryDate": "2025-12-31T23:59:59",
  "isActive": true,
  "usageLimit": 100,
  "couponDetails": {
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "maxDiscountAmount": 200,
    "discountScope": "CART_LEVEL"
  },
  "conditions": [
  {
      "type": "MIN_CART_VALUE",
     "conditionData": "{\"minValue\": 500}",
      "errorMessage": "Cart value must be at least ₹500"
    }
  ]
}




**Get All Coupons **

GET /api/coupons
Content-Type: application/json


**Get Coupon By Id**

GET /api/coupon/{id} 
Content-Type : application/json


**updateCoupon** 

PUT /api/coupon/{id} 
Content-Type: application/json 

PAYLOAD: 

{
  "code": "SAVE10",
  "type": "DISCOUNT",
  "description": "10% off on orders above ₹500",
  "expiryDate": "2025-12-31T23:59:59",
  "isActive": true,
  "usageLimit": 100,
  "couponDetails": {
    "discountType": "PERCENTAGE",
    "discountValue": 10,
    "maxDiscountAmount": 200,
    "discountScope": "CART_LEVEL"
  },
  "conditions": [
  {
      "type": "MIN_CART_VALUE",
     "conditionData": "{\"minValue\": 500}",
      "errorMessage": "Cart value must be at least ₹500"
    }
  ]
} 




**Delete Coupon ** 

Delete /api/coupon/{id}


**Get Applicable Coupons**


POST /api/applicable-coupons
Content-Type : application-json

Payload: 
{
  "userId": "USER123",
  "userType": "NEW",
  "paymentMethod": "UPI",
  "items": [
    {
      "productId": "PROD001",
      "productName": "Laptop",
      "categoryId": "ELECTRONICS",
      "price": 50000,
      "quantity": 1
    },
    {
      "productId": "PROD002",
      "productName": "Mouse",
      "categoryId": "ACCESSORIES",
      "price": 500,
      "quantity": 2
    }
  ],
  "location": {
    "city": "Mumbai",
    "state": "Maharashtra",
    "pincode": "400001"
  }
}



**Apply Coupon**

POST /api/apply-coupon/{id}

{
  "userId": "USER123",
  "userType": "REGULAR",
  "items": [
    {
      "productId": "PROD001",
      "productName": "Laptop",
      "price": 50000,
      "quantity": 1
    }
  ]
}







