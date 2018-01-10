var cars = [
"52692401e4b0d22bedaec6ef",
"51ee5e9ce4b058cd3d4bd2e2",
"5252d114e4b04f4d08f204a5",
"52692466e4b0d22bedaec738",
"52284e5de4b00a043c455f35",
"5239707be4b060b8865b04c2",
"52d63e65e4b0f9afbe27139d",
"5238430ce4b060b8865ac441",
"51ffab4fe4b058cd3d654006",
"52455cf3e4b0d7bdddd723ba",
"526932b5e4b00d71b6dee49e",
"52692d71e4b00d71b6ded884",
"53f7b1a6e4b04c314e7cabbf",
"559f85d9e4b07207d897f754"
];

var total = db.tracks.count();
var c = 1;
var remove = true;

var filteredTracks = [];

function processResult(result) {
	var id = result._id.valueOf();
	print(id);
	return;
	
	var measCount = db.measurements.find({"track": DBRef("tracks", ObjectId(id))}).count();
	
	if (!measCount) {
		print("track has no measurements! "+ result._id.toString());
		filteredTracks.push(id);
		if (remove) {
			db.tracks.remove({"_id": ObjectId(id)});
		}
	}
	else {
		print("skipping "+id);
	}
	
	if (++c === total) {
		printjson(filteredTracks);
	}
}

db.tracks.find().forEach(processResult);
