package com;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.DBConnection;
import java.util.ArrayList;

@Path("/sensor")

public class SensorController {

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	
	//method to add a new sensor
	public String addSensor(Sensor sensor) {		

		int i = 0;
		int sensorid = 0;

		ResultSet results = DBConnection.session.execute("select MAX(sensorid) from sensor");

		if (results.isExhausted()) {
		
			sensorid = 1;
		} else {
		
			for (Row row : results) {
				i = row.getInt(0);
				sensorid = i + 1;
			
			}
		}

		PreparedStatement pre = DBConnection.session
				.prepare("INSERT INTO sensor (sensorid, sensortype, frequency) VALUES (?,?,?)");
		BoundStatement bound1 = pre.bind(sensorid, sensor.getSensortype(), sensor.getFrequency());
		DBConnection.session.execute(bound1);
		sensorid++;

		return "Added Successfully";

	}

	@GET
	@Path("/view")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	
	//method to view the list of sensors
	public List<Sensor> viewSensor() {				
		
		List<Sensor> list = new ArrayList<>();
		ResultSet results = DBConnection.session.execute("select * from sensor");
		for (Row row : results) {
			Sensor sensor1 = new Sensor();
			sensor1.setSensorid(row.getInt("sensorid"));
			sensor1.setSensortype(row.getString("sensortype"));
			sensor1.setFrequency(row.getInt("frequency"));

			list.add(sensor1);

		}

		return list;

	}

}
