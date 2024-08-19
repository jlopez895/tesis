let moduloLogin=angular.module('iw3',['ngStorage', 'oitozero.ngSweetAlert'])


    .constant('URL_API_BASE', 'https://iuatesis.chickenkiller.com/api/final/')
    .constant('URL_BASE', 'https://iuatesis.chickenkiller.com/')
    .constant('URL_WS', '/api/final/ws')

moduloLogin.controller('loginController', function($scope, $localStorage, $http){

    //Si se llegó al login, me aseguro que se borren los datos del localstorage ya que no hay nadie logueado
    delete $localStorage.userdata;
    localStorage.setItem("logged","false");

    //Valido cuando se presiona el botón para iniciar sesión
    $scope.validar = function (){
      if($scope.legajo.length>=4 && $scope.claveUsuario.length>=2){
         let user={legajo:$scope.legajo,password:$scope.claveUsuario};

         let req = {
              method: 'POST',
              url: 'https://iuatesis.chickenkiller.com/login-user?legajo='+user.legajo+'&password='+user.password,
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
                          window.location.replace("https://iuatesis.chickenkiller.com");
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