package util;

public class DistanceCalculator {

	public static Double distanceCalculator(Double hqLatitude, Double hqLongtude, Double otherLatitude, Double otherLongitude) {

		return (3963.0 * Math.acos((Math.sin(Math.toRadians(hqLatitude)) * Math.sin(Math.toRadians(otherLatitude)))
				+ Math.cos(Math.toRadians(hqLatitude)) * Math.cos(Math.toRadians(otherLatitude)) * Math.cos(Math.toRadians(otherLongitude - hqLongtude))));
	}
}