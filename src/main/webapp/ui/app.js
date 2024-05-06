	let app = angular.module('iw3', []);
	
	$(window).on('load', function() {
		setTimeout(function() {
			$(".loader-page").css({ visibility: "hidden", opacity: "0" })
		}, 2000);
	
	});
	
	// Función para llenar un textarea con el contenido de un archivo de texto
	/*function llenarTextareaDesdeArchivo(rutaArchivo) {
		$.get(rutaArchivo, function(data) {
			// Asigna el contenido del archivo al textarea
			$('#descripcionProblematica').val(data);
		});
	}
	
	// Llama a la función para llenar el textarea con el contenido del archivo
	llenarTextareaDesdeArchivo('random.txt');*/
	
	//Funciones para cerrar modales
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
	
	
	
	app.controller('controllerPedidos', function($scope, $http) {
		
		
		if (localStorage.getItem("logged") != "true")
			window.location.replace("/login.html");
	
		//Circulo con iniciales
		let userDataFromLocalStorage = JSON.parse(localStorage.getItem("ngStorage-userdata"));
		let fullName = userDataFromLocalStorage.fullname;
		let initials = fullName.split(', ')
	        .map(word => word.charAt(0))
	        .join('');
	   
		//Info usuario
		$scope.userData = {
			iniciales: initials,
			fullname: fullName,
			email: userDataFromLocalStorage.email,
			rol: "Administrador" 
		};
	
		//Cerrar sesion
		$scope.cerrarSesion = function() {
			console.log("Función cerrarSesion llamada");
			localStorage.setItem("logged", "false");
			localStorage.setItem("token", "");
			window.location.replace("/login.html");
		};
		
		
		//Lista de estimulos
		$scope.currentPage = 1;
		$scope.totalItems = 0;
		$scope.itemsPerPage = 10;
		$scope.totalPages = 0;
		$scope.pages = [];
		
		$scope.setPage = function(page) {
		    if (page >= 1 && page <= $scope.totalPages) {
		        $scope.currentPage = page;
		        $scope.getEstimulos();
		    }
		};
		
				// Función para formatear las fechas
		function formatDates(estimulos) {
		    return estimulos.map(function(estimulo) {
		        estimulo.fechaInicio = formatDate(estimulo.fechaInicio);
		        estimulo.fechaFin = formatDate(estimulo.fechaFin);
		        return estimulo;
		    });
		}
		
		// Función para formatear una fecha específica
		function formatDate(dateString) {
		    var date = new Date(dateString);
		    var day = date.getDate().toString().padStart(2, '0');
		    var month = (date.getMonth() + 1).toString().padStart(2, '0');
		    var year = date.getFullYear();
		    var hours = date.getHours().toString().padStart(2, '0');
		    var minutes = date.getMinutes().toString().padStart(2, '0');
		    var formattedDate = `${day}/${month}/${year} ${hours}:${minutes}`;
		    return formattedDate;
		}
	
	
		$scope.getEstimulos = function() {
		    var reqEstimulos = {
		        method: 'GET',
		        url: 'http://localhost:8080/api/final/estimulos/list',
		        headers: {
		            'Content-Type': 'application/json',
		            'xauthtoken': userDataFromLocalStorage.authtoken
		        },
		        params: {
		            page: $scope.currentPage,
		            size: $scope.itemsPerPage
		        }
		    };
		
		    $http(reqEstimulos)
		    .then(function(response) {
		        // Formatear las fechas antes de asignarlas a $scope.Estimulos
		        $scope.Estimulos = formatDates(response.data.content);
		        $scope.totalItems = response.data.totalElements;
		        $scope.totalPages = Math.ceil($scope.totalItems / $scope.itemsPerPage);
		        $scope.pages = Array.from({length: $scope.totalPages}, (_, i) => i + 1);
		    })
		    .catch(function(error) {
		        console.error('Error al obtener la lista de estímulos:', error);
		    });
		};
		
		
		
	
		$scope.getEstimulos();
		
		
		//Creacion de estimulos
		function limpiarCampos() {
			document.getElementById('tituloEstimulo').value = '';
			document.getElementById('descripcionEstimulo').value = '';
			document.getElementById('fechaEstFinEstimulo').value = '';
			document.getElementById('horaEstFinEstimulo').value = '';
		}
	
		$('#modalNuevoEstimulo').on('shown.bs.modal', function() {
			limpiarCampos(); 
		});
	
		var tituloEstimulo, descripcionEstimulo, fechaEstFinEstimulo, horaEstFinEstimulo;
		$scope.nuevoEst = function() {
	
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
				'titulo': tituloEstimulo /*,
				'usuarioCreador': {
					"id": userDataFromLocalStorage.idUser,
					"apellido":userDataFromLocalStorage.apellido,
					"email":userDataFromLocalStorage.email,
					"nombre":userDataFromLocalStorage.nombre,
					"username":userDataFromLocalStorage.username,
					"idRolPrincipal":userDataFromLocalStorage.rolPrinc
				}*/
			};
			console.log(data.usuarioCreador)
	
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
				then(function(resp) {
					limpiarCampos();
					swal("¡Estímulo registrado exitosamente!", "", "success");
					$('#modalNuevoEstimulo').modal('hide');
	
				}).catch(function(error) {
	
					console.error('Error al registrar el estímulo:', error);
					swal("Error", "Hubo un problema al registrar el estímulo.", "error");
				});
	
	
		}
	
		$scope.Ejecutar = function(req) {
			return $http(req).then(
				function(resp) {
					if (resp.status === 200) {
						return resp.data;
					} else {
						console.log(req);
						return $q.reject("Error en la respuesta");
					}
				},
				function(respErr) {
					console.log(req);
					return $q.reject(respErr);
				}
			);
		}
		
		
		
		//Notificaciones
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
			function(resp) {
				if (resp.status === 200) {
					$scope.Notificaciones = resp.data;
					$scope.totalNotificaciones = $scope.Notificaciones.length;
	
				} else {
					console.log(reqEstimulos);
					alert("No se pueden obtener las notificaciones");
				}
			},
			function(respErr) {
	
				alert("No se pueden obtener las notificaciones");
			}
		);
	
		$scope.mostrarDiv = true;
	
		
	
		$scope.cambiarVisibilidadDiv = function() {
			// Lógica para determinar si el div debe mostrarse u ocultarse según la opción seleccionada
			if (document.getElementById('tipoDoc').value == "1") { // Cambia 'opcionMostrar' por el valor de la opción que quieres mostrar
				$scope.mostrarDiv = true;
			} else {
				$scope.mostrarDiv = false;
			}
		}
	});