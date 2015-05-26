from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.contrib import auth
from django.template.response import TemplateResponse
from doctor.models import Doctor
from patient.models import Patient
from django.http import *

def home(request):
	return HttpResponseRedirect("/clinic/list")

def login_page(request):
	if request.user and request.user.is_authenticated():
		try:
			Patient.objects.get(account=user)
			return HttpResponseRedirect("/patient")
		except:
			try:
				doctor = Doctor.objects.get(account=user)
				return HttpResponseRedirect("/doctor/" + str(doctor.id) + "/appointment")
			except:
				return HttpResponseRedirect("/clinic/list")
	return TemplateResponse(request, "login_page.html", {})

def login(request):
	if request.method != "POST":
		return HttpResponseNotAllowed(['POST'])
	if not request.POST['name'] or 		\
		not request.POST['password']:
		return HttpResponseBadRequest()
	
	user = authenticate(username=request.POST['name'], password=request.POST['password'])
	if user is not None:
		#depends on what type of user, redirect them to different place
		auth.login(request, user)
		try:
			Patient.objects.get(account=user)
			return HttpResponse("/patient")
		except:
			return HttpResponse("/clinic/list")
			
		pass
	return HttpResponseBadRequest()
	
def logout(request):
	auth.logout(request)
	return HttpResponseRedirect("/login")