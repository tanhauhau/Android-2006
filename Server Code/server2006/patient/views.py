from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
from django.template.response import TemplateResponse
from patient.models import Patient
from location.models import Location
from django.http import *
from datetime import date
from django.contrib.auth.decorators import user_passes_test, login_required

def ispatient(user):
	try:
		Patient.objects.get(account=user)
		return True
	except:
		return False

def signup_page(request):
	return TemplateResponse(request, "signup_page.html", {})

def signup(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['name'] or \
		not request.POST['birthday'] or \
		not request.POST['password'] or \
		not request.POST['password2'] or \
		not request.POST['phone'] or \
		not request.POST['email'] or \
		not request.POST['address'] or \
		not request.POST['postal'] or \
		not request.POST['city'] or \
		not request.POST['country']:
		return HttpResponseBadRequest()

	if not request.POST['password'] == request.POST['password2']:
		return HttpResponseBadRequest()
	
	try:
		location = Location.objects.create(city=request.POST['city'],
											country=request.POST['country'],
											address=request.POST['address'],
											postalCode=request.POST['postal'])

		day, month, year = map(int, request.POST['birthday'].split('/'))
		birthday = date(year, month, day)
		account = User.objects.create_user(request.POST['name'], request.POST['email'], request.POST['password'])

		patient = Patient.objects.create(account=account,
										birthday=birthday,
										phoneNumber=request.POST['phone'],
										address=location,
										type=request.POST['type'])
		account.save()	
		location.save()
		patient.save()

		#log in the registered user
		user = authenticate(username=account.username, password=request.POST['password'])
		if user is not None:
			#depends on what type of user, redirect them to different place
			try:
				login(request, user)
				return HttpResponse("/patient")
			except:
				pass
	except:
		pass
	return HttpResponseBadRequest()
	
@user_passes_test(ispatient, login_url='/login')
@login_required(login_url='/login')
def profile_page(request):
	if request.user.is_authenticated():
		try:
			patient = Patient.objects.get(account=request.user)
			return TemplateResponse(request, "profile.html", {'patient':patient})
		except Patient.DoesNotExist:
			pass
	return HttpResponse("no user")
	
@user_passes_test(ispatient, login_url='/login')
@login_required(login_url='/login')
def edit_profile_page(request):
	try:
		patient = Patient.objects.get(account=request.user)
		return TemplateResponse(request, "edit_profile.html", {'patient':patient})
	except Patient.DoesNotExist:
		return HttpResponseRedirect("/patient")
		
@user_passes_test(ispatient, login_url='/login')
@login_required(login_url='/login')
def edit_profile(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['birthday'] or \
		not request.POST['phone'] or \
		not request.POST['email'] or \
		not request.POST['address'] or \
		not request.POST['postal'] or \
		not request.POST['city'] or \
		not request.POST['country']:
		return HttpResponseBadRequest()
	try:
		patient = Patient.objects.get(account=request.user)
		
		day, month, year = map(int, request.POST['birthday'].split('/'))
		patient.birthday = date(year, month, day)
		patient.phoneNumber = request.POST['phone']
		patient.save()
		
		account = patient.account
		account.email = request.POST['email']
		account.save()
		
		address = patient.address
		address.address = request.POST['address']
		address.postal = request.POST['postal']
		address.city = request.POST['city']
		address.country = request.POST['country']
		address.save()
	
		return HttpResponse("/patient")
	except:
		pass
	return HttpResponseBadRequest()