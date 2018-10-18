# SEND EMAIL API
For consume this REST API sned a POST request like this:

URL: localhost:8080/email

ContentType: application/json

Body:
```
{
  "to":
  [{"name": "Maxi",
    "email": "maxi.ruiz140@gmail.com"
	},
	{"name": "Tere",
    "email": "teresita.garro.castro@globant.com"
	}],
  "cc":
    [{"name": "Edargdo",
      "email": "edgardo140@gmail.com"
  	},
  	{"name": "Mai",
      "email": "mai.mua@rakenapp.com"
  	}],
  "bbc":
    [{"name": "Lautaro",
      "email": "lauti321@rakenapp.com"
  	}],
  "subject": "Hi World!",
  "body": "Have a good day!",
  "enrich": true
}
```

When you set "enrich : true", this API will consume another REST service and add the result in the email content.
