package com.dell.marsover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import com.dell.marsover.universe.Coordinates;
import com.dell.marsover.universe.Direction;
import com.dell.marsover.universe.Plateau;

@SpringBootApplication
public class MarsOverApplication {

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(MarsOverApplication.class, args);
		InputStream fileToRead = null;
		File file = null;
		
		if (args == null) {
			printSeparatorLines();
			printConsoleMessage(" .... no file provided as argument ... using the sample file !");
	
			fileToRead = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.txt");
				
		} else {
			if (args.length > 0) {
				file = ResourceUtils.getFile(args[0].toString());
				fileToRead = new FileInputStream(file);
			} else {
				printSeparatorLines();
				printConsoleMessage(" .... no file provided as argument ... using the sample file !");
				fileToRead = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.txt");
			}
		}		
				 	
		printSeparatorLines();
		
		if (file != null) {
			printConsoleMessage("Reading instructions of file: " + file.getName());
		} else {
			printConsoleMessage("Reading instructions of file sample.txt");
		}
		
		printSeparatorLines();
		
		//Read File Content
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileToRead));
		
		int linesOfInstructions = 1;
		int amountOfRobots = 1;
		String terrainCoordinates = "";
		
		printConsoleMessage(" .... Starting reading instructions !!! ....");
		printSeparatorLines();
		
		String line;
		
		Plateau terrain = null;
		
		boolean isCoordinateAndDirectionLoaded = false;
		
		int startAxisXPosition = 0;
	    int startAxisYPosition = 0;
	    String strDirection = "";
	    Direction startDirection = Direction.N;
	    
	    String moviments = "";
		
	    List<String> generatedOutput = new ArrayList<String>();
	    
		while ((line = reader.readLine()) != null) {
		    
			String instruction = line;			
		    
		    if (linesOfInstructions == 1 && terrainCoordinates.equals("")) {
				terrainCoordinates = instruction;
				
				int topRightX = Integer.parseInt(terrainCoordinates.substring(0, 1));
				int topRightY = Integer.parseInt(terrainCoordinates.substring(2));
				
				printConsoleMessage("Creating terrain with X,Y coordinates: X(" + topRightX + ") and Y(" + topRightY + ")");
				printSeparatorLines();
				
				terrain = new Plateau(topRightX, topRightY);
				
				++linesOfInstructions;
						
				continue;
			}
			
		    if (!isCoordinateAndDirectionLoaded) {
		    	startAxisXPosition = Integer.parseInt(instruction.substring(0, 1));
			    startAxisYPosition = Integer.parseInt(instruction.substring(2,3));
			    strDirection = instruction.substring(4);
		    	startDirection = getDirectionByString(strDirection);
		    	isCoordinateAndDirectionLoaded = true;
		    	++linesOfInstructions;
		    	
		    	printConsoleMessage("Initial Position and Direction: [ " + instruction + " ] for the robot(" + amountOfRobots + ")");
		    	printSeparatorLines();
		    	
		    	continue;
		    } else {
		    	
		    	moviments = instruction;
		    	++linesOfInstructions;
		    	
		    	printConsoleMessage("Moviments: [ " + instruction + " ] for the robot(" + amountOfRobots + ")");
		    	printSeparatorLines();
		    }
		    
			if (linesOfInstructions % 2 == 0) {
				Coordinates startingPosition = new Coordinates(startAxisXPosition, startAxisYPosition);
				
				MarsRover marsRover = new MarsRover(terrain, startDirection, startingPosition);
				
				isCoordinateAndDirectionLoaded = false;
				
				executeRoverMoviments(amountOfRobots, marsRover, moviments, generatedOutput);
				
				printSeparatorLines();
				printConsoleMessage("Finished reading robot " + amountOfRobots + " instructions !");
				printSeparatorLines();
				
				++amountOfRobots;
			}

		}
		
		printSeparatorLines();
		printConsoleMessage(" ....File with instructions finished !!! ....");
		
		printSeparatorLines();
		printConsoleMessage(">>> OUTPUT <<<");
		
		printConsoleMessage(printOutput(generatedOutput));
		
	}
	
	private static void executeRoverMoviments(int robotIdx, MarsRover rover, String moviments, List<String> generatedOutput) {
		for(int i=0; i < moviments.length(); i++) {
			String moviment = String.valueOf(moviments.charAt(i));
			System.out.println("Instruction executed : " + moviment);
			rover.run(moviment);
		}
		
		System.out.println("For the robot " + robotIdx + " -> Final position/location: " + rover.currentLocation());
		generatedOutput.add(rover.currentLocation());
	}
	
	private static void printSeparatorLines() {
		System.out.println("");
		System.out.println("");
	}
	
	private static void printConsoleMessage(String message) {
		System.out.println(message);
	}
	
	private static Direction getDirectionByString(String value) {
		if (value.equals("N")) {
			return Direction.N;
		} else if (value.equals("S")) {
			return Direction.S;
		} else if (value.equals("E")) {
			return Direction.E;
		} else {
			return Direction.W;
		}
	}
	
	private static String printOutput(List<String> output) {
		StringBuffer sb = new StringBuffer();
		for(String result : output) {
			sb.append(result);
			sb.append("\n");
		}
		return sb.toString();
	}

}
