// mongodb script to analyse measurements

var lsCount = 0;
var unsureCount = 0;
var lhCount = 0;
var total = db.measurements.count();
var c = 1;

var resultSet = {};
resultSet.ls = {};
resultSet.lh = {};
resultSet.unsure = {};

print("Total count: "+total);

function setTrack(obj, track) {
	if (track) {
		if (obj[track.$id]) {
			obj[track.$id]++;
		}
		else {
			obj[track.$id] = 1
		}
	}
}

function analyse(ms) {
	if (ms && ms.ls) {

		var result = {};
		result.unambiguous = [];
		result.problematic = [];
		
		for (var key in ms.ls) {
			if (ms.ls.hasOwnProperty(key)) {
				var val = ms.ls[key];
				var valLh = ms.lh[key];
				if (valLh) {
					var fac = val / valLh;
					result.problematic.push({
						"track": key,
						"lsCount": val,
						"lhCount": valLh,
						"factor": fac,
						"proposal": (fac < 1 || valLh + val < 10) ? "lh" : "ls"
					});
				}
				else {
					result.unambiguous.push(key);
				}
			}
		}
	
		printjson(result);
		print("unambiguous: "+result.unambiguous.length);
	}
}

function processResults(result) {

	if (result.phenomenons) {
		result.phenomenons.forEach(function(p) {
			if (p.phen._id === "Consumption") {
				if (p.value <= 0.028) {
					// l/s
					lsCount++;
					setTrack(resultSet.ls, result.track);
				}
				else if (p.value > 0.028 && p.value <= 0.05) {
					// not sure
					unsureCount++;
					setTrack(resultSet.unsure, result.track);
				}
				else {
					// l/h
					lhCount++;
					setTrack(resultSet.lh, result.track);
				}
			}
		});
	}
	
	if (++c === total) {
		print("l/s count: "+lsCount);
		print("l/h count: "+lhCount);
		print("unsure count: "+unsureCount);
		analyse(resultSet);
	}
	
	if (c % 1000 === 0) {
		//print(c +"/"+total);
	}
}

db.measurements.find().forEach(processResults);
