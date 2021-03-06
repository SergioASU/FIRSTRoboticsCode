//Button Class
//adds a button object to our code, which has a few handy methods that
//detects if the button was just pushed, if the button is pushed, and
//if the button is just released

//RECCOMENDED USAGE:
//in the main loop, the one that is consistently updated, call btnObjectName.update(), so that the
//data held by the buttonObject is accurate and current.
//when checking the values (with the get...() methods), check within the same loop, and same thread
//this is because the ifPressed and ifReleased values are reset when the update() method is called
//the ifPressed and ifReleased values are only legit after update is called, and the farther they 
//are, the less likely they are to be accurate

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.DigitalInput;

public class Button 
{
    DigitalInput btn = null;//create a var for the joystick object
	int btnChannel;//the var that corresponds to the specific button
	
	boolean isPressed = false; //is true if the button was just pressed
	boolean isHeld = false; //is true if the button is currently down
	boolean isReleased = false; //is true if the button was just released
	
	// Parameters:
	//    jPortTmp: the game pad port that the button is on
	//    btnPortTmp: the button number (or id, whatever you wanna call it)
    Button(int btnChannelTmp)
    {
        btn = new DigitalInput(btnChannelTmp);//open a new button on the designated channel
		btnChannel = btnChannelTmp;//assigns the global var to the temp var
    }
	//check the button for changes
	//it is recommended that you keep calling this in your main loop, so that the values for methods like "getPressed" are current and accurate
	public void update()
	{
		isPressed = false;//we just updated, so if the button is released, the below code will catch it. This just acts like a reset
		isReleased = false;//see above
		if(!btn.get() && isHeld == false) //if the button is pressed but our variable says it isn't (if the button was just pushed)
		{
			//code for if button is pushed
			isHeld = true;//say that the button is pressed
			isPressed = true;//say that the button was just pressed
			isReleased = false;//say that it wasn't just released
		}
		if(btn.get() && isHeld == true) //if the button isn't pressed but our variable says it is (if the button was just released)
		{
			//execute the code for if the button is released
			isHeld = false;//say that the button isn't pressed
			isPressed = false;//say that the button wasn't just pressed
			isReleased = true;//say that the button was just released
		}
	}
    
	//return true if the button was just released. Is only considered true once after "update" is called
    public boolean getPressed()
    {
        boolean tmp = isPressed;//make a temporary var, so we can return a value and change it at the same time
		isPressed = false;//set it to false, so we're not always considered released
		return tmp;
	}
	//return true if the button is currently being held down
	public boolean getHeld()
	{
		return isHeld;
	}
	//return true if the button was just released. Is only considered true once after "update" is called
	public boolean getReleased()
	{
		boolean tmp = isReleased;//make a temporary var, so we can return a value and change it at the same time
		isReleased = false;//set it to false, so we're not always considered released
		return tmp;
	}
	
	//return the channel of the button that we attached to
	public int getChannel()
	{
		return btnChannel;
	}
}


