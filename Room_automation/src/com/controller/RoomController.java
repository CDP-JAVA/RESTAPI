package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.pojos.RoomDetails;
import com.pojos.RoomSensor;
import com.service.RoomService;
import com.service.SensorService;

@Path("/room")

public class RoomController {
	
	RoomService roomService;
	SensorService sensorService;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addRoom(RoomDetails roomDetails) {
		
		int i = 0;
		int sensorid = 0;

		roomService.addRoomService(roomDetails);
		sensorid=sensorService.generateSensorId();
		
		for (i = 0; i < roomDetails.getNoOfSensors(); i++) {
			roomService.addSensorDetailsService(roomDetails,sensorid,i);
			sensorid++;
		}
		return "Added Successfully";
	}

	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteRoom(RoomDetails roomDetails) {
		
		roomService.deleteRoomService(roomDetails);
		sensorService.deleteSensorDetailsService(roomDetails);
		return "Deleted Successfully";
	}

	@POST
	@Path("/view")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoomSensor> viewRoom(RoomDetails roomDetails) {
		List<RoomSensor> list = new ArrayList<>();
		list=roomService.viewRoomService(roomDetails);
		return list;
	}

}
