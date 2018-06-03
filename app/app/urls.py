from django.conf import settings
from django.conf.urls import include, url
from django.conf.urls.static import static
from django.contrib.staticfiles.views import serve

import session_csrf

session_csrf.monkeypatch()

from django.contrib import admin

admin.autodiscover()

import webapp.views as webapp
from webapp.api_views import ApiInfo, BankApi,StateApi

urlpatterns = (
    url(r'^$', webapp.index, name='home'),
    url(r'^api/bank', BankApi.as_view(), name="api_bank"),
    url(r'^api/state', StateApi.as_view(), name="api_state"),
    url(r'^api/', ApiInfo.as_view(), name="api_home"),
    url(r'^_ah/', include('djangae.urls')),

    # Note that by default this is also locked down with login:admin in app.yaml
    url(r'^admin/', include(admin.site.urls)),

    url(r'^csp/', include('cspreports.urls')),

    url(r'^auth/', include('djangae.contrib.gauth.urls')),
)

if settings.DEBUG:
    urlpatterns += tuple(static(settings.STATIC_URL, view=serve, show_indexes=True))
