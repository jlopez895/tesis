let app = angular.module('iw3', []);

$(window).on('load', function () {
	setTimeout(function () {
		$(".loader-page").css({ visibility: "hidden", opacity: "0" })
	}, 2000);

});

// Función para llenar un textarea con el contenido de un archivo de texto
function llenarTextareaDesdeArchivo(rutaArchivo) {
	$.get(rutaArchivo, function (data) {
		// Asigna el contenido del archivo al textarea
		$('#descripcionProblematica').val(data);
	});
}

// Llama a la función para llenar el textarea con el contenido del archivo
llenarTextareaDesdeArchivo('random.txt');

function cerrarModalNuevoDocumento() {
	$("#modalNuevoDocumento").modal('hide');
}
function cerrarModalNuevoEstimulo() {
	$("#modalNuevoEstimulo").modal('hide');
}

function cerrarModalNotif() {
	$("#modalNotif").modal('hide');
}

function cerrarModaEstimulos() {
	$("#modalEstimulos").modal('hide');
}

function cerrarModalNuevoEstimulo() {
	$("#modalNuevoEstimulo").modal('hide');
}

function cerrarModalProb() {
	$("#modalProb").modal('hide');
}

app.controller('controllerPedidos', function ($scope, $http) {
	if (localStorage.getItem("logged") != "true")
		window.location.replace("/login.html");

	let userDataFromLocalStorage = JSON.parse(localStorage.getItem("ngStorage-userdata"));
	let fullName = userDataFromLocalStorage.fullname;
	let initials = fullName.split(',')
		.map(word => word.charAt(0))
		.join('');

	$scope.userData = {
		iniciales: initials,
		fullname: fullName,
		email: userDataFromLocalStorage.email,
		rol: "Administrador"  // Puedes obtener estos valores desde localStorage o cualquier otra fuente
	};

	$scope.cerrarSesion = function () {
		console.log("Función cerrarSesion llamada");
		localStorage.setItem("logged", "false");
		localStorage.setItem("token", "");
		window.location.replace("/login.html");
	};

	var reqEstimulos = {
		method: 'GET',
		url: 'http://localhost:8080/api/final/estimulos/list',
		headers: {
			'Content-Type': 'application/json',
			'xauthtoken': userDataFromLocalStorage.authtoken
		},
	};

	$scope.selectedEstimulo = {};
	$scope.Estimulos = [];
	$scope.Data = [];
	$scope.FiltroEstimulos = { valor: '' };

	$http(reqEstimulos).then(
		function (resp) {
			if (resp.status === 200) {
				$scope.Estimulos = resp.data;
				$scope.totalEstimulos = $scope.Estimulos.length;

			} else {
				console.log(reqEstimulos);
				alert("No se pueden obtener los estimulos");
			}
		},
		function (respErr) {

			alert("No se pueden obtener los estimulos");
		}
	);

	var reqNotificaciones = {
		method: 'GET',
		url: 'http://localhost:8080/api/final/notificaciones/list/' + userDataFromLocalStorage.rolPrinc,
		headers: {
			'Content-Type': 'application/json',
			'xauthtoken': userDataFromLocalStorage.authtoken
		},
	};

	$scope.Notificacion = {};
	$scope.Notificaciones = [];
	$scope.FiltroNotificaciones = { valor: '' };


	$http(reqNotificaciones).then(
		function (resp) {
			if (resp.status === 200) {
				$scope.Notificaciones = resp.data;
				$scope.totalNotificaciones = $scope.Notificaciones.length;

			} else {
				console.log(reqNotificaciones);
				alert("No se pueden obtener las notificaciones");
			}
		},
		function (respErr) {

			alert("No se pueden obtener las notificaciones");
		}
	);

	$scope.mostrarDiv = true;

	function limpiarCampos() {
		document.getElementById('tituloEstimulo').value = '';
		document.getElementById('descripcionEstimulo').value = '';
		document.getElementById('fechaEstFinEstimulo').value = '';
		document.getElementById('horaEstFinEstimulo').value = '';
	}

	$('#modalNuevoEstimulo').on('shown.bs.modal', function () {
		limpiarCampos(); // Limpia los campos cada vez que se abre el modal
	});

	var tituloEstimulo, descripcionEstimulo, fechaEstFinEstimulo, horaEstFinEstimulo;
	$scope.nuevoEst = function () {

		tituloEstimulo = document.getElementById('tituloEstimulo').value;
		descripcionEstimulo = document.getElementById('descripcionEstimulo').value;
		fechaEstFinEstimulo = document.getElementById('fechaEstFinEstimulo').value;
		horaEstFinEstimulo = document.getElementById('horaEstFinEstimulo').value;

		//FechaHoraInicio
		let fechaHoraActual = new Date();
		let fechaHoraActualSQL = fechaHoraActual.toISOString();

		//FechaHoraFin
		let fechaPartes = fechaEstFinEstimulo.split('-');
		let horaPartes = horaEstFinEstimulo.split(':');
		let fechaHoraFin = new Date(
			fechaPartes[0],
			fechaPartes[1] - 1,
			fechaPartes[2],
			horaPartes[0],
			horaPartes[1]
		);
		let fechaHoraFinSQL = fechaHoraFin.toISOString();

		if (fechaPartes[0] == fechaHoraActual.getFullYear() &&
			fechaPartes[1] - 1 == fechaHoraActual.getMonth() &&
			fechaPartes[2] == fechaHoraActual.getDate()) {

			if (horaPartes[0] < fechaHoraActual.getHours() ||
				(horaPartes[0] == fechaHoraActual.getHours() && horaPartes[1] <= fechaHoraActual.getMinutes())) {

				swal("Error", "La hora estimada de finalización no puede ser anterior a la hora actual.", "error");
				return;
			}
		}

		//TiempoEstimado
		let diferenciaMilisegundos = fechaHoraFin - fechaHoraActual;
		let diferenciaHoras = diferenciaMilisegundos / (1000 * 60 * 60);
		let tiempoEstimado = parseInt(diferenciaHoras);


		var data = {
			'descripcion': descripcionEstimulo,
			'estado': 1,
			'fechaFin': fechaHoraFinSQL,
			'fechaInicio': fechaHoraActualSQL,
			'tiempoEstmado': tiempoEstimado,
			'titulo': tituloEstimulo,
			'usuarioCreador': userDataFromLocalStorage.idUser
		};

		var req = {
			method: 'POST',
			url: 'http://localhost:8080/api/final/estimulos/nuevoEstimulo',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
			data: data


		};

		$scope.Ejecutar(req).
			then(function (resp) {
				limpiarCampos();
				swal("¡Estímulo registrado exitosamente!", "", "success");
				$('#modalNuevoEstimulo').modal('hide');

			}).catch(function (error) {

				console.error('Error al registrar el estímulo:', error);
				swal("Error", "Hubo un problema al registrar el estímulo.", "error");
			});


	}

	$scope.nuevoDoc = function () {

		tipoDocumento = document.getElementById('tipoDoc').value;
		idEstimulo= $scope.selectedEstimulo.id;
		tituloDocumento = document.getElementById('tituloDocumento').value;
		descripcionDocumento = document.getElementById('cuerpo').value;
		if($scope.mostrarDiv == true&&document.getElementById('final').checked)
			esFinalDocumento = true;
		else
			esFinalDocumento = false;
	
		//FechaHoraInicio
		let fechaHoraActual = new Date();
		
		let fechaDocumentoSQL = fechaHoraActual.toISOString();
	
		var data = {
			'tipo': tipoDocumento,
			'fecha': fechaDocumentoSQL,
			'titulo': tituloDocumento,
			'descripcion': descripcionDocumento,
			'esFinal': esFinalDocumento,
			'estimulo': $scope.selectedEstimulo.id, // replace with the selected estimulo id
			'usuario':userDataFromLocalStorage.idUser,
			'rol': userDataFromLocalStorage.rolPrinc// replace with the selected rol id
		};
	
		var req = {
			method: 'POST',
			url: 'http://localhost:8080/api/final/documentos/nuevoDocumento',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
			data: data
		};
	
		$scope.Ejecutar(req).
			then(function (resp) {
				limpiarCampos();
				swal("¡Documento registrado exitosamente!", "", "success");
				$('#modalNuevoDocumento').modal('hide');
	
			}).catch(function (error) {
	
				console.error('Error al registrar el documento:', error);
				swal("Error", "Hubo un problema al registrar el documento.", "error");
			});
	}

	$scope.Ejecutar = function (req) {
		return $http(req).then(
			function (resp) {
				if (resp.status === 200) {
					return resp.data;
				} else {
					console.log(req);
					return $q.reject("Error en la respuesta");
				}
			},
			function (respErr) {
				console.log(req);
				return $q.reject(respErr);
			}
		);
	}

	$scope.selectedTipoDoc = '';
	$scope.mostrarDiv = false;

	$scope.cambiarVisibilidadDiv = function () {
		if($scope.selectedTipoDoc === "1")
			$scope.mostrarDiv = true;
		else
			$scope.mostrarDiv = false;
	};
});