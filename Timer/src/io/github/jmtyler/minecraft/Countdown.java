package io.github.jmtyler.minecraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Countdown {
	public final static int SECOND = 20;
	public final static int MINUTE = 1200;
	public final static int HOUR = 72000;
	public final static int DAY = 1728000;
	
	//Create variables for the length of time, the size of the unit of time (how many server ticks one unit is) and the owning plugin.
	private int count;
	private int factor;
	protected Plugin owning;
	
	//Creates list for all tasks
	private List<Integer> taskList;
	
	//run Method runs the countdown with given parameters
	public void run(int c, int f, Plugin p){
		//Runs the onRun method, be it in this class or the subclass.
		this.onRun(c, f);
		//Sets the variables
		count = c;
		factor = f;
		owning = p;
		//Initialises taskList
		taskList = new ArrayList<Integer>();
		//Starts the countdown
		this.main();
	}
	
	//Method to stop the countdown and run onCancel
	public void cancel(){
		stop();
		this.onCancel();
	}
	
	public void extend(int time) {
		this.count += time;
	}
	
	//Method to stop the countdown and run onEnd
	private void end(){
		stop();
		this.onEnd();
	}
	
	//Cancels all tasks in the taskList (there should only be one, I think the fact it's a list is a relic from older versions. Possibly useful if pausing and starting the countdown becomes a thing.)
	private void stop(){
		for (int index = 0; index < taskList.size(); index++){
			Bukkit.getScheduler().cancelTask(taskList.get(index));
		}
		
	}
	
	//main Method runs countdown
	private void main(){
		//Creates repeating task and stores it in variable ID
		int ID = Bukkit.getScheduler().scheduleSyncRepeatingTask(owning, new Runnable(){

			@Override
			public void run() {
				boolean isfin = false; //Sets isfin variable to false every tick
				if (count == 0){ //Checks if it has ended
					isfin = true; //Sets isfin to true for safety
					end(); //Ends the countdown
				}
				
				if (isfin == false){ //Otherwise will do this
				    onTick(new Tick(count)); //Runs onTick with a new Tick object
					count--;  //Takes one from count before it runs again
				}
			}
			
		}, 0, factor);
		taskList.add(ID);
	}

	//Basic modules
	protected void onRun(int c, int f) {
	}
	protected void onTick(Tick t) {
	}
	protected void onEnd() {
	}
	protected void onCancel() {
	}
}
