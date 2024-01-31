angular.module('iw3').config(
		function($routeProvider, $locationProvider, 
		$httpProvider, $logProvider, $localStorageProvider){
			 
			$localStorageProvider.setKeyPrefix('iw3/');
			
			$logProvider.debugEnabled(true);
			
			//$httpProvider.defaults.withCredentials = true;

			$httpProvider.interceptors.push('APIInterceptor');
			
			$locationProvider.hashPrefix('!');
			$routeProvider
				.when('/main', {
					templateUrl : 'ui/views/main.html',
					controller : 'Main'
				})
				.when('/productos', {
					templateUrl : 'ui/modulos/productos/productos.html',
					controller : 'ProductosController'
				})
				.when('/graficos', {
					templateUrl : 'ui/modulos/graficos/graficos.html',
					controller : 'GraficosController'
				})
				.otherwise({
					redirectTo : '/main'
				});
				
		});
