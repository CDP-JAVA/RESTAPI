package com.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.pojos.SensorDetails;
import com.service.SensorService;



@Path("/sensor")

public class SensorDetailsController {
	
	SensorService sensorService;
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addsensor(SensorDetails sensorDetails) 
	{
		sensorDetails.setSensorid(sensorService.generateSensorId());
		sensorService.addSensorDetailsSevice(sensorDetails);
		return "Added ";
	}
}
