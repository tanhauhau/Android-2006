from django.db import models
from doctor.models import Doctor
from patient.models import Patient

class Appointment(models.Model):
	done = models.BooleanField(default=False)
	cancel = models.BooleanField(default=False)
	time_appointed = models.DateTimeField()
	time_created = models.DateTimeField(auto_now_add=True)
	time_modified = models.DateTimeField(auto_now=True)
	doctor = models.ForeignKey(Doctor, related_name='doctor_appointments')
	patient = models.ForeignKey(Patient, related_name='patient_appointments')
	patientnote = models.CharField(max_length=100000, default="")
	doctornote = models.CharField(max_length=100000, default="")

class AppointmentReminder(models.Model):
	appointment = models.ForeignKey(Appointment, related_name="reminders")
	note = models.CharField(max_length=10000, default="")
	time_created = models.DateTimeField(auto_now_add=True)