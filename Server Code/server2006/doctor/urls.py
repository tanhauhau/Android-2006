from django.conf.urls import patterns, url
from django.contrib import admin
import doctor
urlpatterns = patterns('',
	#doctor
	url(r'^create/(\d+)$', 'doctor.views.create_doctor_page'),
	url(r'^create/submit$', 'doctor.views.create_doctor'),
	url(r'^edit/(\d+)$', 'doctor.views.edit_doctor_page'),
	url(r'^edit/submit$', 'doctor.views.edit_doctor'),
	url(r'^delete/(\d+)$', 'doctor.views.delete_doctor'),
	url(r'^(\d+)/appointment$', 'appointment.views.list_by_doctor'),
)
