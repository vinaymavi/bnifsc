from django.conf import settings
from django.conf.urls import include, url
from django.conf.urls.static import static
from django.contrib.staticfiles.views import serve

import session_csrf

session_csrf.monkeypatch()

from django.contrib import admin

admin.autodiscover()

import webapp.views as webapp
from webapp.api_views import ApiInfo, BankApi, StateApi, BankDetailApi,DistrictApi,CityApi,BranchApi

urlpatterns = (
    
    # Static Urls
    url(r'^$', webapp.index, name='home'),
    url(r'^aboutus$', webapp.aboutus, name='aboutus'),
    url(r'^contactus$', webapp.contactus, name='contactus'),
    url(r'^privacy$', webapp.privacy, name='privacy'),
    url(r'^disclaimer$', webapp.disclaimer, name='disclaimer'),
    url(r'^search-by-ifsc$', webapp.by_ifsc, name='by_ifsc'),
    url(r'^validate-ifsc$', webapp.validate_ifsc, name='validate_ifsc'),
    
    # API Urls
    url(r'^api/bank$', BankApi.as_view(), name="api_bank"),
    url(r'^api/state$', StateApi.as_view(), name="api_state"),
    url(r'^api/district$', DistrictApi.as_view(), name="api_district"),
    url(r'^api/city$', CityApi.as_view(), name="api_city"),
    url(r'^api/branch$', BranchApi.as_view(), name="api_branch"),
    url(r'^api/bank-detail$', BankDetailApi.as_view(), name="api_bank_detail"),
    url(r'^api/', ApiInfo.as_view(), name="api_home"),
    url(r'^_ah/', include('djangae.urls')),    
    
    # Dymamic Urls
    url(r'^([\w-]+)/(\d+)/$',webapp.bank, name="bank_page"),
    url(r'^([\w-]+)/(\d+)-(\d+)/$',webapp.state, name="state_page"),
    url(r'^([\w-]+)/(\d+)-(\d+)-(\d+)/$',webapp.district, name="district_page"),
    url(r'^([\w-]+)/(\d+)-(\d+)-(\d+)-(\d+)/$',webapp.city, name="city_page"),
    url(r'^([\w-]+)/(\d+)-(\d+)-(\d+)-(\d+)-(\d+)/$',webapp.branch, name="branch_page"),
    url(r'^([\w-]+)/(\w+)/$',webapp.ifsc_code_info, name="ifsc_code_info"),

    # Note that by default this is also locked down with login:admin in app.yaml
    url(r'^admin/', include(admin.site.urls)),

    url(r'^csp/', include('cspreports.urls')),

    url(r'^auth/', include('djangae.contrib.gauth.urls')),
)

if settings.DEBUG:
    urlpatterns += tuple(static(settings.STATIC_URL, view=serve, show_indexes=True))
