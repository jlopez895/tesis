let app = angular.module('iw3', ['ngStomp'])


	.constant('URL_API_BASE', 'https://iuatesis.chickenkiller.com/api/final/')
	.constant('URL_BASE', 'https://iuatesis.chickenkiller.com/')
	.constant('URL_WS', '/api/final/ws');

$(window).on('load', function () {
	setTimeout(function () {
		$(".loader-page").css({ visibility: "hidden", opacity: "0" })
	}, 2000);

});

// Función para llenar un textarea con el contenido de un archivo de texto
fetch('ui/random.txt')
	.then(response => response.text())
	.then(data => {
		document.getElementById('descripcionProblematica').value = data;
	});
function llenarTextareaDesdeArchivo(rutaArchivo) {
	$.get(rutaArchivo, function (data) {
		// Asigna el contenido del archivo al textarea
		$('#descripcionProblematica').val(data);
	});
}

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

function cerrarModalNoticias() {
	$("#modalNoticias").modal('hide');
}

function cerrarModalNuevaNoticia() {
	$("#modalNuevaNoticia").modal('hide');
	$("#modalNoticias").modal('show');
}


function cerrarModalNuevoEstimulo() {
	$("#modalNuevoEstimulo").modal('hide');
}

function cerrarModalProb() {
	$("#modalProb").modal('hide');
}



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

	console.log(userDataFromLocalStorage);

	$scope.cerrarSesion = function () {
		console.log("Función cerrarSesion llamada");
		localStorage.setItem("logged", "false");
		localStorage.setItem("token", "");
		window.location.replace("/login.html");
	};

	$scope.tiposDoc = [
		{ id: 1, nombre: 'Decreto' },
		{ id: 2, nombre: 'Informe' },
		{ id: 3, nombre: 'Solicitud' },
		{ id: 4, nombre: 'Volante' }
	];

	var reqEstimulos = {
		method: 'GET',
		url: 'https://iuatesis.chickenkiller.com/api/final/estimulos/list',
		headers: {
			'Content-Type': 'application/json',
			'xauthtoken': userDataFromLocalStorage.authtoken
		},
	};
	$scope.selectedEstimulo = {};
	$scope.selectedMinisterio = {};
	$scope.Estimulos = [];
	$scope.Data = [];
	$scope.FiltroEstimulos = { valor: '' };
	$scope.filteredEstimulos = [];
	$scope.currentPage = 0;
	$scope.itemsPerPage = 5;


	$scope.cargarEstimulos = function () {
		$http(reqEstimulos).then(
			function (resp) {
				if (resp.status === 200) {
					$scope.Estimulos = resp.data;
					$scope.total = $scope.Estimulos.length;
					$scope.totalEstimulos = $scope.Estimulos.length;

					$scope.$watch('FiltroEstimulos.valor', function (newVal) {

						if (newVal == '') {
							$scope.filteredEstimulos = $scope.Estimulos;
							$scope.totalEstimulos = $scope.Estimulos.length;
						}
						else {
							$scope.filteredEstimulos = $filter('filter')($scope.Estimulos, newVal);
							$scope.totalEstimulos = $scope.filteredEstimulos.length;
						}
						$scope.currentPage = 0;
					});

				} else {

				}
			},
			function (respErr) {


			}
		);

	}


	$scope.firstPage = function () {
		return $scope.currentPage == 0;
	}

	$scope.primeraPag = function () {
		$scope.currentPage = 0;
	}

	$scope.ultimaPag = function () {
		var lastPageNum = Math.ceil($scope.totalEstimulos / $scope.itemsPerPage - 1);
		$scope.currentPage = lastPageNum;
	}

	$scope.lastPage = function () {

		var lastPageNum = Math.ceil($scope.totalEstimulos / $scope.itemsPerPage - 1);
		return $scope.currentPage == lastPageNum;
	}

	$scope.numberOfPages = function () {
		return Math.ceil($scope.totalEstimulos / $scope.itemsPerPage);
	}

	$scope.startingItem = function () {
		return $scope.currentPage * $scope.itemsPerPage;
	}

	$scope.pageBack = function () {
		$scope.currentPage = $scope.currentPage - 1;
	}

	$scope.pageForward = function () {
		$scope.currentPage = $scope.currentPage + 1;
	}
	var reqNotificaciones = {
		method: 'GET',
		url: 'https://iuatesis.chickenkiller.com/api/final/notificaciones/list/' + userDataFromLocalStorage.rolPrinc,
		headers: {
			'Content-Type': 'application/json',
			'xauthtoken': userDataFromLocalStorage.authtoken
		},
	};

	$scope.Notificacion = {};
	$scope.Notificaciones = [];
	$scope.FiltroNotificaciones = { valor: '' };
	$scope.totalNotificaciones = 0;
	$scope.currentPageNot = 0;
	$scope.cargarNotificaciones = function () {

		$http(reqNotificaciones).then(
			function (resp) {
				if (resp.status === 200) {
					$scope.Notificaciones = resp.data;

					if ($scope.totalNotificaciones < $scope.Notificaciones.length) {
						swal("Tiene nuevas notificaciones", "", "warning");
					}
					$scope.totalNotificaciones = $scope.Notificaciones.length;

				} else {

				}
			},
			function (respErr) {


			}
		);
	}


	$stomp.connect(URL_WS + "?xauthtoken=" + localStorage.getItem("token")).then(function (frame) {
		console.log('WebSocket connected:', frame);
		// Una vez conectado, puedes suscribirte a un canal específico

		$stomp.subscribe('/iw3/data', function (payload, headers, res) {
			console.log('Received data from WebSocket:', payload);
			$scope.cargarNotificaciones();
		});
	}).catch(function (error) {

	});


	var reqMinisterios = {
		method: 'GET',
		url: 'https://iuatesis.chickenkiller.com/api/final/ministerios/list',
		headers: {
			'Content-Type': 'application/json',
			'xauthtoken': userDataFromLocalStorage.authtoken
		},
	};

	$scope.Ministerio = {};
	$scope.Ministerios = [];

	$http(reqMinisterios).then(
		function (resp) {
			if (resp.status === 200) {
				$scope.Ministerios = resp.data;
				$scope.totalMinisterios = $scope.Ministerios.length;

			} else {

				localStorage.setItem("logged", "false");
				localStorage.setItem("token", "");
				window.location.replace("/login.html");
			}
		},
		function (respErr) {

			localStorage.setItem("logged", "false");
			localStorage.setItem("token", "");
			window.location.replace("/login.html");
		}
	);

	$scope.mostrarDiv = true;
	obtenerDivsVisibles();

	function obtenerDivsVisibles() {
		$scope.modalHistorialVisible = false;
		$scope.modalNuevoDocVisible = false;
		$scope.modalNuevoEstVisible = false;
		$scope.modalEstadisticasVisible = false;
		$scope.modalEstVisible = false;
		$scope.modalNotifVisible = false;

		if ($scope.userData.rol == 1) {
			$scope.modalHistorialVisible = true;
			$scope.modalNuevoDocVisible = true;
			$scope.modalNuevoEstVisible = true;
			$scope.modalEstadisticasVisible = true;
			$scope.modalEstVisible = true;
			$scope.modalNotifVisible = true;
		}
		else if ($scope.userData.rol != 5) {

			$scope.modalNuevoDocVisible = true;
			$scope.modalNuevoEstVisible = true;
			$scope.modalEstVisible = true;
			$scope.modalNotifVisible = true;
		}

	}

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
		let diferenciaHoras = diferenciaMilisegundos / (1000 * 60);
		let tiempoEstimado = parseInt(diferenciaHoras);


		var data = {
			'descripcion': descripcionEstimulo,
			'estado': 1,
			'fechaFin': null,
			'fechaInicio': fechaHoraActualSQL,
			'tiempoEstmado': tiempoEstimado,
			'titulo': tituloEstimulo,
			'usuarioCreador': userDataFromLocalStorage.idUser
		};

		var req = {
			method: 'POST',
			url: 'https://iuatesis.chickenkiller.com/api/final/estimulos/nuevoEstimulo',
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
		var selectElement = document.getElementById('estimulo');
		var indiceSeleccionado = selectElement.selectedIndex - 1;
		if (indiceSeleccionado < 0) {
			swal("Error", "Por favor, rellena todos los campos requeridos.", "error");
			return;
		}
		selectElement = document.getElementById('ministerio');
		indiceSeleccionado = selectElement.selectedIndex - 1;
		if (indiceSeleccionado < 0) {
			swal("Error", "Por favor, rellena todos los campos requeridos.", "error");
			return;
		} // Restar 1 para ajustar el índice debido a la opción "Selecciona un estimulo"
		selectElement = document.getElementById('tipoDoc');
		indiceSeleccionado = selectElement.selectedIndex - 1;
		if (indiceSeleccionado < 0) {
			swal("Error", "Por favor, rellena todos los campos requeridos.", "error");
			return;
		} // Restar 1 para ajustar el índice debido a la opción "Selecciona un estimulo"
		const form = document.getElementById('nuevoDocumentoForm');
		if (!form.checkValidity()) {
			swal("Error", "Por favor, rellena todos los campos requeridos.", "error");
			return;
		}
		else {


			tipoDocumento = $scope.selectedTipoDoc;
			idEstimulo = $scope.selectedEstimulo.id;
			idMinisterio = $scope.selectedMinisterio.id;
			tituloDocumento = document.getElementById('tituloDocumento').value;
			descripcionDocumento = document.getElementById('cuerpo').value;

			if ($scope.mostrarDiv == true && document.getElementById('final').checked)
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
				'estimulo': $scope.selectedEstimulo.id,
				'usuario': userDataFromLocalStorage.idUser,
				'ministerio': idMinisterio
			};

			var req = {
				method: 'POST',
				url: 'https://iuatesis.chickenkiller.com/api/final/documentos/nuevoDocumento',
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
	}

	$scope.Ejecutar = function (req) {
		return $http(req).then(
			function (resp) {
				if (resp.status === 200) {
					return resp.data;
				} else {
					console.log(req);
					return $req.reject("Error en la respuesta");
				}
			},
			function (respErr) {
				console.log(req);
				return $req.reject(respErr);
			}
		);
	}

	$scope.selectedTipoDoc = '';
	$scope.mostrarDiv = false;

	$scope.cambiarVisibilidadDiv = function () {

		if ($scope.selectedTipoDoc === 1)
			$scope.mostrarDiv = true;
		else
			$scope.mostrarDiv = false;
	};
	$scope.currentPageDoc = 0;
	$scope.itemsPerPageDoc = 5;
	$scope.filteredEstimulos = [];

	$scope.currentPageNot = 0;
	$scope.itemsPerPageNot = 5;

	$scope.currentPageNoticias = 0;
	$scope.itemsPerPageNoticias = 5;
	$scope.totalNoticias = 0;

	$scope.verDocumentos = function (i) {


		$('#documentos').modal('show');
		$('#modalEstimulos').modal('hide');
		$scope.estimuloSelec = i;

		$scope.Documentos = [];
		$scope.FiltroNotificaciones = { valor: '' };

		var reqDocs = {
			method: 'GET',
			url: 'https://iuatesis.chickenkiller.com/api/final/documentos/list/' + i,
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
		};

		$http(reqDocs).then(
			function (resp) {
				if (resp.status === 200) {
					$scope.Documentos = resp.data;
					$scope.totalDocs = $scope.Documentos.length;
					$scope.$watch('FiltroDocumentos.valor', function (newVal) {

						if (newVal == '') {
							$scope.filteredDocumentos = $scope.Documentos;
							$scope.totalDocs = $scope.Documentos.length;
						}
						else {
							$scope.filteredDocumentos = $filter('filter')($scope.Documentos, newVal);
							$scope.totalDocs = $scope.filteredDocumentos.length;
						}
						$scope.currentPageDoc = 0;
					});
				} else {

				}
			},
			function (respErr) {


			}
		);

	}

	$scope.getMinisterio = function (ministerioId) {
		var ministerio = $scope.Ministerios.find(function (m) {
			return m.id === ministerioId;
		});
		return ministerio ? ministerio.nombre : '';
	};

	$scope.getFechaEstimadaFin = function (estimulo) {
		var fecha = new Date(estimulo.fechaInicio);
		fecha.setMinutes(fecha.getMinutes() + estimulo.tiempoEstmado);
		return fecha;
	};

	$scope.getTipo = function (tipoId) {
		var tipo = $scope.tiposDoc.find(function (m) {
			return m.id === tipoId;
		});
		return tipo ? tipo.nombre : '';
	};

	$scope.getEstimulo = function () {
		var estimulo = $scope.Estimulos.find(function (m) {
			return m.id === $scope.estimuloSelec;
		});
		return estimulo ? estimulo.titulo : '';
	};

	////paginacion documentos
	$scope.firstPageDoc = function () {
		return $scope.currentPageDoc == 0;
	}

	$scope.primeraPagDoc = function () {
		$scope.currentPageDoc = 0;
	}

	$scope.ultimaPagDoc = function () {
		var lastPageNum = Math.ceil($scope.totalDocs / $scope.itemsPerPageDoc - 1);
		$scope.currentPageDoc = lastPageNum;
	}

	$scope.lastPageDoc = function () {
		var lastPageNum = Math.ceil($scope.totalDocs / $scope.itemsPerPageDoc - 1);
		return $scope.currentPageDoc == lastPageNum;
	}

	$scope.numberOfPagesDoc = function () {
		return Math.ceil($scope.totalDocs / $scope.itemsPerPageDoc);
	}

	$scope.startingItemDoc = function () {
		return $scope.currentPageDoc * $scope.itemsPerPageDoc;
	}

	$scope.pageBackDoc = function () {
		$scope.currentPageDoc = $scope.currentPageDoc - 1;
	}

	$scope.pageForwardDoc = function () {
		$scope.currentPageDoc = $scope.currentPageDoc + 1;
	}

	////paginacion noticias
	$scope.firstPageNoticias = function () {
		return $scope.currentPageNoticias == 0;
	}

	$scope.primeraPagNoticias = function () {
		$scope.currentPageNoticias = 0;
	}

	$scope.ultimaPagNoticias = function () {
		var lastPageNum = Math.ceil($scope.totalNoticias / $scope.itemsPerPageNoticias - 1);
		$scope.currentPageNoticias = lastPageNum;
	}

	$scope.lastPageNoticias = function () {
		var lastPageNum = Math.ceil($scope.totalNoticias / $scope.itemsPerPageNoticias - 1);
		return $scope.currentPageNoticias == lastPageNum;
	}

	$scope.numberOfPagesNoticias = function () {
		return Math.ceil($scope.totalNoticias / $scope.itemsPerPageNoticias);
	}

	$scope.startingItemNoticias = function () {
		return $scope.currentPageNoticias * $scope.itemsPerPageNoticias;
	}

	$scope.pageBackNoticias = function () {
		$scope.currentPageNoticias = $scope.currentPageNoticias - 1;
	}

	$scope.pageForwardNoticias = function () {
		$scope.currentPageNoticias = $scope.currentPageNoticias + 1;
	}

	//paginacion notificaciones
	$scope.firstPageNot = function () {
		return $scope.currentPageNot == 0;
	}

	$scope.primeraPagNot = function () {
		$scope.currentPageNot = 0;
	}

	$scope.ultimaPagNot = function () {
		var lastPageNum = Math.ceil($scope.totalNotificaciones / $scope.itemsPerPageNot - 1);
		$scope.currentPageNot = lastPageNum;
	}

	$scope.lastPageNot = function () {
		var lastPageNum = Math.ceil($scope.totalNotificaciones / $scope.itemsPerPageNot - 1);
		return $scope.currentPageNot == lastPageNum;
	}

	$scope.numberOfPagesNot = function () {
		return Math.ceil($scope.totalNotificaciones / $scope.itemsPerPageNot);
	}

	$scope.startingItemNot = function () {
		return $scope.currentPageNot * $scope.itemsPerPageNot;
	}

	$scope.pageBackNot = function () {
		$scope.currentPageNot = $scope.currentPageNot - 1;
	}

	$scope.pageForwardNot = function () {
		$scope.currentPageNot = $scope.currentPageNot + 1;
	}
	////////////////////////

	$scope.cerrarModalDocumentos = function () {
		$('#documentos').modal('hide');
		$('#modalEstimulos').modal('show');
	}

	$scope.aprobar = function (i, id) {

		var req = {
			method: 'PUT',
			url: 'https://iuatesis.chickenkiller.com/api/final/estimulos/cambiarEstado/' + i,
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			}
		};

		$scope.Ejecutar(req).
			then(function (resp) {
				req = {
					method: 'PUT',
					url: 'https://iuatesis.chickenkiller.com/api/final/documentos/cambiarEstado/' + id + "/2",
					headers: {
						'Content-Type': 'application/json',
						'xauthtoken': userDataFromLocalStorage.authtoken
					}
				};
				$scope.Ejecutar(req).
					then(function (resp) {
						$scope.cargarEstimulos();
						swal("¡Estímulo cerrado exitosamente!", "", "success");
						$('#documentos').modal('hide');
						$('#modalEstimulos').modal('show');

					});


			}).catch(function (error) {

				console.error('Error al cerrar el estímulo:', error);
				swal("Error", "Hubo un problema al cerrar el estímulo.", "error");
			});

	}

	$scope.estimulos = function () {
		const selectElement = document.getElementById("estimulo");
		const options = selectElement.options;
		for (let i = options.length - 1; i >= 0; i--) {
			if (options[i].label === "") {
				selectElement.remove(i);
			}
		}
		document.getElementById('nuevoDocumentoForm').reset();
		$scope.titulo = "ESTIMULOS ABIERTOS";
		$scope.esHistorico = false;
		$scope.cargarEstimulos();
	}

	$scope.pintar = function (estimulo) {

		var fecha = new Date(estimulo.fechaFin);
		fecha.setSeconds(0);
		var fecha2 = new Date($scope.getFechaEstimadaFin(estimulo));
		fecha2.setSeconds(0);
		return $scope.esHistorico && fecha <= fecha2;
	}

	$scope.getEstado = function (documento) {
		if (documento.esFinal == 1 && documento.estado == 2)
			return "ACEPTADO";
		else if (documento.esFinal == 1 && documento.estado == 3)
			return "RECHAZADO";
		else
			return "";

	}
	console.log(userDataFromLocalStorage.token);

	$scope.noticias = function () {
		var reqNoticias = {
			method: 'GET',
			url: 'https://iuatesis.chickenkiller.com/api/final/noticias/list',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
		};
		$http(reqNoticias).then(
			function (resp) {
				if (resp.status === 200) {
					$scope.Noticias = resp.data;

					$scope.totalNoticias = $scope.Noticias.length;

					$scope.$watch('FiltroNoticias.valor', function (newVal) {

						if (newVal == '') {
							$scope.filteredNoticias = $scope.Noticias;
							$scope.totalNoticias = $scope.Noticias.length;
						}
						else {
							$scope.filteredNoticias = $filter('filter')($scope.Noticias, newVal);
							$scope.totalNoticias = $scope.filteredNoticias.length;
						}
						$scope.currentPageNoticias = 0;
					});

				}
			},
			function (respErr) {

				$scope.Noticias = null;
				$scope.total = 0;
				$scope.totalNoticias = 0;
			}
		);
	}

	$scope.historial = function () {
		$scope.titulo = "ESTIMULOS CERRADOS";
		$scope.esHistorico = true;
		$('#modalEstimulos').modal('show');

		var reqEstimulosOld = {
			method: 'GET',
			url: 'https://iuatesis.chickenkiller.com/api/final/estimulos/list/old2',
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			},
		};
		$http(reqEstimulosOld).then(
			function (resp) {
				if (resp.status === 200) {
					$scope.Estimulos = resp.data;
					$scope.total = $scope.Estimulos.length;
					$scope.totalEstimulos = $scope.Estimulos.length;

					$scope.$watch('FiltroEstimulos.valor', function (newVal) {

						if (newVal == '') {
							$scope.filteredEstimulos = $scope.Estimulos;
							$scope.totalEstimulos = $scope.Estimulos.length;
						}
						else {
							$scope.filteredEstimulos = $filter('filter')($scope.Estimulos, newVal);
							$scope.totalEstimulos = $scope.filteredEstimulos.length;
						}
						$scope.currentPage = 0;
					});

				}
			},
			function (respErr) {

				$scope.Estimulos = null;
				$scope.total = 0;
				$scope.totalEstimulos = 0;
			}
		);
	}

	$scope.rechazar = function (i, id) {

		req = {
			method: 'PUT',
			url: 'https://iuatesis.chickenkiller.com/api/final/documentos/cambiarEstado/' + id + "/3",
			headers: {
				'Content-Type': 'application/json',
				'xauthtoken': userDataFromLocalStorage.authtoken
			}
		};

		$scope.Ejecutar(req).
			then(function (resp) {

				$scope.cargarEstimulos();
				swal("Documento rechazado exitosamente!", "", "success");
				$('#documentos').modal('hide');
				$('#modalEstimulos').modal('show');
			}).catch(function (error) {

				console.error('Error al rechazar el documento:', error);
				swal("Error", "Hubo un problema al rechazar el documento.", "error");
			});

	}
	$scope.crearNoticia = function () {
		$('#modalNuevaNoticia').modal('show');
		$('#modalNoticias').modal('hide');
	}

	$scope.nuevaNoticia = function () {

		const form = document.getElementById('descripcionNoticia');
		if (!form.checkValidity()) {
			swal("Error", "Por favor, rellena todos los campos requeridos.", "error");

			return false;
		}
		else {

			var descripcion = document.getElementById('descripcionNoticia').value;

			//FechaHoraInicio
			let fechaHoraActual = new Date();

			let fechaDocumentoSQL = fechaHoraActual.toISOString();

			var data = {
				'descripcion': descripcion,
				'fecha': fechaDocumentoSQL,
			};

			var req = {
				method: 'POST',
				url: 'https://iuatesis.chickenkiller.com/api/final/noticias/nuevaNoticia',
				headers: {
					'Content-Type': 'application/json',
					'xauthtoken': userDataFromLocalStorage.authtoken
				},
				data: data
			};

			$scope.Ejecutar(req).
				then(function (resp) {
					document.getElementById('descripcionNoticia').value = '';
					swal("Noticia registrada exitosamente!", "", "success");
					$scope.noticias();
					$('#modalNuevaNoticia').modal('hide');
					$('#modalNoticias').modal('show');

				}).catch(function (error) {

					console.error('Error al registrar el documento:', error);
					swal("Error", "Hubo un problema al registrar el documento.", "error");
				});
		}
	}

	$scope.estadisticas = function () {
		$window.open("/estadisticas.html");
	};

	$scope.init = function () {
		var ctx = document.getElementById('myChart').getContext('2d');
		var myChart = new Chart(ctx, {
			type: 'bar',
			data: {
				labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
				datasets: [{
					label: '# of Votes',
					data: [12, 19, 3, 5, 2, 3],
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
						beginAtZero: true
					}
				}
			}
		});
	};
});
