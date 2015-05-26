from clinic.models import Clinic
from location.models import Location
from doctor.models import Doctor
from django.contrib.auth.models import User
from appointment.models import Appointment
from django.core import serializers
from django.http import *
from django.template.response import TemplateResponse
from django.views.decorators.csrf import csrf_protect
from django.contrib.auth.decorators import user_passes_test, login_required

import datetime

def isstaff(user):
	return user.is_staff

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
def create_doctor_page(request, clinic_id):
	try:
		clinic = Clinic.objects.get(id=clinic_id)
		return TemplateResponse(request, 'doctor_create.html', {'clinic':clinic})
	except Clinic.DoesNotExists:
		return HttpResponseRedirect('/clinic/list')

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
def edit_doctor_page(request, doctor_id):
	try:
		return TemplateResponse(request, 'doctor_edit.html', {'doctor': Doctor.objects.prefetch_related('account').get(id=doctor_id)})
	except Clinic.DoesNotExist:
		return HttpResponseRedirect('/clinic/list')

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
@csrf_protect
def create_doctor(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['name'] or 		\
		not request.POST['type'] or 	\
		not request.POST['clinicid'] or \
		not request.POST['password'] or \
		not request.POST['password2'] or \
		not request.POST['email']:
		return HttpResponseBadRequest()
	if request.POST['password'] != request.POST['password2']:
		return HttpResponseBadRequest()
	
	try:
		clinic = Clinic.objects.get(id=int(request.POST['clinicid']))
		account = User.objects.create_user(request.POST['name'], request.POST['email'], request.POST['password'])
		doctor = Doctor.objects.create(account=account,
									type=request.POST['type'],
									clinic=clinic)
		account.save()						
		doctor.save()
		return HttpResponse(doctor.id)
	except Clinic.DoesNotExist:
		return HttpResponseBadRequest()

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
@csrf_protect
def edit_doctor(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['name'] or 		\
		not request.POST['type'] or 	\
		not request.POST['doctorid']:
		return HttpResponseBadRequest()
	
	try:
		doctor = Doctor.objects.get(id=int(request.POST['doctorid']))
		doctor.type = request.POST['type']
		doctor.save()
		
		account = doctor.account
		account.username = request.POST['name']
		account.save()
		return HttpResponse(doctor.id)
	except Doctor.DoesNotExist:
		return HttpResponseBadRequest()

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
def delete_doctor(request, doctorid):
	try:
		doctor = Doctor.objects.get(id=doctorid)
		doctor.delete()
	except:
		pass
	return HttpResponse("")
