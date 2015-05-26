from django import template

register = template.Library()

@register.filter(name='doctor_type')
def doctor_type(value):
	if value == 0:
		return "General"
	elif value == 1:
		return "Dental"
	elif value == 2:
		return "Ear, Nose and Throat"
	elif value == 3:
		return "Women Health"
	return "Unknown: %s" % value
	
@register.filter(name='format_date')
def format_date(value):
	return "%02d/%02d/%04d" % (value.day, value.month, value.year)
	
@register.filter(name='split')
def split(str, splitter):
	return str.split(splitter)