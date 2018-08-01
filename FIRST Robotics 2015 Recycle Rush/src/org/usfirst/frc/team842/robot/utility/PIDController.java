package org.usfirst.frc.team842.robot.utility;

public class PIDController 
{
	public double kp;
	public double ki;
	public double kd;
	public double kf;
	public double target;
	public double lastError;
	public double lastTime;
	public double integralError;
	public boolean firstIteration = true;
	public double error;
	
	public PIDController(double p, double i, double d, double ff)
	{
		kp = p;
		ki = i;
		kd = d;
		kf = ff;
	}
	
	public PIDController(double p, double i, double d)
	{
		this(p,i,d,0);
	}
	
	public void setTarget(double target)
	{
		this.target = target;
	}
	
	public void start()
	{
		lastTime = System.currentTimeMillis() / 1000.0;
		lastError = 0;
		firstIteration = true;
	}
	
	public double iterate(double inputValue)
	{
		double currentTime = System.currentTimeMillis() / 1000.0;
		error = inputValue - target;
		double deltaTime = currentTime - lastTime;
		double deltaError = error - lastError;
		integralError += deltaTime * error;
		double derivativeError = 0;
		
		if(!firstIteration)
		{
			derivativeError =  deltaError / deltaTime;
		}
		
		firstIteration = false;
		lastTime = currentTime;
		lastError = error;
		return target * kf + error * kp + integralError * ki + derivativeError * kd;
	}
	
	public void resetIntegral()
	{
		integralError = 0;
	}
	
	public double getError()
	{
		return error;
	}
	
	public double getTarget()
	{
		return target;
	}
}