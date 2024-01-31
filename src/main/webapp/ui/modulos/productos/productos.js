var moduloProductos=angular.module('productos',['ui.bootstrap','ui-notification','oitozero.ngSweetAlert']);

moduloProductos.controller('ProductosController',
	function($scope, $rootScope, SweetAlert, Notification, $uibModal, productosService, $log) {
	$scope.title="Productos";


	$scope.data=[];
	
	$scope.refresh=function(buscar){
		productosService.list(buscar).then(
			function(resp){
				$scope.data=resp.data;
				$scope.totalDeItems = $scope.data.length;
			},
			function(err){}
		);
	}; //End refresh()
	
	$scope.refresh(false);
	
	
	$scope.verItemsPorPagina = 3;
 	$scope.totalDeItems = 0;
  	$scope.paginaActual = 1;
  	$scope.itemsPorPagina = $scope.verItemsPorPagina;
	$scope.setItemsPorPagina = function(num) {
		$scope.itemsPorPagina = num;
		$scope.paginaActual = 1;
	};//End setItemsPorPagina()
	
	$scope.remove=function(p) {
			SweetAlert.swal({
				title: "Eliminar producto",
				text: "Está segur@ que desea eliminar el producto <strong>"+p.nombre+"</strong>?",
				type: "warning",
			    showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "Si, eliminar producto!",
				cancelButtonText: "No",
				closeOnConfirm: true,
				html: true
			}, function(confirm){
				if(confirm) {
					productosService.remove(p).then(
						function(resp) {
							$scope.refresh();
							Notification.success({message:'El producto se ha eliminado',title:'Operación existosa!'});
						}, 
						function(err) {
							Notification.error({message:'No se ha podido eliminar el producto',title:'Operación fallida!'});
						}
					);
				}
			});
		};//End remove()
		
		
		$scope.addEdit=function(p){
			var modalInstance = $uibModal.open({
				animation : true,
				backdrop : false,
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'ui/modulos/productos/add-edit-modal-form.html',
				controller : 'AgregaEditaProductoModal',
				controllerAs : '$ctrl',
				size : 'large',
				resolve : {
					producto : angular.copy(p)
				}
			});

			modalInstance.result.then(function(instance) {
				if (!p) { //Agregar
					productosService.add(instance).then(
						function(resp) {
							$scope.refresh();
							Notification.success({message:'El producto se agregó correctamente',title:'Operación existosa!'});
						}, 
						function(err) {
							Notification.error({message:'No se ha podido agregar el producto',title:'Operación fallida!'});
						}
					);
				} else { //Editar
					productosService.edit(instance).then(
						function(resp) {
							$scope.refresh();
							Notification.success({message:'El producto se modificó correctamente',title:'Operación existosa!'});
						}, 
						function(err) {
							Notification.error({message:'No se ha podido modificar el producto',title:'Operación fallida!'});
						}
					);
				}
			}, function() {

			});
		}; //End addEdit()
	

}); //End ProductosController

moduloProductos.controller('AgregaEditaProductoModal',
	function($uibModalInstance, producto) {
		var $ctrl = this;
		$ctrl.agregar=!producto;
		
		if(!producto) {
			producto={nombre:'', precioLista:0.0, descripcion:'', enStock:false};
		}
		
		$ctrl.instance=producto;
		
		$ctrl.verGuardar=function(){
			return $ctrl.instance.nombre && $ctrl.instance.nombre.length>2 &&
				   $ctrl.instance.precioLista && $ctrl.instance.precioLista>0; 
		};
		
		$ctrl.ok=function() {
			$uibModalInstance.close($ctrl.instance);
		};
		
		$ctrl.volver = function() {
			$uibModalInstance.dismiss();
		};
	}
); //End AgregaEditaProductoModal

//***************** Services ***************************
moduloProductos.factory('productosService',
	function($http, URL_API_BASE) {
		return {
			list: function(buscar) {
				var qs="";
				if(buscar)
					qs="?parte="+buscar;
				return $http.get(URL_API_BASE+"productos"+qs);
			},
			add: function(p) {
				return $http.post(URL_API_BASE+"productos",p);
			},
			edit: function(p) {
				return $http.put(URL_API_BASE+"productos",p);
			},
			remove: function(p) {
				return $http.delete(URL_API_BASE+"productos/"+p.id);
			}
			
			
		}
	}
); //End productosService