package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.Sensor;
import com.SensorController;

public class SensorControllerTest {
	private SensorController sensorController;
	private Sensor sensor;
	@Before
	public void setup()
	{
		sensorController = new SensorController();
		sensor = new Sensor();
		sensor.setFrequency(5);
		sensor.setSensorid(1);
		sensor.setSensortype("Temperature");
	}
	
	@Test
	public void addSensorTest()
	{
		String result = sensorController.addSensor(sensor);
		Assert.assertEquals(result, "Added Successfully");
		
	}
	
	
	@Test
	public void viewSensorTest()
	{
		List result = sensorController.viewSensor();
		Assert.assertNotNull(result);
	}

}
