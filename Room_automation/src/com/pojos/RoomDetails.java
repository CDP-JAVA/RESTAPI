package com.pojos;

import java.util.ArrayList;

public class RoomDetails {

		int roomid;
		String buildg;
		int floor;
		int noOfSensors;
		ArrayList <String> sensors;

		public int getRoomid() {
			return roomid;
		}
		public void setRoomid(int roomid) {
			this.roomid = roomid;
		}
		public String getBuildg() {
			return buildg;
		}
		public void setBuildg(String buildg) {
			this.buildg = buildg;
		}
		public int getFloor() {
			return floor;
		}
		public void setFloor(int floor) {
			this.floor = floor;
		}
		public int getNoOfSensors() {
			return noOfSensors;
		}
		public void setNoOfSensors(int noOfSensors) {
			this.noOfSensors = noOfSensors;
		}
		public ArrayList<String> getSensors() {
			return sensors;
		}
		public void setSensors(ArrayList<String> sensors) {
			this.sensors = sensors;
		}
		
}
