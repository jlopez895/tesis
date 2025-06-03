let app = angular.module('iw3', ['ngStomp'])


	.constant('URL_API_BASE', 'http://iuatesis.chickenkiller.com/api/final/')
	.constant('URL_BASE', 'http://iuatesis.chickenkiller.com/')
	.constant('URL_WS', '/api/final/ws');

$(window).on('load', function () {
	setTimeout(function () {
		$(".loader-page").css({ visibility: "hidden", opacity: "0" })
	}, 2000);

});



app.controller('controllerPedidos', function ($scope, $filter, $http, $rootScope, $stomp, $window, URL_WS) {
	// Verifica si el usuario está logueado

	if (localStorage.getItem("logged") != "true")
		window.location.replace("/login.html");

	$rootScope.stomp = $stomp;
	let userDataFromLocalStorage = JSON.parse(localStorage.getItem("ngStorage-userdata"));
	let fullName = userDataFromLocalStorage.fullname;
	let initials = fullName.split(',')
		.map(word => word.charAt(0))
		.join('');

	$scope.userData = {
		iniciales: initials,
		fullname: fullName,
		email: userDataFromLocalStorage.email,
		rol: userDataFromLocalStorage.rolPrinc,
		rolDesc: userDataFromLocalStorage.rolPrincDesc  // Puedes obtener estos valores desde localStorage o cualquier otra fuente
	};


	$scope.cerrarSesion = function () {

		localStorage.setItem("logged", "false");
		localStorage.setItem("token", "");
		window.location.replace("/login.html");
	};
	
	$scope.init = function () {
		
		var reqNotificaciones = {
			method: 'GET',
			url: 'http://iuatesis.chickenkiller.com/api/final/documentos/estadisticasPorRol',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
		};

		$scope.Estadisticas = {};
		$scope.cargarDocumentosPorRol = function () {

			$http(reqNotificaciones).then(
				function (resp) {
					if (resp.status === 200) {
						$scope.Estadisticas = resp.data;

						// Actualizar los datos del gráfico
						myChart.data.labels = Object.keys($scope.Estadisticas);
						myChart.data.datasets[0].data = Object.values($scope.Estadisticas);
						myChart.update();

					} else {
						alert("No se pueden obtener las estadisticas");
					}
				},
				function (respErr) {
					alert("No se pueden obtener las estadisticas");
				}
			);
		}

		var ctx = document.getElementById('myChart').getContext('2d');
		var myChart = new Chart(ctx, {
			type: 'bar',
			data: {
				labels: [], // inicialmente vacío
				datasets: [{
					label: 'Documentos generados por rol',
					data: [], // inicialmente vacío
					backgroundColor: [
						'rgba(255, 99, 132, 0.2)',
						'rgba(54, 162, 235, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					y: {
						beginAtZero: true,
						ticks: {
							stepSize: 1
						}
					}
				}
			}
		});

		// Llamar a cargarNotificaciones
		$scope.cargarDocumentosPorRol();
	};

	//documentos por destinatario
	$scope.init2 = function () {
		var reqNotificaciones = {
			method: 'GET',
			url: 'http://iuatesis.chickenkiller.com/api/final/documentos/estadisticasPorMinisterio',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
		};

		$scope.Estadisticas2 = {};
		$scope.cargarDocumentosPorMinisterio = function () {

			$http(reqNotificaciones).then(
				function (resp) {
					if (resp.status === 200) {
						$scope.Estadisticas2 = resp.data;

						// Actualizar los datos del gráfico
						myChart.data.labels = Object.keys($scope.Estadisticas2);
						myChart.data.datasets[0].data = Object.values($scope.Estadisticas2);
						myChart.update();

					} else {
						alert("No se pueden obtener las estadisticas");
					}
				},
				function (respErr) {
					alert("No se pueden obtener las estadisticas");
				}
			);
		}

		var ctx = document.getElementById('myChart2').getContext('2d');
		var myChart = new Chart(ctx, {
			type: 'bar',
			data: {
				labels: [], // inicialmente vacío
				datasets: [{
					label: 'Documentos generados por destinatario',
					data: [], // inicialmente vacío
					backgroundColor: [
						'rgba(255, 99, 132, 0.2)',
						'rgba(54, 162, 235, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					y: {
						beginAtZero: true,
						ticks: {
							stepSize: 1
						}
					}
				}
			}
		});

		// Llamar a cargarNotificaciones
		$scope.cargarDocumentosPorMinisterio();
	};


	//tiempos por estimulo
	$scope.init3 = function () {
		var reqNotificaciones = {
			method: 'GET',
			url: 'http://iuatesis.chickenkiller.com/api/final/estimulos/estadisticasPorEstimulo',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
		};

		$scope.Estadisticas2 = {};
		$scope.cargarestadisticasPorEstimulo = function () {

			$http(reqNotificaciones).then(
				function (resp) {
					if (resp.status === 200) {
						$scope.Estadisticas3 = resp.data;

						// Actualizar los datos del gráfico
						myChart.data.labels = Object.keys($scope.Estadisticas3);
						myChart.data.datasets[0].data = Object.values($scope.Estadisticas3);
						myChart.update();

					} else {
						alert("No se pueden obtener las estadisticas");
					}
				},
				function (respErr) {
					alert("No se pueden obtener las estadisticas");
				}
			);
		}

		var ctx = document.getElementById('myChart3').getContext('2d');
		var myChart = new Chart(ctx, {
			type: 'pie', // Cambiado de 'bar' a 'pie'
			data: {
				labels: [], // inicialmente vacío
				datasets: [{
					label: 'Total',
					data: [], // inicialmente vacío
					backgroundColor: [
						'rgba(255, 99, 132, 0.2)',
						'rgba(54, 162, 235, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)'
					],
					borderWidth: 1
				}]
			},
			options: {
				responsive: true, // Asegura que el gráfico se redimensione correctamente en diferentes tamaños de pantalla
				plugins: {
					title: {
						display: true,
						text: [
							'Estimulos con tiempo total > estimado',
							'vs',
							'Estimulos con tiempo total < estimado'
						] // Título con saltos de línea
					}
				}
			}
		});

		// Llamar a cargarNotificaciones
		$scope.cargarestadisticasPorEstimulo();
	};
	// Ejecutar la función cargarNotificaciones cada 5 segundos
	const intervalo = 5000; // 5 segundos (5000 ms)
	setInterval(function () {
		$scope.cargarDocumentosPorRol();
		$scope.cargarDocumentosPorMinisterio();
		$scope.cargarestadisticasPorEstimulo();
	}, intervalo);

});
