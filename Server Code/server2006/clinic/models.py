from django.db import models
from location.models import Location

class Clinic(models.Model):
	location = models.ForeignKey(Location)
	clinicName = models.CharField(max_length=50)
	contact = models.CharField(max_length=20)