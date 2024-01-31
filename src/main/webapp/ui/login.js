let moduloLogin=angular.module('iw3',['ngStorage', 'oitozero.ngSweetAlert'])


    .constant('URL_API_BASE', 'http://localhost:8080/api/final/')
    .constant('URL_BASE', 'http://localhost:8080/')
    .constant('URL_WS', '/api/final/ws')

moduloLogin.controller('loginController', function($scope, $localStorage, $http){

    //Si se llegó al login, me aseguro que se borren los datos del localstorage ya que no hay nadie logueado
    delete $localStorage.userdata;
    localStorage.setItem("logged","false");

    //Valido cuando se presiona el botón para iniciar sesión
    $scope.validar = function (){
      if($scope.dni.length>=4 && $scope.claveUsuario.length>=2){
         let user={dni:$scope.dni,password:$scope.claveUsuario};

         let req = {
              method: 'POST',
              url: 'http://localhost:8080/login-user?dni='+user.dni+'&password='+user.password,
              headers : { 'Content-Type': 'form-data' }
          };

          let login = function () {
              $http(req).then(
                  function(resp){
                      if(resp.status===200) {
                          console.log(resp.data);
                          $localStorage.userdata=resp.data;
                          localStorage.setItem("logged","true");
                          localStorage.setItem("token",resp.data.authtoken);
                          window.location.replace("http://localhost:8080");
                      }else{
                          console.log("No se pudo loguear.");
                          alert("Los datos ingresados son incorrectos.");
                      }
                  },
                  function(respErr){
                      console.log("No se pudo loguear.");
                      alert("Los datos ingresados son incorrectos.");
                  }
              );
          };

          login();

          }
        };

});