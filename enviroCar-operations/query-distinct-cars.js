var manufacturers = [
	{name: "Volkswagen", matches: ["volkswagen", "vw"]},
	{name: "BMW", matches: ["bmw"]},
	{name: "Mercedes Benz", matches: ["mercedes", "mercedes benz", "benz", "daimler"]},
	{name: "Audi", matches: ["audi"]},
	{name: "Skoda", matches: ["skoda"]},
	{name: "Citroen", matches: ["citroen"]},
	{name: "Volvo", matches: ["volvo"]},
	{name: "Peugeot", matches: ["peugeot"]},
	{name: "Nissan", matches: ["nissan"]},
	{name: "Toyota", matches: ["toyota"]},
	{name: "Suzuki", matches: ["suzuki"]},
	{name: "Honda", matches: ["honda"]},
	{name: "Opel", matches: ["opel"]},
	{name: "Subaru", matches: ["subaru"]},
	{name: "Mitsubishi", matches: ["mitsubishi"]},
	{name: "Ford", matches: ["ford"]},
	{name: "Renault", matches: ["renault"]},
	{name: "Jeep", matches: ["jeep"]},
	{name: "Chrysler", matches: ["chrysler"]},
	{name: "Fiat", matches: ["fiat"]},
	{name: "Mazda", matches: ["mazda"]},
	{name: "Seat", matches: ["seat"]},
	{name: "Lexus", matches: ["lexus"]},
	{name: "Kia", matches: ["kia"]},
	{name: "Fiat", matches: ["fiat"]},
	{name: "Porsche", matches: ["porsche"]},
	{name: "Chevrolet", matches: ["chevrolet"]}
];

var total = db.sensors.count();
var c = 1;
var distinctCars = [];

var idToDistinct = {};

function equals(car1, car2) {
	var prop1 = car1.properties;
	var prop2 = car2.properties;
	
	if (prop1.fuelType !== prop2.fuelType) {
		return false;
	}
	
	if (prop1.constructionYear !== prop2.constructionYear) {
		return false;
	}
	
	if (prop1.engineDisplacement !== prop2.engineDisplacement) {
		return false;
	}
	
	if (prop1.model.trim() !== prop2.model.trim()) {
		return false;
	}
	
	if (prop1.manufacturer.trim() !== prop2.manufacturer.trim()) {
		return false;
	}
	
	return true;
}

function addCar(car) {
	var exists = false;
	
	distinctCars.forEach(function(c) {
		if (exists) return;
		
		if (equals(c, car)) {
			exists = true;
			idToDistinct[car._id.toString()] = c._id.toString();
		}
	});
	
	if (!exists) {
		car.properties.manufacturer = car.properties.manufacturer.trim();
		car.properties.model = car.properties.model.trim();

		delete car.className;
		delete car.created;
		delete car.modified;
		delete car.type;

		distinctCars.push(car);
	}
}

function processResult(result) {
	
	if (result.properties.manufacturer) {
		var man = result.properties.manufacturer;
		
		manufacturers.forEach(function(m) {
			if (m.matches.indexOf(man.toLowerCase().trim()) >= 0) {
				result.properties.manufacturer = m.name;
			}
		});

		addCar(result);
	}
	
	if (c++ === total) {
		//printjson(distinctCars);
		printjson(idToDistinct);
		
		print("total cars: "+total);
		print("distinct cars: "+distinctCars.length);
	}
}

db.sensors.find().forEach(processResult);
