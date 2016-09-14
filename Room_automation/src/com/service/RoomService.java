package com.service;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.db.DbConnection;
import com.pojos.RoomDetails;
import com.pojos.RoomSensor;

public class RoomService {
	
	/**
	 * @param roomDetails
	 * Add new room to database
	 */
	public void addRoomService(RoomDetails roomDetails)
	{
		PreparedStatement prepared = DbConnection.session
				.prepare("INSERT INTO room (roomid, buildg, floor, noofsensors) VALUES (?,?,?,?)");
		BoundStatement bound = prepared.bind(roomDetails.getRoomid(), roomDetails.getBuildg(), roomDetails.getFloor(),
				roomDetails.getNoOfSensors());
		DbConnection.session.execute(bound);
	}
		
	/**
	 * @param roomDetails
	 * Delete existing room from database
	 */
	public void deleteRoomService(RoomDetails roomDetails)
	{
		PreparedStatement prepared = DbConnection.session.prepare("DELETE  FROM room where roomid = ? ");
		BoundStatement bound = prepared.bind(roomDetails.getRoomid());
		DbConnection.session.execute(bound);

	}

	/**
	 * @param roomDetails
	 * @return List<RoomSensor>
	 * Get all rooms from database
	 */
	public List<RoomSensor> viewRoomService(RoomDetails roomDetails)
	{
		RoomSensor room = new RoomSensor();
		List<RoomSensor> list = new ArrayList<>();
		PreparedStatement prepared = DbConnection.session
				.prepare("select *  from roomsensor where buildg=? AND floor=? ALLOW FILTERING");
		BoundStatement bound = prepared.bind(roomDetails.getBuildg(), roomDetails.getFloor());
		ResultSet results = DbConnection.session.execute(bound);
		for (Row row : results) {

			room.setSensorid(row.getInt("sensorid"));
			room.setRoomid(row.getInt("roomid"));
			room.setBuildg(row.getString("buildg"));
			room.setFloor(row.getInt("floor"));
			room.setType(row.getString("type"));
			room.setIsenabled(row.getBool("isenabled"));
			list.add(room);

		}
		return list;

	}
	
	/**
	 * @param roomDetails
	 * @param sensorid
	 * @param i
	 * Add new sensor into database
	 */
	public void addSensorDetailsService(RoomDetails roomDetails,int sensorid, int i)
	{
		PreparedStatement prepared1 = DbConnection.session
				.prepare("INSERT INTO sensordetails (sensorid, roomid, isenabled, type) VALUES (?,?,?,?)");
		BoundStatement bound1 = prepared1.bind(sensorid, roomDetails.getRoomid(), true,
				roomDetails.getSensors().get(i));
		DbConnection.session.execute(bound1);
		
		prepared1 = DbConnection.session.prepare(
				"INSERT INTO roomSensor (sensorid, roomid, buildg,floor,type,isenabled) VALUES (?,?,?,?,?,?)");
		bound1 = prepared1.bind(sensorid, roomDetails.getRoomid(), roomDetails.getBuildg(), roomDetails.getFloor(),
				roomDetails.getSensors().get(i), true);
		System.out.println("for Loop");
		DbConnection.session.execute(bound1);		
	}
}
