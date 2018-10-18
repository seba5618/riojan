# SEND EMAIL API
For consume this REST API sned a POST request like this:

URL: localhost:8080/email

ContentType: application/json

Body:
{
  "to": 
  [{"name": "Maxi",
    "email": "maxi.ruiz140@gmail.com"
	},
	{"name": "Tere",
    "email": "teresita.garro.castro@globant.com"
	}],
  "subject": "Hi World!",
  "body": "Have a good day!",
  "sendQuote": true
}
