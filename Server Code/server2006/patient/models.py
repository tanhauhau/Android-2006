from django.db import models
from django.contrib.auth.models import User
from location.models import Location

class Patient(models.Model):
	account = models.ForeignKey(User)
	type = models.IntegerField() #0: contact via phone, #1: via email
	address = models.ForeignKey(Location)
	phoneNumber = models.CharField(max_length=50)
	birthday = models.DateField()