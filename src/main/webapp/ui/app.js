
$(window).on('load', function () {
	setTimeout(function () {
		$(".loader-page").css({ visibility: "hidden", opacity: "0" })
	}, 2000);

});

function cerrarModalNuevoDocumento() {
	$("#modalNuevoDocumento").modal('hide');
}
function cerrarModalNuevoEstimulo() {
	$("#modalNuevoEstimulo").modal('hide');
}


let home = angular.module('iw3', ['ngStomp'])


	.constant('URL_API_BASE', 'http://localhost:8080/api/final/')
	.constant('URL_BASE', 'http://localhost:8080/')
	.constant('URL_WS', '/api/final/ws')

home.controller('home', function ($scope, $rootScope, wsService, $stomp, $http) {

	$rootScope.stomp = $stomp;
	$scope.Estimulo = {};
	$scope.Estimulos = [];
	//Verifico si el usuario está logueado, y si no está logueado lo redirecciono a la página de login
	if (localStorage.getItem("logged") != "true")
		window.location.replace("/login.html");




	var token = localStorage.getItem("token");
	var idUser = localStorage.getItem("idUser");
	var fecha = new Date();

	$scope.estado = 0;
	$scope.progreso = '0%'
	$scope.eta = 0;
	$scope.promedioTemp = 0;
	$scope.promedioCaudal = 0;

	$scope.cerrarSesion = function () {
		localStorage.setItem("logged", "false");
		localStorage.setItem("token", "");
		window.location.replace("/login.html");
	}

	$scope.estimulos = function () {



		var req = {
			method: 'GET',
			url: 'http://localhost:8080/api/final/estimulos/list',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			},
		};

		$http(req).then(
			function (resp) {
				if (resp.status === 200) {
					$scope.Estimulos = resp.data;
					$scope.total = $scope.Estimulos.length;


				} else {
					console.log(req);
					alert("No se pueden obtener los stimulos");
				}
			},
			function (respErr) {

				alert("No se pueden obtener los estimulos");
			}
		);
	}

	$scope.nuevoDoc = function () {
		var estimulo, dest, tipo, final, titulo, cuerpo;
		estimulo = document.getElementById('estimulo');
		dest = document.getElementById('destinatario');
		tipo = document.getElementById('tipo');
		final = document.getElementById('final');
		estimulo = document.getElementById('tipo');
		titulo = document.getElementById('titulo');
		cuerpo = document.getElementById('cuerpo');

		var data = {
			'tipo': parseInt(tipo),
			'fecha': new Date(),
			'titulo':titulo,
			'descripcion':descripcion ,
			'esFinal': final,
			
		};

		var req = {
			method: 'POST',
			url: 'http://localhost:8080/api/final/documentos/nuevoDocumento/'+estimulo+"/"+idUser+"/"+dest,
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			},
			data: data


		};

		$scope.Ejecutar(req);

	}

	$scope.nuevoEst = function () {
		var estimulo, dest, tipo, final, titulo, cuerpo;
		estimulo = document.getElementById('estimulo');
		dest = document.getElementById('destinatario');
		tipo = document.getElementById('tipo');
		final = document.getElementById('final');
		estimulo = document.getElementById('tipo');
		titulo = document.getElementById('titulo');
		cuerpo = document.getElementById('cuerpo');

		var data = {
			'tipo': parseInt(tipo),
			'fecha': new Date(),
			'titulo':titulo,
			'descripcion':descripcion ,
			'esFinal': final,
			
		};

		var req = {
			method: 'POST',
			url: 'http://localhost:8080/api/final/documentos/nuevoDocumento/'+estimulo+"/"+idUser+"/"+dest,
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			},
			data: data


		};

		$scope.Ejecutar(req);

	}

	$scope.Ejecutar = function(req) {
		$http(req).then(
			function(resp) {
				if (resp.status === 200) {
					
				} else {
					
					console.log(req);
					alert("Error");
					ok = false;
				}
			},
			function(respErr) {

				console.log(req);
				alert("Error");
				ok = false;
			}
		);

	}

});