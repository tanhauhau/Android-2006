from django.conf.urls import patterns, url
from django.contrib import admin
import clinic
urlpatterns = patterns('',
	url(r'^$', 'clinic.views.list_clinic_page'),
	url(r'^create$', 'clinic.views.create_clinic_page'),
	url(r'^create/submit$', 'clinic.views.create_clinic'),
	url(r'^list$', 'clinic.views.list_clinic_page'),
	url(r'^edit/(\d+)$', 'clinic.views.edit_clinic_page'),
	url(r'^edit/submit$', 'clinic.views.edit_clinic'),
	url(r'^delete/(\d+)$', 'clinic.views.delete_clinic'),
)
