package com.vip.gardenapp.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vip.gardenapp.dao.CurrentLocationRepository;
import com.vip.gardenapp.dao.LocationsRepository;
import com.vip.gardenapp.dto.CurrentLocation;
import com.vip.gardenapp.dto.Locations;
import com.vip.gardenapp.service.ArduinoMoveService;

@RestController
public class SampleTestController {

	public ArduinoMoveService arduinoService;

	@Autowired
	LocationsRepository locationsRepository;

	@Autowired
	CurrentLocationRepository currentLocationRepository;

	public SampleTestController() {
		this.arduinoService = new ArduinoMoveService();
	}

	@RequestMapping("/moveto")
	public String moveToCoordinates(@RequestParam String x, String y) throws IOException {

		boolean result = arduinoService.move(x, y);
		if (result == true) {

			CurrentLocation location = currentLocationRepository.findAll().get(0);
			String xCoordinate = location.getXCoordinate();
			String yCoordinate = location.getYCoordinate();
			System.out.println("x = " + xCoordinate + " y = " + yCoordinate);
			if (x.equals("0")) {
				Integer val = Integer.MIN_VALUE;
				if (y.charAt(0) == ' ') {
					val = Integer.parseInt(xCoordinate) + Integer.parseInt(y.substring(1));
				} else {
					val = Integer.parseInt(xCoordinate) - Integer.parseInt(y.substring(1));

				}
				currentLocationRepository.deleteAll();
				currentLocationRepository
						.save(CurrentLocation.newInstance().setXCoordinate(val.toString()).setYCoordinate(yCoordinate));

			} else if (x.equals("1")) {
				Integer val = Integer.MIN_VALUE;

				if (y.charAt(0) == ' ') {
					val = Integer.parseInt(yCoordinate) + Integer.parseInt(y.substring(1));
				} else {
					val = Integer.parseInt(yCoordinate) - Integer.parseInt(y.substring(1));

				}
				currentLocationRepository.deleteAll();
				currentLocationRepository.save(CurrentLocation.newInstance().setXCoordinate(xCoordinate.toString())
						.setYCoordinate(val.toString()));
			}
			return "moved to x =  " + x + "y = " + y;
		} else {
			return "failed";
		}
	}

	@RequestMapping("/save")
	public String saveCoordinates() throws FileNotFoundException, IOException {

		CurrentLocation currentLocation = currentLocationRepository.findAll().get(0);

		Locations location = Locations.newInstance().setXCoordinate(currentLocation.getXCoordinate())
				.setYCoordinate(currentLocation.getYCoordinate());
		locationsRepository.save(location);
		return "SAVED";
	}

	@RequestMapping("/home")
	public String home() throws FileNotFoundException, IOException, InterruptedException {

		CurrentLocation currentLocation = currentLocationRepository.findAll().get(0);

		boolean resultx = arduinoService.move("0", "-" + currentLocation.getXCoordinate());
		if (resultx == false) {
			return "xMove failed";
		}
		TimeUnit.SECONDS.sleep(1);

		boolean resulty = arduinoService.move("1", "-" + currentLocation.getYCoordinate());
		if (resulty == false) {
			return "yMove failed";
		}
		CurrentLocation newLocation = CurrentLocation.newInstance().setXCoordinate("0").setYCoordinate("0");
		currentLocationRepository.deleteAll();
		currentLocationRepository.save(newLocation);
		return "AT HOME";
	}

	@RequestMapping("/scan")
	public String scan() throws FileNotFoundException, IOException, InterruptedException {

		CurrentLocation currentLocation = currentLocationRepository.findAll().get(0);

		List<Locations> locations = locationsRepository.findAll();

		Comparator<Locations> compareByXAndY = (Locations l1, Locations l2) -> {
			Integer l1X = Integer.parseInt(l1.getXCoordinate());
			Integer l2X = Integer.parseInt(l2.getXCoordinate());
			Integer l1Y = Integer.parseInt(l1.getYCoordinate());
			Integer l2Y = Integer.parseInt(l2.getYCoordinate());

			if (l1X.compareTo(l2X) < 0) {
				return -1;
			} else if (l1X.compareTo(l2X) > 0) {
				return 1;
			} else if (l1Y.compareTo(l2Y) < 0) {
				return -1;
			} else {
				return 1;
			}
		};
		Collections.sort(locations, compareByXAndY);
		int currX = Integer.parseInt(currentLocation.getXCoordinate());
		int currY = Integer.parseInt(currentLocation.getYCoordinate());
		for (Locations location : locations) {
			int toX = Integer.parseInt(location.getXCoordinate());
			int toY = Integer.parseInt(location.getYCoordinate());
			
			String diffX = (toX-currX)<=0?String.valueOf(toX - currX):"+"+String.valueOf(toX - currX);
			String diffY = (toY - currY)<=0?String.valueOf(toY - currY):"+"+ String.valueOf(toY - currY);
			System.out.println("moving x from " + currX + " to "+ toX + " diffX = " + diffX);
			boolean resultx = arduinoService.move("0",  diffX);
			TimeUnit.SECONDS.sleep(1);
			System.out.println("moving y from " + currY + " to "+ toY + "diffY = " + diffY);
			boolean resulty = arduinoService.move("1", diffY);
			TimeUnit.SECONDS.sleep(3);
			CurrentLocation newLocation = CurrentLocation.newInstance().setXCoordinate(String.valueOf(toX)).setYCoordinate(String.valueOf(toY));
			currentLocationRepository.deleteAll();
			currentLocationRepository.save(newLocation);
			currX = toX;
			currY = toY;
		}
		
		return "AT HOME";
	}

}
