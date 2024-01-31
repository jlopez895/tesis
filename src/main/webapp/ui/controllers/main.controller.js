angular.module('iw3').controller('Main',
	function($scope, $rootScope, coreService) {
		$scope.title="Hola";
		
		$scope.logout=function(){
			coreService.logout();
		};
	} 
);