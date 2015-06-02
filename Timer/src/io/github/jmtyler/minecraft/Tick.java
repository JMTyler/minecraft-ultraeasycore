package io.github.jmtyler.minecraft;

public class Tick {
	//Variable TickID created
	private int TickID;
	
	//Constructor, initialises TickID
	public Tick(int passedID){
		TickID = passedID;
	}
	
	//Checks if Tick is multiple of any number in passed list
	public boolean isMilestone(int[] multiples){
		for (int index = 0; index < multiples.length; index++){ //Iterates through list
			if (TickID % multiples[index] == 0){ //Checks if it is multiple
				return true; //If it is, returns true
			}
		}
		return false; //If it does not match any of the numbers, it returns false
	}
	
	//Returns the Tick's ID
	public int getTickID(){
		return TickID;
	}
}
