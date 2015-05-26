from clinic.models import Clinic
from location.models import Location
from doctor.models import Doctor
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
def create_clinic_page(request):
	print "Create_clinic_page"
	return TemplateResponse(request, 'clinic_create.html', {})

def list_clinic_page(request):
	template = 'public_clinic_list.html'
	if request.user and request.user.is_authenticated():
		if request.user.is_staff:
			template = 'clinic_list.html'
	clinics = Clinic.objects.prefetch_related('doctors__doctor_appointments', 'doctors__account', 'location').all()
	return TemplateResponse(request, template, {'clinics': clinics, 'request': request})

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
def edit_clinic_page(request, clinic_id):
	try:
		return TemplateResponse(request, 'clinic_edit.html', {'clinic': Clinic.objects.prefetch_related('location').get(id=clinic_id)})
	except Clinic.DoesNotExist:
		return HttpResponseRedirect('/clinic/list')

#parameter needed: 	city, country, address, postal, name, contact
@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
@csrf_protect
def create_clinic(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['city'] or 		\
		not request.POST['country'] or 	\
		not request.POST['address'] or	\
		not request.POST['postal'] or	\
		not request.POST['name'] or		\
		not request.POST['contact']:
		return HttpResponseBadRequest()
	
# 	print "create location"
	location = Location.objects.create(city=request.POST['city'],
									country=request.POST['country'],
									address=request.POST['address'],
									postalCode=request.POST['postal'])
# 	print "create clinic"
	clinic = Clinic.objects.create(location=location,
									clinicName=request.POST['name'],
									contact=request.POST['contact']) 
# 	print "save clinic"
	clinic.save()
	return HttpResponse(clinic.id)

#parameter needed: 	city, country, address, postal, name, contact
@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
@csrf_protect
def edit_clinic(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
		
	if 	not request.POST['clinicid'] or 		\
		not request.POST['city'] or 		\
		not request.POST['country'] or 	\
		not request.POST['address'] or	\
		not request.POST['postal'] or	\
		not request.POST['name'] or		\
		not request.POST['contact']: 
		return HttpResponseBadRequest()
	
	clinic = Clinic.objects.get(id=int(request.POST['clinicid']))
	location = clinic.location
	location.city = request.POST['city']
	location.country = request.POST['country']
	location.address = request.POST['postal']
	location.postalCode = request.POST['postal']
	location.address = request.POST['address']
	location.save()
	clinic.clinicName = request.POST['name']
	clinic.contact = request.POST['contact']
	clinic.save()
	return HttpResponse(str(clinic.id))

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
def delete_clinic(request, clinicid):
	try:
		clinic = Clinic.objects.get(id=clinicid)
		clinic.delete()
	except:
		pass
	return HttpResponse("")

@user_passes_test(isstaff, login_url='/login')
@login_required(login_url='/login')
def get_clinic(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['clinicid']:
		return HttpResponseBadRequest()
	clinic = Clinic.objects.get(id=int(request.POST['clinicid'])).prefetch_related('doctors','location')
	return JsonResponse(serializers.serialize('json', clinic))
	
#country
#city
#postal code
#appointment type, eg: #0: general, #1: dental, #2: ent, #3: wh
#appointment type can be comma separated value
#a, true->show clinic with appointment available, false->show all clinics
#date for filter based on date
def list_clinic(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
		
	#location based
	query = Clinic.objects.prefetch_related('doctors__doctor_appointments', 'location')
	if request.POST['country']:
		query = query.filter(location__country=request.POST['country'])
	if request.POST['city']:
		query = query.filter(location__city=request.POST['city'])
	if request.POST['postal']:
		query = query.filter(location__city=request.POST['postal'])
	
	#appointment type based	
	if request.POST['apptype']:
		csv = [int(d) for d in request.POST['apptype'].split(",") if d.isdigit()]
		for apptype in csv:
			query = query.filter(doctors__type=int(apptype))
	
	if request.POST["a"] and request.POST["a"].lower() == "true":		
		#date time based
		today = datetime.date.today()
		if request.POST['date']:
			try:
				timestamp = float(request.POST['date'])
				today = datetime.date.fromtimestamp(float)
			except ValueError:
				pass
		start = datetime.datetime.combine(today, datetime.time.min)
		end = datetime.datetime.combine(today, datetime.time.max)
		query = query.filter(doctors__doctor_appointments__time__gte=start)
		query = query.filter(doctors__doctor_appointments__time__gte=end)
			
	data = serializers.serialize('json', query)
	return JsonResponse(data)