
$(window).on('load', function () {
    setTimeout(function () {
        $(".loader-page").css({ visibility: "hidden", opacity: "0" })
    }, 2000);

});

function cerrarModalNuevoDocumento()
{
    $("#modalNuevoDocumento").modal('hide');
}
function cerrarModalNuevoEstimulo()
{
    $("#modalNuevoEstimulo").modal('hide');
}


let home = angular.module('iw3', ['ngStomp'])


    .constant('URL_API_BASE', 'http://localhost:8080/api/final/')
    .constant('URL_BASE', 'http://localhost:8080/')
    .constant('URL_WS', '/api/final/ws')

	home.controller('home', function ($scope, $rootScope,wsService, $stomp, $http) {

	$rootScope.stomp = $stomp;

	//Verifico si el usuario está logueado, y si no está logueado lo redirecciono a la página de login
	if (localStorage.getItem("logged") != "true")
		window.location.replace("/login.html");




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

	$scope.estimulos = function() {
	
		window.location.replace("/estimulos.html");
	}

});