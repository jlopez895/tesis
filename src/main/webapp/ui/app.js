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

app.controller('controllerPedidos', function ($scope) {
    if (localStorage.getItem("logged") != "true")
        window.location.replace("/login.html");

    $scope.cerrarSesion = function () {
        localStorage.setItem("logged", "false");
        localStorage.setItem("token", "");
        window.location.replace("/login.html");
    };

    var reqEstimulos = {
        method: 'GET',
        url: 'http://localhost:8080/api/final/estimulos/list',
        headers: {
            'Content-Type': 'application/json',
            'xauthtoken': token
        },
    };

    $scope.Estimulo = {};
	$scope.Estimulos = [];
	$scope.Data = [];
	$scope.FiltroEstimulos = { valor: '' };


    $http(reqEstimulos).then(
        function (resp) {
            if (resp.status === 200) {
                $scope.Estimulos = resp.data;
                $scope.totalEstimulos = $scope.Estimulos.length;

                console.log($scope.Estimulos.lenght);
            } else {
                console.log(reqEstimulos);
                alert("No se pueden obtener los estimulos");
            }
        },
        function (respErr) {

            alert("No se pueden obtener los estimulos");
        }
    );

    var tituloEstimulo, descripcionEstimulo, fechaEstFinEstimulo, horaEstFinEstimulo;
    $scope.nuevoEst = function () {

        tituloEstimulo = document.getElementById('tituloEstimulo').value;
        descripcionEstimulo = document.getElementById('descripcionEstimulo').value;
        fechaEstFinEstimulo = document.getElementById('fechaEstFinEstimulo').value;
        horaEstFinEstimulo = document.getElementById('horaEstFinEstimulo').value;


        var data = {
            'titulo': tituloEstimulo,
            'descripcion': descripcionEstimulo,
            'tiempoEstmado': fechaEstFinEstimulo,
            'usuarioCreador': localStorage.getItem("userdata")
        };  

        var req = {
            method: 'POST',
            url: 'http://localhost:8080/api/final/estimulos/nuevoEstimulo',
            headers: {
                'Content-Type': 'application/json',
                'xauthtoken': token
            },
            data: data


        };

        $scope.Ejecutar(req);


    }

    $scope.Ejecutar = function (req) {
        $http(req).then(
            function (resp) {
                if (resp.status === 200) {

                } else {

                    console.log(req);
                    alert("Error");
                    ok = false;
                }
            },
            function (respErr) {

                console.log(req);
                alert("Error");
                ok = false;
            }
        );

    }
});