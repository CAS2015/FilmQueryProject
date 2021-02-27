package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
//    app.test();
    app.launch();
  }

//  private void test() {
//    Film film = db.findFilmById(1);
//    System.out.println(film);
//  }

  private void launch() {
    Scanner input = new Scanner(System.in);
    
    startUserInterface(input);
    
    input.close();
  }

  private void startUserInterface(Scanner input) {
	boolean keepGoing = true;
	String[] mainMenu = {"Look up a film by ID number", "Look up a film by keyword", "Exit"};
	
    System.out.println("Welcome to the Film Depot");
   
    while(keepGoing) {
    	printMenu(mainMenu);
    	int selection = getUserMenuSelection(input, mainMenu.length);
    	keepGoing = completeUserSelection(input, selection);
  
    }
    
  }

private boolean completeUserSelection(Scanner input, int selection) {
	boolean keepGoing = true;
	switch (selection) {
	case 1:
		filmLookupById(input);
		break;
	case 2:
		filmLookupByKeyword(input);
		break;
	case 3:
		System.out.println("Goodbye!");
		keepGoing = false;
	}
	return keepGoing;
}

private void filmLookupByKeyword(Scanner input) {
	System.out.print("Please enter the word you'd like to search by ");
	String selection = input.nextLine();
	List<Film> films = db.findFilmByKeyword(selection);
	if(films.isEmpty()==false) {
		for (Film film: films) {
			System.out.println(film);
			printActorsList(film);
		}
	}
	else {
		System.out.println("No film(s) found with the keyword of " + selection);
	}
}

private void printActorsList(Film film) {
	System.out.println("Actors in " + film.getTitle() + ": ");
	for (Actor actor : film.getActorsList()) {
		System.out.println(actor);
	}
}

private void filmLookupById(Scanner input) {
	int selection = -1;
	boolean validInput = false;
	System.out.print("Please enter the id number of the film you'd like to view " );
	
	while(!validInput) {
	try {
		selection = Integer.parseInt(input.nextLine());
		validInput = true;
	} catch (Exception e) {
		System.out.println("Invalid input! Please type in a number.");
	}
	}
	
	Film film = db.findFilmById(selection);
	if(film != null) {
	System.out.println(film);
	printActorsList(film);
	}
	else {
		System.out.println("No film found with an id of " + selection);
	}
}

private int getUserMenuSelection(Scanner input, int numOfChoices) {
	int selection;
	try {
		selection = Integer.parseInt(input.nextLine());
		if (!(selection > 0 && selection <= numOfChoices)) {
			throw new Exception();
		}

	} catch (Exception e) {
		System.out.println("Invalid input! Please select a number 1-" + numOfChoices);
		selection = getUserMenuSelection(input, numOfChoices);
	}
	return selection;
}

private void printMenu(String[] menu) {
	int i = 1;
	System.out.println("\nMenu");
	for (String option : menu) {
		System.out.println(i + ") " + option);
		i++;
	}
	System.out.print("What would you like to do? ");
}

}
