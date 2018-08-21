package com.flemmli97.fatemod.client.model;

public class ModelUtils {

	public static float degToRad(float degree)
	{
		return degree* ((float)Math.PI/180F);
	}
	
	public static float radToDeg(float rad)
	{
		return rad* (180/(float)Math.PI);
	}
	
	private static float anim(float defaultAngle, int ticker, float startAngle, float endAngle, int startTick, int duration, float partialTicks)
	{
		ticker -=startTick;
		if(ticker< 0 || ticker>=duration)
			return defaultAngle;
		float m = (endAngle - startAngle) / (float)duration;
		
		float rotationLastTick = startAngle + m*Math.max(0, ticker-1);
		float rotationNow = startAngle + m*ticker;
		float actualRotation = rotationLastTick + (rotationNow - rotationLastTick) * (partialTicks);
		return actualRotation;
	}
	
	/**
	 * Basic linear animation for an model part.
	 * @param ticker Ticker for the animation. Ticks down!
	 * @param startDegree Degree of first tick
	 * @param endDegree Degree of last tick
	 * @param startTick When the animation should start
	 * @return Rotation degree in rad at the current tick.
	 */
	/*public static float animDegree(float defaultDegree, int ticker, float startDegree, float endDegree, int startTick, int duration, float partialTicks)
	{
		float endTick = startTick - duration;
		if(ticker>startTick || ticker<endTick||ticker<0)
			return defaultDegree;
		float m = (startDegree - endDegree)/duration;
		float c = endDegree-endTick*m;
		float rotationLastTick = c + m*Math.min(startTick, ticker+1);
		float rotationNow = c + m*ticker;
		float actualRotation = rotationLastTick + (rotationNow - rotationLastTick) * (partialTicks);
		return actualRotation;
	}*/
	
	/**
	 * Basic linear animation for an model part. 
	 * @param ticker Ticker for the animation. Ticker should count up
	 * @param startAngle Degree of first tick
	 * @param endAngle Degree of last tick
	 * @param startTick When the animation should start
	 * @param degree If start and endAngle is in degree
	 * @return Rotation degree in rad at the current tick.
	 */
	public static float animAngle(float defaultAngleInRad, int ticker, float startAngle, float endAngle, int startTick, int duration, float partialTicks, boolean degree)
	{
		if(degree)
			return anim(defaultAngleInRad, ticker, degToRad(startAngle), degToRad(endAngle), startTick, duration, partialTicks);
		return anim(defaultAngleInRad, ticker, startAngle, endAngle, startTick, duration, partialTicks);
	}
	
	public static float stayAtAngle(float defaultAngleInRad, int ticker,  float angle, int startTick, int duration, boolean degree)
	{
		if(ticker<startTick || ticker>duration+startTick)
			return defaultAngleInRad;
		return degree?degToRad(angle):angle;
	}
	
	public static float anim(float defaultAngle, int ticker, float[] angles, int startTick, float partialTicks)
	{
		float endTick = startTick - angles.length;
		if(ticker>startTick||ticker<endTick||ticker<0)
			return defaultAngle;
		return angles[ticker-startTick];
	}
}
