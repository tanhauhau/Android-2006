from django.conf.urls import patterns, url
from django.contrib import admin
import doctor
urlpatterns = patterns('',
	#api
	url(r'^account/username/check$', 'api.views.check_username'),
	url(r'^account/register$', 'api.views.register'),
	url(r'^account/login$', 'api.views.login'),
	url(r'^account/view$', 'api.views.view_account'),
	url(r'^account/update$', 'api.views.update_account'),
	url(r'^account/password/update$', 'api.views.update_password'),
	url(r'^account/admin/new$', 'api.views.new_admin'),
	#### appointment ####
	url(r'^appointment/clinic_loc$', 'api.views.clinic_location_from_type'),
	url(r'^appointment/timeslot/list$', 'api.views.timeslot_list'),
	url(r'^appointment/doctor/list$', 'api.views.doctor_list'),
	url(r'^appointment/make$', 'api.views.make_appointment'),
	url(r'^appointment/delete$', 'api.views.remove_appointment'),
	url(r'^appointment/modify$', 'api.views.modify_appointment'),
	url(r'^appointment/list$', 'api.views.list_appointment'),
	url(r'^appointment/timeslot/doctor/list$', 'api.views.timeslot_for_doctor'),
	url(r'^location/clinic$', 'api.views.location_for_clinic'),
	url(r'^clinic/location$', 'api.views.clinic_from_location'),
	url(r'^doctor/clinic$', 'api.views.doctor_from_clinic'),
	#### clinic	####
	url(r'^clinic/create$', 'api.views.create_clinic'),
	url(r'^clinic/edit$', 'api.views.edit_clinic'),
	url(r'^clinic/delete$', 'api.views.delete_clinic'),
	url(r'^clinic/get$', 'api.views.get_clinic'),
	url(r'^clinic/stat$', 'api.views.get_clinic_stat'),
	url(r'^clinic/list$', 'api.views.list_clinic'),
	#### doctor ####
	url(r'^doctor/create$', 'api.views.create_doctor'),
	url(r'^doctor/edit$', 'api.views.edit_doctor'),
	url(r'^doctor/delete$', 'api.views.delete_doctor'),
	url(r'^doctor/link$', 'api.views.link_doctor_clinic'),
	url(r'^doctor/list$', 'api.views.list_doctor'),
	url(r'^doctor/appointment/list$', 'api.views.list_doctor_appointment'),
	url(r'^doctor/check/appointment$', 'api.views.check_appointment'),
)
