from django.contrib.auth.models import AnonymousUser
from django.contrib.auth import authenticate, login
class GetUser(object):
	def process_request(self, request):
		try:
			if isinstance(request.user, AnonymousUser):
				dic = {}
				if request.method == "POST":
					dic = request.POST
				elif request.method == "GET":
					dic = request.GET
				if dic and "username" in dic and "password" in dic:
					username = dic["username"]
					password = dic["password"]
					user = authenticate(username=username, password=password)
					request.user = user
		except:
			pass
# 				if user:
# 					login(request, user)
# 		if request.user is 