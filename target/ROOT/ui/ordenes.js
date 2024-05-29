let app = angular.module('iw3', ['ngStomp'])


	.constant('URL_API_BASE', 'http://localhost:8080/api/final/')
	.constant('URL_BASE', 'http://localhost:8080/')
	.constant('URL_WS', '/api/final/ws')


app.controller('verOrdenes', function($scope, $rootScope, $stomp, wsService, $http) {

	$rootScope.stomp = $stomp;

	$scope.Orden = {};
	$scope.Ordenes = [];
	$scope.Data = [];
	$scope.Filtro = { valor: '' };


	//Verifico si el usuario está logueado, y si no está logueado lo redirecciono a la página de login
	if (localStorage.getItem("logged") != "true")
		window.location.replace("/login.html");


	//Obtengo el token del usuario
	var token = localStorage.getItem("token");
	
	var req = {
			method: 'GET',
			url: 'http://localhost:8080/api/final/ordenes',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			},
		};
		
	$http(req).then(
			function(resp) {
				if (resp.status === 200) {
					$scope.Ordenes = resp.data;
					$scope.total = $scope.Ordenes.length;

					console.log($scope.Ordenes.lenght);
				} else {
					console.log(req);
					alert("No se pueden obtener las ordenes");
				}
			},
			function(respErr) {

				alert("No se pueden obtener las ordenes");
			}
		);

	$scope.cerrarSesion = function() {
		localStorage.setItem("logged", "false");
		localStorage.setItem("token", "");
		window.location.replace("/login.html");

	}

	$scope.inicio = function() {
		window.location.replace("/index.html");

	}
	
	//Inicio el Web Socket para cargar
	wsService.initStompClient('/iw3/data', function(payload,
		headers, res) {

		if (res != null) {

			$scope.Ejecutar(req);

		}
	}, $scope.stomp);


	$scope.Ejecutar = function(req) {

		$http(req).then(
			function(resp) {
				if (resp.status === 200) {
					$scope.Ordenes = resp.data;
					$scope.total = $scope.Ordenes.length;

					console.log($scope.Ordenes.lenght);
				} else {
					console.log(req);
					alert("No se pueden obtener las ordenes");
				}
			},
			function(respErr) {

				alert("No se pueden obtener las ordenes");
			}
		);
	}


	$scope.ordenarporOrdenAscendente = function() {
		console.log($scope.Ordenes);
		$scope.Ordenes.sort(function(a, b) { return (a.numeroOrden - b.numeroOrden); });

	}
	$scope.ordenarporOrdenDescendente = function() {
		console.log($scope.Ordenes);
		$scope.Ordenes.sort(function(a, b) { return (b.numeroOrden - a.numeroOrden); });

	}

	$scope.ordenarporEstadoAscendente = function() {
		console.log($scope.Ordenes);
		$scope.Ordenes.sort(function(a, b) { return (a.estado - b.estado); });

	}
	$scope.ordenarporEstadoDescendiente = function() {
		console.log($scope.Ordenes);
		$scope.Ordenes.sort(function(a, b) { return (b.estado - a.estado); });

	}


	$scope.verConciliacion = function(i) {

		if ($scope.pesajeInicial = $scope.Ordenes[i].estado != "4") {
			$('#dialogo1').modal('hide');
			alert("Solo se puede ver la conciliacion para ordenes en estado 4");
		}
		else {
			$('#dialogo1').modal('show');
			$scope.pesajeInicial = $scope.Ordenes[i].pesajeInicial;
			$scope.pesajeFinal = $scope.Ordenes[i].pesajeFinal;
			$scope.masa = $scope.Ordenes[i].ultimaMasaAcumulada;
			$scope.masa = $scope.Ordenes[i].ultimaMasaAcumulada;
			$scope.masa = $scope.Ordenes[i].ultimaMasaAcumulada;
			$scope.promedioTemp = $scope.Ordenes[i].promedioTemperatura;
			$scope.promedioCaudal = $scope.Ordenes[i].promedioCaudal;
		}


	}
});

//Módulo encargado de gestionar el Web Socket
app.factory('wsService',
	function($rootScope, URL_WS) {

		var fnConfig = function(stomp, topic, cb) {
			stomp.subscribe(topic, function(payload, headers, res) {
				cb(payload, headers, res);
			});
		};
		return {
			initStompClient: function(topic, cb, stomp) {


				stomp.setDebug(function(args) {

					if (stomp.sock.readyState > 1) {
						fnConnect();
					}
				});
				var fnConnect = function() {

					if (localStorage.getItem("logged") == "true") {
						stomp.connect(URL_WS + "?xauthtoken=" + localStorage.getItem("token")).then(function(frame) {
							fnConfig(stomp, topic, cb);
						});
					} else {
						console.log("No existen credenciales para presentar en WS")
					}
				};
				fnConnect();
			},
			stopStompClient: function() {
				if (stomp)
					stomp.disconnect();
			}
		}

	});
