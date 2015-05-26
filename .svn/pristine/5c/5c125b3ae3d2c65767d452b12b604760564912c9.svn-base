from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
	url(r'^$', 'server2006.views.home', name='home'),
    url(r'^clinic/', include("clinic.urls")),
	url(r'^doctor/', include("doctor.urls")),
    url(r'^admin/', include(admin.site.urls)),
    
    url(r'^login$', 'server2006.views.login_page', name='login'),
    url(r'^login/submit$', 'server2006.views.login'),
    
    url(r'^logout$', 'server2006.views.logout'),
    
    url(r'^signup$', 'patient.views.signup_page'),
    url(r'^signup/submit$', 'patient.views.signup'),

    url(r'^patient/', include('patient.urls')),
    
    url(r'^appointment/', include('appointment.urls')),
    url(r'^api/', include('api.urls')),
)
