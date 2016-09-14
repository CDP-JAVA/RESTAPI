package com.service;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.db.DbConnection;
import com.pojos.RoomDetails;
import com.pojos.RoomSensor;
import com.pojos.SensorDetails;

public class SensorService {

	/**
	 * @return
	 * Generate incremented unique sensor Id
	 */
	public int generateSensorId() {
		int i = 0, sensorid = 0;

		ResultSet results = DbConnection.session.execute("select MAX(sensorid) from sensordetails");

		if (results.isExhausted()) {
			System.out.println("No data");
			sensorid = 1;
		} else {
			System.out.println("Data");

			for (Row row : results) {
				i = row.getInt(0);
				sensorid = i + 1;
				System.out.println(i);
			}
		}
		return sensorid;
	}


	/**
	 * @param roomDetails
	 * Delete SensorDetails from database once the room is deleted
	 */
	public void deleteSensorDetailsService(RoomDetails roomDetails) {
		PreparedStatement prepared = DbConnection.session
				.prepare("SELECT sensorid FROM roomsensor WHERE roomid=? ALLOW FILTERING");
		BoundStatement bound = prepared.bind(roomDetails.getRoomid());

		ResultSet results = DbConnection.session.execute(bound);

		for (Row row : results) {
			prepared = DbConnection.session.prepare("DELETE  FROM roomsensor where sensorid=? ");
			bound = prepared.bind(row.getInt("sensorid"));
			DbConnection.session.execute(bound);
		}
		prepared = DbConnection.session.prepare("SELECT sensorid FROM sensordetails WHERE roomid=? ALLOW FILTERING ");
		bound = prepared.bind(roomDetails.getRoomid());

		results = DbConnection.session.execute(bound);

		for (Row row : results) {

			prepared = DbConnection.session.prepare("DELETE  FROM sensordetails where sensorid=? ");
			bound = prepared.bind(row.getInt("sensorid"));
			DbConnection.session.execute(bound);
		}
	}

	/**
	 * @param sensorDetails
	 * Add new sensor to database
	 */
	public void addSensorDetailsSevice(SensorDetails sensorDetails) {
		PreparedStatement prepared = DbConnection.session
				.prepare("INSERT INTO sensordetails (sensorid, roomid, isenabled, type) VALUES (?,?,?,?)");
		BoundStatement bound = prepared.bind(sensorDetails.getSensorid(), sensorDetails.getRoomid(),
				sensorDetails.isIsenabled(), sensorDetails.getType());
		DbConnection.session.execute(bound);

		addRoomSensorService(sensorDetails);
	}

	
	/**
	 * @param sensorDetails
	 * Add sensor details and room details in single table
	 */
	public void addRoomSensorService(SensorDetails sensorDetails) {
		PreparedStatement prepared = DbConnection.session.prepare("SELECT * FROM room WHERE roomid=? ALLOW FILTERING");
		BoundStatement bound = prepared.bind(sensorDetails.getRoomid());
		ResultSet results = DbConnection.session.execute(bound);
		RoomSensor room = new RoomSensor();
		for (Row row : results) {
			room.setRoomid(row.getInt("roomid"));
			room.setBuildg(row.getString("buildg"));
			room.setFloor(row.getInt("floor"));
		}
		room.setSensorid(sensorDetails.getSensorid());
		room.setType(sensorDetails.getType());
		room.setIsenabled(sensorDetails.isIsenabled());

		// Insert room and sensor details into single table
		prepared = DbConnection.session
				.prepare("INSERT INTO roomSensor (sensorid, roomid, buildg,floor,type,isenabled) VALUES (?,?,?,?,?,?)");
		bound = prepared.bind(room.getSensorid(), room.getRoomid(), room.getBuildg(), room.getFloor(), room.getType(),
				room.isIsenabled());
		DbConnection.session.execute(bound);
	}

}
