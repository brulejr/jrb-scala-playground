var appModule = angular.module('things', [ 'ngGrid', 'restServices' ]);

appModule.controller('ThingCtrl', [ '$scope', 'ThingList',
		function($scope, ThingList) {

			$scope.things = ThingList.query();
			$scope.gridOptions = {
				data : 'things',
				columnDefs : [ {
					field : 'name',
					displayName : 'Name',
					width : '150px'
				}, {
					field : 'description',
					displayName : 'Description',
					width : '290px'
				}, {
					field : 'location',
					displayName : 'Location',
					width : '293px'
				} ],
				showFilter: true,
				showSelectionCheckbox: true
			};

		} ]);
