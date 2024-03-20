let app = angular.module('iw3', []);

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

app.controller('controllerPedidos', function($scope) {
	
    $scope.cerrarSesion = function() {
        localStorage.setItem("logged", "false");
        localStorage.setItem("token", "");
        window.location.replace("/login.html");
    };
});