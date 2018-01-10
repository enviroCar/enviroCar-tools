var request = require('request-promise');

var result = [];
var total = 0;

request({
    uri: 'https://envirocar.org/api/stable/tracks/',
    json: true
  }).then(function(data) {

  total = data.tracks.length;

  data.tracks.forEach(function(t) {
    request({
        uri: 'https://envirocar.org/api/stable/tracks/'+t.id,
        json: true
    }).then(function(track) {
      var id = track.properties.id;
      // console.info(id);

      var features = track.features;
      var start = features[0];
      var end = features[features.length-1];

      result.push({
        id: id,
        startPosition: {
          geometry: start.geometry
        },
        endPosition: {
          geometry: end.geometry
        }
      });

      if (result.length === total) {
        console.info(JSON.stringify(result, null, 4));
      }
    })
  });
});
