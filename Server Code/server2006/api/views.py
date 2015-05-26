from clinic.models import Clinic
from location.models import Location
from doctor.models import Doctor
from patient.models import Patient
from appointment.models import Appointment
from django.contrib.auth.models import User
from django.db.models import Q
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.decorators import user_passes_test, login_required
from django.contrib.auth import authenticate
import datetime
import pytz
import json
import string
import random
from django.core import serializers

@csrf_exempt
def check_username(request):
	if request.method == "POST" and "username" in request.POST:
		username = request.POST["username"]
		try:
			User.objects.get(username=username)
			return JsonResponse(json.dumps({'username':username, 'exists':True}), safe=False)
		except User.DoesNotExist:
			return JsonResponse(json.dumps({'username':username, 'exists':False}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def register(request):
	if request.method == "POST":
		if 'name' in request.POST and \
			'birthday' in request.POST and \
			'password' in request.POST and \
			'password2' in request.POST and \
			'phone' in request.POST and \
			'email' in request.POST and \
			'address' in request.POST and \
			'postal' in request.POST and \
			'city' in request.POST and \
			'country' in request.POST and \
			'type' in request.POST:
			if not request.POST['password'] == request.POST['password2']:
				return JsonResponse(json.dumps({'error':'Password not the same'}), safe=False)
			
			try:
				location = Location.objects.create(city=request.POST['city'],
													country=request.POST['country'],
													address=request.POST['address'],
													postalCode=request.POST['postal'])

				day, month, year = map(int, request.POST['birthday'].split('/'))
				birthday = datetime.date(year, month, day)
				account = User.objects.create_user(request.POST['name'], request.POST['email'], request.POST['password'])

				patient = Patient.objects.create(account=account,
												birthday=birthday,
												phoneNumber=request.POST['phone'],
												address=location,
												type=request.POST['type'])
				account.save()	
				location.save()
				patient.save()
			
				return JsonResponse(json.dumps({"register":"success"}), safe=False)
			except Exception, an_error:
				return JsonResponse(json.dumps({'error':'exception'}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt	
def login(request):
	if request.method == "POST" and \
		'username' in request.POST and \
		'password' in request.POST:
				
		user = authenticate(username=request.POST['username'], password=request.POST['password'])
		
		if user is not None:
			
			if user.is_staff:
				return JsonResponse(json.dumps({'login':'admin'}), safe=False)
			try:
				Patient.objects.get(account=user)
				return JsonResponse(json.dumps({'login':'patient'}), safe=False)
			except Patient.DoesNotExist:
				pass
			try:
				Doctor.objects.get(account=user)
				return JsonResponse(json.dumps({'login':'doctor'}), safe=False)
			except Doctor.DoesNotExist:
				pass
				
		return JsonResponse(json.dumps({'login':'failed'}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def view_account(request):
	if request.method == "POST":
		try:
			patient = Patient.objects.prefetch_related("address", "account").get(account=request.user)
			return JsonResponse(json.dumps({'username': patient.account.username,
											'phone': patient.phoneNumber,
											'email': patient.account.email,
											 'address': patient.address.address,
											 'type': patient.type,
											 'postal': patient.address.postalCode,
											 'city': patient.address.city,
											 'country': patient.address.country}), 
											 safe=False)
		except Patient.DoesNotExist:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
@csrf_exempt
def update_account(request):
	if request.method == "POST":
		if 'phone' in request.POST and \
			'email' in request.POST and \
			'address' in request.POST and \
			'postal' in request.POST and \
			'city' in request.POST and \
			'country' in request.POST and \
			'type' in request.POST:
			try:
				patient = Patient.objects.prefetch_related("address", "account").get(account=request.user)
				patient.phoneNumber = request.POST['phone']
				patient.type = request.POST['type']
				
				account = patient.account
				account.email = request.POST['email']
				account.save()
		
				address = patient.address
				address.address = request.POST['address']
				address.postalCode = request.POST['postal']
				address.city = request.POST['city']
				address.country = request.POST['country']
				address.save()
				
				patient.address = address
				patient.account = account
				patient.save()
			
				return JsonResponse(json.dumps({'success':'success'}), safe=False)
			except Patient.DoesNotExist:
				pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def update_password(request):
	if request.method == "POST" and \
		"newpassword" in request.POST:
		try:
			user = request.user
			user.set_password(request.POST["newpassword"])
			user.save()
			return JsonResponse(json.dumps({'success':'success'}), safe=False)			
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def new_admin(request):
	if request.method == "POST" and \
		"username" in request.POST and \
		"password" in request.POST and \
		"newadmin" in request.POST and \
		"newpass" in request.POST:
		try:
			if request.user is not None and request.user.is_staff:
				account = User.objects.create_user(request.POST['newadmin'], request.POST['newadmin'], request.POST['newpass'])
				account.is_staff = True
				account.save()
				return JsonResponse(json.dumps({'success':'success'}), safe=False)			
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

###############################################
#####			Appointment					###	
###############################################
@csrf_exempt
def clinic_location_from_type(request):
	if request.method == "POST":
		if 'type' in request.POST:			
			clinics = Clinic.objects.prefetch_related("doctors", "location").filter(doctors__type=request.POST['type'])
			cities = []
			for clinic in clinics:
				if clinic.location.city not in cities:
					cities.append(clinic.location.city)
	
			return JsonResponse(json.dumps({'locations': cities}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def timeslot_list(request):
	if request.method == "POST" and \
		"type" in request.POST and \
		"city" in request.POST and \
		"date" in request.POST:
		
		type = int(request.POST["type"])
		city = request.POST["city"]
		date = request.POST["date"]
	
		doctors = Doctor.objects.prefetch_related("clinic__location", "doctor_appointments")	\
								.filter(type=type)						\
								.filter(clinic__location__city=city)
	
		timeslots = {}
		num_doctor = len(doctors)
		for t in range(9,17):
			timeslots[datetime.time(t,0)] = num_doctor
			timeslots[datetime.time(t,30)] = num_doctor
		
		day, month, year = map(int, date.split('/'))
		date = datetime.date(year, month, day)
		start = pytz.utc.localize(datetime.datetime.combine(date, datetime.time.min))
		end = pytz.utc.localize(datetime.datetime.combine(date, datetime.time.max))
	
		for doctor in doctors:
			appointments = doctor.doctor_appointments.filter(time_appointed__gte=start).filter(time_appointed__lte=end)
			for appointment in appointments:
				ta = appointment.time_appointed
			
				t = datetime.time(ta.hour, ta.minute)
				print(ta)
				if t in timeslots:
					timeslots[t] = timeslots[t] - 1
			
		result = []
		for t in sorted(timeslots):
			if timeslots[t] > 0:
				result.append({'time':str(t),'n':timeslots[t]})
	
		return JsonResponse(json.dumps({'timeslots': result}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
@csrf_exempt
def doctor_list(request):
	if request.method == "POST" and \
		"type" in request.POST and \
		"city" in request.POST and \
		"date" in request.POST and \
		"timeslot" in request.POST:
		
		type = request.POST["type"]
		city = request.POST["city"]
		date = request.POST["date"]
		timeslot = request.POST["timeslot"]
	
		day, month, year = map(int, date.split('/'))
		hour, minute, second = map(int, timeslot.split(':'))
		t = datetime.datetime(year, month, day, hour, minute, second, 0, pytz.UTC)
		
		doctors = Doctor.objects.prefetch_related("clinic__location", "doctor_appointments", "account", "clinic")	\
								.filter(type=type)						\
								.filter(clinic__location__city=city)	\
								.exclude(doctor_appointments__time_appointed=t)
	
		result = []
		for doctor in doctors:
			result.append({'id':doctor.id, 'name': doctor.account.username, 'clinic': {'name':doctor.clinic.clinicName}})
	
		return JsonResponse(json.dumps({'doctors': result}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
		
@csrf_exempt
@login_required(login_url='/login')
def list_appointment(request):
	if request.method == "POST":
		patient = Patient.objects.prefetch_related('patient_appointments__doctor__clinic', 'patient_appointments__doctor__account').get(account=request.user)
		a = []
		for appointment in patient.patient_appointments.all():
			loc = appointment.doctor.clinic.location
			a.append({'id':appointment.id, 
					'doctor': {'id': appointment.doctor.id,
								'name': appointment.doctor.account.username,
								'clinic': {'id': appointment.doctor.clinic.id,
											'name': appointment.doctor.clinic.clinicName,
											'location': {'city': loc.city,
														'country': loc.country,
														'address': loc.address,
														'postalCode': loc.postalCode},
											'contact': appointment.doctor.clinic.contact
											}
								},
						'note': appointment.patientnote,
						'date': str(appointment.time_appointed.date()),
						'time': str(appointment.time_appointed.time())
						})
		return JsonResponse(json.dumps({'appointments': a}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def location_for_clinic(request):
	if request.method == "POST":
		locations = Clinic.objects.values_list('location__city', flat=True).distinct().order_by('location__city')
# 		ids = Entry.objects.values_list('column_name', flat=True).filter(...)
# my_models = MyModel.objects.filter(pk__in=set(ids))
		return JsonResponse(json.dumps({'cities': map(unicode, locations)}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False) 
		
@csrf_exempt
def clinic_from_location(request):
	if request.method == "POST" and "city" in request.POST:
		clinics = Clinic.objects.filter(location__city=request.POST["city"]).all()
		result = []
		for clinic in clinics:
			result.append({'id':clinic.id, 'name': clinic.clinicName, 'contact': clinic.contact})
		return JsonResponse(json.dumps({'clinics': result}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def doctor_from_clinic(request):
	if request.method == "POST" and "clinic" in request.POST:
		clinic = Clinic.objects.get(id=request.POST["clinic"])
		doctors = Doctor.objects.filter(clinic=clinic).all()
		result = []
		for doctor in doctors:
			result.append({'id':doctor.id, 'name': doctor.account.username, 'clinic': {'name':doctor.clinic.clinicName}})
				
		return JsonResponse(json.dumps({'doctors': result}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
@csrf_exempt	
def timeslot_for_doctor(request):
	if request.method == "POST" and \
		"doctor" in request.POST and \
		"date" in request.POST:
		day, month, year = map(int, request.POST["date"].split('/'))
		date = datetime.date(year, month, day)
		start = pytz.utc.localize(datetime.datetime.combine(date, datetime.time.min))
		end = pytz.utc.localize(datetime.datetime.combine(date, datetime.time.max))
		try:
			doctor = Doctor.objects.prefetch_related('doctor_appointments').get(id=request.POST["doctor"])
			appointments = doctor.doctor_appointments.filter(time_appointed__gte=start).filter(time_appointed__lte=end)
			timeslots = {}
			for t in range(9,17):
				timeslots[datetime.time(t,0)] = 1
				timeslots[datetime.time(t,30)] = 1
			for appointment in appointments:
				ta = appointment.time_appointed
				t = datetime.time(ta.hour, ta.minute)
				if t in timeslots:
					timeslots[t] = 0
		
			result = []
			for t in sorted(timeslots):
				if timeslots[t] > 0:
					result.append({'time':str(t),'n':timeslots[t]})
			return JsonResponse(json.dumps({'timeslots': result}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def remove_appointment(request):
	if request.method == "POST" and \
		"id" in request.POST:
		try:
			appointment = Appointment.objects.get(id=request.POST["id"])
			appointment.delete()
			return JsonResponse(json.dumps({'success':'success'}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
			
@csrf_exempt
@login_required(login_url='/login')
def make_appointment(request):
	if request.method == "POST" and \
		"date" in request.POST and \
		"timeslot" in request.POST and \
		"doctor" in request.POST:	
		date = request.POST["date"]
		timeslot = request.POST["timeslot"]

		#get doctor
		doctor = Doctor.objects.get(id=int(request.POST["doctor"]))
		#get appointment time
		day, month, year = map(int, date.split('/'))
		hour, minute, second = map(int, timeslot.split(':'))
		t = datetime.datetime(year, month, day, hour, minute, second, 0, pytz.UTC)
		#get patient
		patient = Patient.objects.get(account=request.user)
		appointment = Appointment.objects.create(doctor=doctor, 
												patient=patient, 
												time_appointed=t)
									
		if "note" in request.POST:
			appointment.patientnote = request.POST["note"]
			
		appointment.save()
		
		loc = appointment.doctor.clinic.location		
		return JsonResponse(json.dumps({'appointment': {'id':appointment.id, 
														'doctor': {'id': appointment.doctor.id,
																	'name': appointment.doctor.account.username,
																	'clinic': {'id': appointment.doctor.clinic.id,
																				'name': appointment.doctor.clinic.clinicName,
																				'location': {'city': loc.city,
																							'country': loc.country,
																							'address': loc.address,
																							'postalCode': loc.postalCode},
																				'contact': appointment.doctor.clinic.contact
																				}
																	},
															'note': appointment.patientnote,
															'date': str(appointment.time_appointed.date()),
															'time': str(appointment.time_appointed.time())
															}
													}), safe=False)														
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def modify_appointment(request):
	if request.method == "POST" and \
		"date" in request.POST and \
		"timeslot" in request.POST and \
		"id" in request.POST:
		date = request.POST["date"]
		timeslot = request.POST["timeslot"]

		#get appointment time
		day, month, year = map(int, date.split('/'))
		hour, minute, second = map(int, timeslot.split(':'))
		t = datetime.datetime(year, month, day, hour, minute, second, 0, pytz.UTC)

		appointment = Appointment.objects.get(id=request.POST['id'])
		appointment.time_appointed=t
		appointment.save()
		loc = appointment.doctor.clinic.location		
		return JsonResponse(json.dumps({'appointment': {'id':appointment.id, 
														'doctor': {'id': appointment.doctor.id,
																	'name': appointment.doctor.account.username,
																	'clinic': {'id': appointment.doctor.clinic.id,
																				'name': appointment.doctor.clinic.clinicName,
																				'location': {'city': loc.city,
																							'country': loc.country,
																							'address': loc.address,
																							'postalCode': loc.postalCode},
																				'contact': appointment.doctor.clinic.contact
																				}
																	},
															'note': appointment.patientnote,
															'date': str(appointment.time_appointed.date()),
															'time': str(appointment.time_appointed.time())
															}
													}), safe=False)	
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
###############################################
#####				Clinic 					###	
###############################################
#parameter needed: 	city, country, address, postal, name, contact
@csrf_exempt
def create_clinic(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'city' in request.POST and \
		'country' in request.POST and \
		'address' in request.POST and \
		'postal' in request.POST and \
		'name' in request.POST and \
		'contact' in request.POST:
		
		location = Location.objects.create(city=request.POST['city'],
									country=request.POST['country'],
									address=request.POST['address'],
									postalCode=request.POST['postal'])
		clinic = Clinic.objects.create(location=location,
									clinicName=request.POST['name'],
									contact=request.POST['contact']) 
		clinic.save()
		
		return JsonResponse(json.dumps({'clinic':{
												'id':clinic.id,
												'name':clinic.clinicName,
												'contact': clinic.contact,
												'location': {'city': location.city,
															'country': location.country,
															'address': location.address,
															'postalCode': location.postalCode}
												}}), safe=False)
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def edit_clinic(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'clinicid' in request.POST and \
		'city' in request.POST and \
		'country' in request.POST and \
		'address' in request.POST and \
		'postal' in request.POST and \
		'name' in request.POST and \
		'contact' in request.POST:
		
		try:
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
			
			return JsonResponse(json.dumps({'clinic':{
										'id':clinic.id,
										'name':clinic.clinicName,
										'contact': clinic.contact,
										'location': {'city': location.city,
													'country': location.country,
													'address': location.address,
													'postalCode': location.postalCode}
										}}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def delete_clinic(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'id' in request.POST:
# 		try:
		clinic = Clinic.objects.get(id=request.POST['id'])
		clinic.delete()
		
		return JsonResponse(json.dumps({'success','success'}), safe=False)
# 		except:
# 			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def get_clinic(request):
	if request.method == "POST" and 'id' in request.POST:
		try:
			clinic = Clinic.objects.get(id=request.POST['id'])
			return JsonResponse(json.dumps({'clinic':{
										'id':clinic.id,
										'name':clinic.clinicName,
										'contact': clinic.contact,
										'location': {'city': clinic.location.city,
													'country': clinic.location.country,
													'address': clinic.location.address,
													'postalCode': clinic.location.postalCode}
										}}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def get_clinic_stat(request):
	if request.method == "POST" and 'id' in request.POST:
		try:
			clinic = Clinic.objects.prefetch_related('doctors__doctor_appointments').get(id=request.POST["id"])
			doctors = clinic.doctors.all()
			total_appointment = []
			for doctor in doctors:
				appointments = doctor.doctor_appointments.all()
				for appointment in appointments:
					total_appointment.append(appointment)
			return JsonResponse(json.dumps({'stats':{'doctor': len(doctors), 'appointment': len(total_appointment)}}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def list_clinic(request):
	if request.method == "POST":
		try:
			clinics = Clinic.objects.all()
			result = []
			for clinic in clinics:
				result.append({'id':clinic.id,
							'name':clinic.clinicName,
							'contact': clinic.contact,
							'location': {'city': clinic.location.city,
										'country': clinic.location.country,
										'address': clinic.location.address,
										'postalCode': clinic.location.postalCode}})
			return JsonResponse(json.dumps({'clinics': result}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
###############################################
#####				Doctor 					###	
###############################################
@csrf_exempt
def create_doctor(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'name' in request.POST and \
		'type' in request.POST and \
		'email' in request.POST:
		
		try:
			account = User.objects.create_user(request.POST['name'], request.POST['email'], request.POST['name'])
			doctor = Doctor.objects.create(name=request.POST['name'],
										account=account,
										type=int(request.POST['type']))
			account.save()
			doctor.save()
			return JsonResponse(json.dumps({'doctor':{'id':doctor.id, 
											'type':doctor.type, 
											'name': doctor.account.username,
											'email': doctor.account.email}
									}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def edit_doctor(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'type' in request.POST and \
		'id' in request.POST:
		try:
			doctor = Doctor.objects.get(id=int(request.POST['id']))
			doctor.type = int(request.POST['type'])
			doctor.save()
			if doctor.clinic:
				return JsonResponse(json.dumps({'doctor':{'id':doctor.id, 
												'type':doctor.type, 
												'name': doctor.account.username, 
												'email': doctor.account.email,
												'clinic': {'id':doctor.clinic.id, 
															'name':doctor.clinic.clinicName}
													}
										}), safe=False)
			else:
				return JsonResponse(json.dumps({'doctor':{'id':doctor.id, 
								'type':doctor.type, 
								'name': doctor.name}
						}), safe=False)

		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def delete_doctor(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'id' in request.POST:
		try:
			doctor = Doctor.objects.get(id=request.POST['id'])
			doctor.delete()
			
			return JsonResponse(json.dumps({'success':'success'}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
@csrf_exempt
def link_doctor_clinic(request):
	if request.user.is_staff and \
		request.method == "POST" and \
		'doctorid' in request.POST and \
		'clinicid' in request.POST:
# 		try:
		clinic = Clinic.objects.get(id=request.POST['clinicid'])
		doctor = Doctor.objects.get(id=request.POST['doctorid'])
		doctor.clinic = clinic
		doctor.save()
		return JsonResponse(json.dumps({'success':'success'}), safe=False)
# 		except:
# 			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
@csrf_exempt
def list_doctor(request):
	if request.method == "POST":
		try:
			doctors = Doctor.objects.prefetch_related('clinic', 'clinic__location').all()
			result = []
			for doctor in doctors:
				b = {'id':doctor.id, 
					'type':doctor.type, 
					'name': doctor.account.username,
					'email': doctor.account.email}
				if doctor.clinic:
					loc = doctor.clinic.location
					b['clinic'] = {'id':doctor.clinic.id, 
									'name':doctor.clinic.clinicName,
									'contact': doctor.clinic.contact,
									'location': {'city': loc.city,
												'country': loc.country,
												'address': loc.address,
												'postalCode': loc.postalCode}}
				result.append(b)
			return JsonResponse(json.dumps({'doctors': result}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)

@csrf_exempt
def list_doctor_appointment(request):
	if request.method == "POST":
		try:
			doctor = Doctor.objects.prefetch_related('doctor_appointments', 'doctor_appointments__patient').get(account=request.user)
			now = pytz.utc.localize(datetime.datetime.now())
			appointments = doctor.doctor_appointments.filter(time_appointed__gte=now)
			result = []
			for appointment in appointments.all():
				result.append({'id':appointment.id, 
							   'patient': { 'id': appointment.patient.id,
											'name': appointment.patient.account.username,
											'type': appointment.patient.type,
											'email': appointment.patient.account.email,
											'phone': appointment.patient.phoneNumber},
								'note': appointment.patientnote,
								'date': str(appointment.time_appointed.date()),
								'time': str(appointment.time_appointed.time())
							})
						
			return JsonResponse(json.dumps({'appointments': result}), safe=False)
		except:
			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)
	
@csrf_exempt
def check_appointment(request):
	if request.method == "POST" and "time" in request.POST:
# 		try:
		doctor = Doctor.objects.prefetch_related('doctor_appointments', 'doctor_appointments__patient').get(account=request.user)
		time = pytz.utc.localize(datetime.datetime.fromtimestamp(int(request.POST['time'])/1000))
		appointments = doctor.doctor_appointments.filter(Q(time_created__gte=time) | Q(time_modified__gte=time))
		result = []
		for appointment in appointments.all():
			result.append({'id':appointment.id, 
						   'patient': { 'id': appointment.patient.id,
										'name': appointment.patient.account.username,
										'type': appointment.patient.type,
										'email': appointment.patient.account.email,
										'phone': appointment.patient.phoneNumber},
							'note': appointment.patientnote,
							'date': str(appointment.time_appointed.date()),
							'time': str(appointment.time_appointed.time())
						})
		return JsonResponse(json.dumps({'appointments': result}), safe=False)
		# except:
# 			pass
	return JsonResponse(json.dumps({'error':'error'}), safe=False)