angular.module('iw3')
	.factory('coreService',
		function($http, $q, URL_API_BASE, URL_BASE,$localStorage) {
			return {
				login: function(user) {
					var req = {
						method: 'POST',
						url: URL_BASE+'login-user',
						headers : { 'Content-Type': 'application/x-www-form-urlencoded' },
						data: 'username='+user.name+'&password='+user.password
					};
					return $http(req);
				},
				authInfo: function() {
					return $http.get(URL_BASE+"auth-info");
				},
				logout: function() {
					$http.get(URL_BASE+"logout-token").then(
						function(data){
							delete $localStorage.userdata;
							$localStorage.logged=false;
							$http.get(URL_BASE+"auth-info");
						},
						function(err){
							delete $localStorage.userdata;
							$localStorage.logged=false;
						}
					);
					
				}

		} 
	}
);