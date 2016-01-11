'use strict';

angular.module('bnifscApp')
    .service('admin', function Admin() {
        var CLIENT_ID = "1056077165370-lpjivah5okp45lcelbrgeeg7d4hc16rv.apps.googleusercontent.com";
        var SCOPES = "https://www.googleapis.com/auth/userinfo.email";
        /**
         * Google+ login function.
         * @param cb function
         */
        var self = this;
        self.login = function (cb) {
            gapi.auth.authorize({
                    client_id: CLIENT_ID,
                    scope: SCOPES, immediate: false
                },
                cb)
        };
        return self;
    });
