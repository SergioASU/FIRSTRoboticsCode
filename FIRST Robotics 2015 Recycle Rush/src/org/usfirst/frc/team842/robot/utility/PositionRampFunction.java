package org.usfirst.frc.team842.robot.utility;

public class PositionRampFunction 
{
	double tFinal;
	double tRamp;
	double xFinal;
	double vMax;
	double x1;
	double x2;
	double t1;
	double t2;
	double a;
	
	
	public PositionRampFunction(double totalTime, double rampTime, double finalPosition)
	{
		this.tFinal = totalTime;
		this.tRamp = rampTime;
		this.xFinal = finalPosition;
		
		if(tRamp * 2 <= tFinal)
		{
			vMax = xFinal / (tFinal - tRamp);
			t1 = tRamp;
			t2 = tFinal - tRamp;
			a = vMax/tRamp;
			x1 = .5 * tRamp*vMax;
			x2 = xFinal - x1;
		}
	}
	
	public double get(double time)
	{
		if(tRamp * 2 >= tFinal)
		{
			return 0;
		}
		else if(time < 0)
		{
			return 0;
		}
		
		else if(time > tFinal)
		{
			return xFinal;
		}
		
		else if(time >= t1 && time <= t2)
		{
			return (vMax * time) - (vMax*t1) + x1;
		}
		else if(time >= 0 && time < t1)
		{
			return .5 * a * time * time;
		}
		else if(time > t2 && time <= tFinal)
		{
			return xFinal - .5 * a * (tFinal - time) * (tFinal - time);
		}
		return 0;	
	}

}
