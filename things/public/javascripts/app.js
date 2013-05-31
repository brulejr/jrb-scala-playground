var appModule = angular.module('things', [ 'restServices' ]);

appModule.controller('ThingCtrl', [ '$scope', 'ThingList',
		function($scope, ThingList) {

			$scope.things = ThingList.query();

		} ]);
