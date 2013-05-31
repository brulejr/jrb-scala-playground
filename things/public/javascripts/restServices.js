var restThingModule = angular.module('restServices', [ 'ngResource' ]);

restThingModule.constant('thingListUri', '/api/things');

restThingModule.factory('ThingList', [ '$resource', 'thingListUri',
		function($resource, thingListUri) {
			return $resource(thingListUri, {}, {
				query : {
					method : 'GET',
					isArray : true
				}
			});
		} ]);
