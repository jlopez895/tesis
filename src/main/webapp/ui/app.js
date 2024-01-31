let app = angular.module('iw3', ['ngStomp'])


	.constant('URL_API_BASE', 'http://localhost:8080/api/final/')
	.constant('URL_BASE', 'http://localhost:8080/')
	.constant('URL_WS', '/api/final/ws')
var inicio = 0;
var timeout = 0;
app.controller('controllerPedidos', function($scope, $rootScope,wsService, $stomp, $http) {

	$rootScope.stomp = $stomp;

	//Verifico si el usuario está logueado, y si no está logueado lo redirecciono a la página de login
	if (localStorage.getItem("logged") != "true")
		window.location.replace("/login.html");

	
	var orden, camion, chofer, cliente, producto, mes, dia, anio, hora, min, preset, bttn1, bttn2, cerrarOrden, pesajeInicial, password,
		masa, densidad, temp, caudal, bttn3, bttn4, pesajeFinal, conciliacion, groupSelect, progress, ok;
	//Obtengo el token del usuario


	var token = localStorage.getItem("token");
	var fecha = new Date();

	$scope.estado = 0;
	$scope.progreso = '0%'
	$scope.eta = 0;
	$scope.promedioTemp = 0;
	$scope.promedioCaudal = 0;

	$scope.cerrarSesion = function() {
		localStorage.setItem("logged", "false");
		localStorage.setItem("token", "");
		window.location.replace("/login.html");
	}

	$scope.verOrdenes = function() {

		window.location.replace("/ordenes.html");
	}

	//Inicio el Web Socket para cargar
	wsService.initStompClient('/iw3/data', function(payload,
		headers, res) {

		let resSplit = res.toString().split("\n");
		let respuesta = resSplit[resSplit.length - 1];

		//Si incluye "TYPE=" es porque es una alarma

		if (respuesta.includes("TYPE=")) {
			let tipo = respuesta.split("TYPE=")[1];
			respuesta = respuesta.split("TYPE=")[0];
			$scope.nroOrden = respuesta.split("orden ")[1].split(" ")[0];
			$scope.motivoAlarma = respuesta;
			let titulo = "";
			let logoAlarma = "error";

			if (tipo == "excesoTemp")
				titulo = "Exceso de Temperatura detectado"


			//Muestro la alarma 
			swal({
				title: titulo,
				text: respuesta,
				type: logoAlarma,
				showCancelButton: false,
				confirmButtonColor: "#FF0000",
				confirmButtonText: "Aceptar alarma",
				closeOnConfirm: true
			},
				function() {
					$scope.aceptarAlarma();
				});
		}
	}, $scope.stomp);

	$scope.aceptarAlarma = function() {

		
		var req = {
			method: 'POST',
			url: 'http://localhost:8080/api/final/alarmas',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			},
			data: {
				'motivoAlarma': $scope.motivoAlarma,
				'orden':{
					'numeroOrden':$scope.orden
				},
				'usuarioQueAcepto':{
					'id': 1,
				}
			
			}


		};
		$http(req).then(
			function(resp) {
				if (resp.status === 201) {
					
				} else {
					console.log(resp.status);
					alert("Error");
					ok = false;
				}
			},
			function(respErr) {

				console.log(respErr.status);
				alert("Error");
				ok = false;
			}
		);
	}
	
	$scope.cambiarEstado1 = function() {

		orden = document.getElementById('orden');
		patente = document.getElementById('patente');
		cisternado1 = document.getElementById('cist1');
		cisternado2 = document.getElementById('cist2');
		chofer = document.getElementById('dniChofer');
		cliente = document.getElementById('cliente');
		producto = document.getElementById('producto');
		mes = document.getElementById('mes');
		dia = document.getElementById('dia');
		anio = document.getElementById('anio');
		hora = document.getElementById('hora');
		min = document.getElementById('min');
		preset = document.getElementById('preset');
		codExternoCamion = document.getElementById('codExternoCamion');
		codExternoChofer = document.getElementById('codExternoChofer');
		codExternoCliente = document.getElementById('codExternoCliente');
		codExternoProducto = document.getElementById('codExternoProducto');


		if (validar(orden.value) && validar(chofer.value) && validar(preset.value) && validar(dia.value)
			&& validar(mes.value) && validar(anio.value) && validar(hora.value) && validar(min.value) && patente.value != "" &&
			validar(cisternado1.value) && validar(cisternado2.value) && validar(chofer.value) && cliente.value != "" && producto.value != "" && codExternoProducto.value != ""
			&& codExternoCliente.value != "" && codExternoChofer.value != "" && codExternoCamion.value != "") {
			if (validarFecha(dia.value, mes.value, anio.value, hora.value, min.value)) {

				orden.disabled = true;
				codExternoCamion.disabled = true;
				patente.disabled = true;
				cisternado2.disabled = true;
				cisternado1.disabled = true;
				codExternoChofer.disabled = true;
				codExternoCliente.disabled = true;
				codExternoProducto.disabled = true;
				chofer.disabled = true;
				cliente.disabled = true;
				producto.disabled = true;
				preset.disabled = true;
				dia.disabled = true;
				mes.disabled = true;
				anio.disabled = true;
				hora.disabled = true;
				min.disabled = true;
				bttn1 = document.getElementById('estado1');
				bttn1.disabled = true;
				pesajeInicial = document.getElementById('pesajeInicial');
				pesajeInicial.disabled = false;
				password = document.getElementById('password');
				password.disabled = false;
				bttn2 = document.getElementById('estado2');
				bttn2.disabled = false;
				groupSelect = document.getElementById('inputGroupSelect01');
				groupSelect.disabled = false;
				$scope.estado = 1;
				$scope.progreso = '25%'
				$('#bar').css('width', 25 + '%');

				var h = parseInt(hora.value) + 3;
				var fecha = anio.value + "-" + mes.value + "-" + dia.value + "T" + h + ":" + min.value + ":00Z";
				console.log(fecha);
				var data = {
					'numeroOrden': parseInt(orden.value),
					'turno': fecha,
					'chofer': {
						'codigoexterno': codExternoChofer.value,
						'dni': parseInt(chofer.value)
					},
					'camion': {
						'codigoexterno': codExternoCamion.value,
						'patente': patente.value,
						'cisternado': [parseInt(cisternado1.value), parseInt(cisternado2.value)]
					},
					'cliente': {
						'codigoexterno': codExternoCliente.value,
						'razonSocial': cliente.value
					},
					'producto': {
						'codigoexterno': codExternoProducto.value,
						'nombre': producto.value
					},
					'preset': parseInt(preset.value)
				};

				var req = {
					method: 'POST',
					url: 'http://localhost:8080/api/final/ordenes/ingresoOrden',
					headers: {
						'Content-Type': 'application/json',
						'xauthtoken': token
					},
					data: data


				};

				$scope.Ejecutar(req);


			}
			else
				window.alert("Los datos ingresados no son correctos");

		}
		else
			window.alert("Los datos ingresados no son correctos");


	}

	$scope.cambiarEstado2 = function() {

		pesajeInicial = document.getElementById('pesajeInicial');
		password = document.getElementById('password');
		frecuencia = document.getElementById('inputGroupSelect01');
		if (validar(pesajeInicial.value) && validar(password.value) && frecuencia.value != "") {

			$scope.estado = 2;
			$scope.progreso = '50%'
			$('#bar').css('width', 50 + '%');

			pesajeInicial.disabled = true;
			password.disabled = true;
			bttn2 = document.getElementById('estado2');
			bttn2.disabled = true;
			groupSelect = document.getElementById('inputGroupSelect01');
			groupSelect.disabled = true;

			masa = document.getElementById('masa');

			masa.disabled = false;
			densidad = document.getElementById('densidad');
			densidad.disabled = false;
			temp = document.getElementById('temp');
			temp.disabled = false;
			caudal = document.getElementById('caudal');
			caudal.disabled = false;
			bttn3 = document.getElementById('estado3');
			bttn3.disabled = false;


			var data = {
				'pesajeInicial': parseInt(pesajeInicial.value),
				'password': parseInt(password.value),
				'frecuenciaAlmacenamiento': parseInt(frecuencia.value)

			};

			var req = {
				method: 'POST',
				url: 'http://localhost:8080/api/final/ordenes/pesajeInicial/' + orden.value,
				headers: {
					'Content-Type': 'application/json',
					'xauthtoken': token
				},
				data: data


			};

			$scope.Ejecutar(req);


			inicio = new Date().getTime();
			funcionando();





		}
		else
			window.alert("Los datos ingresados no son correctos");

	}
	$scope.cargarCamion = function() {
		masa = document.getElementById('masa');
		densidad = document.getElementById('densidad');
		temp = document.getElementById('temp');
		caudal = document.getElementById('caudal');
		preset = document.getElementById('preset');
		if (validar(masa.value) && validar(densidad.value) && validar(temp.value) && validar(caudal.value)
			&& parseFloat(masa.value) <= parseFloat(preset.value) && password.value.length == 5) {


			$('#bar').css('width', 75 + '%');
			$scope.progreso = '75%'
			$scope.estado = 3;

			//masa.disabled = true;
			//densidad.disabled = true;
			//temp.disabled = true;
			//caudal.disabled = true;
			bttn3 = document.getElementById('estado3');
			//bttn3.disabled = true;

			pesajeFinal = document.getElementById('pesajeFinal');
			pesajeFinal.disabled = false;
			bttn4 = document.getElementById('estado4');
			bttn4.disabled = false;
			var data = {
				"caudal": parseFloat(caudal.value),
				"masaAcumulada": parseFloat(masa.value),
				"densidad": parseFloat(densidad.value),
				"temperatura": parseFloat(temp.value)

			};
			console.log();
			var req = {
				method: 'POST',
				url: 'http://localhost:8080/api/final/detallesOrdenes/cargarCamion/' + orden.value,
				headers: {
					'Content-Type': 'application/json',
					'xauthtoken': token
				},
				data: data


			};
			$scope.Ejecutar(req);
			$scope.eta = (parseInt(preset.value) - parseInt(masa.value)) / parseInt(caudal.value);

			if (parseFloat(masa.value) == parseFloat(preset.value)) {

				btnCerrar = document.getElementById('btnCerrar');
				btnCerrar.style.display = 'inline';
				bttn3.disabled = true;

			}

		}
		else
			window.alert("Los datos ingresados no son correctos");



	}

	$scope.cerrarOrden = function() {

		var req2 = {
			method: 'PUT',
			url: 'http://localhost:8080/api/final/ordenes/cerrarOrden/' + orden.value,
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			}

		};
		$scope.Ejecutar(req2);

		masa = document.getElementById('masa');
		masa.disabled = true;
		densidad = document.getElementById('densidad');
		densidad.disabled = true;
		temp = document.getElementById('temp');
		temp.disabled = true;
		caudal = document.getElementById('caudal');
		caudal.disabled = true;
		btnCerrar = document.getElementById('btnCerrar');
		btnCerrar.disabled = true;

		clearTimeout(timeout);
		timeout = 0;
	}

	$scope.cambiarEstado4 = function() {

		pesajeFinal = document.getElementById('pesajeFinal');


		if (validar(pesajeFinal.value)) {

			$scope.estado = 4;
			$('#bar').css('width', 100 + '%');
			$scope.progreso = '100%'
			conciliacion = document.getElementById('conciliacion');
			conciliacion.style.display = 'inline';
			pesajeFinal.disabled = true;
			bttn4 = document.getElementById('estado4');
			bttn4.disabled = true;

			var data = {
				"pesajeFinal": parseFloat(pesajeFinal.value)
			};

			var req = {
				method: 'POST',
				url: 'http://localhost:8080/api/final/ordenes/pesajeFinal/' + orden.value,
				headers: {
					'Content-Type': 'application/json',
					'xauthtoken': token
				},
				data: data


			};

			$scope.Ejecutar(req);






		}
		else
			window.alert("Los datos ingresados no son correctos");

	}

	$scope.verConciliacion = function() {
		var Orden = {};
		var req2 = {
			method: 'GET',
			url: 'http://localhost:8080/api/final/ordenes/' + orden.value,
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': token
			},
		};

		$http(req2).then(
			function(resp) {
				if (resp.status === 200) {
					Orden = resp.data;
					console.log(Orden);
					$scope.promedioTemp = Orden.promedioTemperatura;
					$scope.promedioCaudal = Orden.promedioCaudal;
					$('#dialogo1').modal('show');

				} else {
					console.log(req);
					alert("No se pudo obtener la orden");
				}
			},
			function(respErr) {

				alert("No se pudo obtener la orden");
			}
		);
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

function funcionando() {
	// obteneos la fecha actual
	var actual = new Date().getTime();

	// obtenemos la diferencia entre la fecha actual y la de inicio
	var diff = new Date(actual - inicio);

	// mostramos la diferencia entre la fecha actual y la inicial
	var result = LeadingZero(diff.getUTCHours()) + ":" + LeadingZero(diff.getUTCMinutes()) + ":" + LeadingZero(diff.getUTCSeconds());
	document.getElementById('crono').innerHTML = result;

	// Indicamos que se ejecute esta función nuevamente dentro de 1 segundo
	timeout = setTimeout("funcionando()", 1000);
}

/* Funcion que pone un 0 delante de un valor si es necesario */
function LeadingZero(Time) {
	return (Time < 10) ? "0" + Time : + Time;
}

function validar(num) {

	if (isNaN(num) || num < 0) {

		return false;

	}
	if (num.length == 0 || /^\s+$/.test(num)) {
		return false;
	}

	return true;
}
function validarFecha(dia, mes, anio, hora, min) {
	var date = new Date();

	if ((mes < 1 || mes > 12) || (dia < 1 || dia > 31) || (hora < 8 || hora > 20) || (min < 0 || min > 59))
		return false;

	if (anio < date.getFullYear())
		return false;

	if (anio == date.getFullYear() && mes < ((date.getMonth()) + 1))
		return false;

	if (anio == date.getFullYear() && mes == ((date.getMonth()) + 1) && dia < date.getDate())
		return false;

	if (anio == date.getFullYear() && mes == ((date.getMonth()) + 1) && dia == date.getDate() && hora < date.getHours())
		return false;

	return true;

}