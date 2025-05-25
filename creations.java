package create;
import java.lang.Character;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.util.*;
import java.io.*;
//todo: implement n player scalability (probablility of this happening is almost 0)

public class creations {
	static PrintStream Player1TextField;
	static PrintStream Player2TextField;
	static Scanner KB = new Scanner(System.in);
	static Random Dealer = new Random();
	static HashMap<String, String> HandRules = new HashMap<>();
	static char[] card_numb = {'A','B','C','D','E','F','G','H','I','J','K','L','M'};
	static char[] card_type = {'A','B','C','D'};
	static int player1points =0;
	static int player2points =0;
	static int p1tempoints =0;
	static int p2tempoints =0;
	static ArrayList<String> CardsHandedOut = new ArrayList<>();
	static ArrayList<String> Player1Cards = new ArrayList<>();
	static ArrayList<String> Player2Cards = new ArrayList<>();
	static ArrayList<String> CardsOnTable = new ArrayList<>();
	public static void main(String[] args) throws FileNotFoundException {
		while(player1points < 15 && player2points <15) {
			HashMapInitializer();
			Player1TextField = new PrintStream(new File("Player1.txt"));
			Player2TextField = new PrintStream(new File("Player2.txt"));
			System.out.println("Hit Enter to Start");
			Round();
			CardsHandedOut.clear();
			Player1Cards.clear();
			Player2Cards.clear();
			CardsOnTable.clear();
			System.out.println("Press 'N' to quit");
			String response = KB.next();
			if (response.equalsIgnoreCase("N")) {
				break;
			}
		}
	}
	
	
	public static void Round()  {
		
		for (int a = 1;a<=4;a++) { //This For loop generates cards for player 1 and 2
			if(a%2 == 0) {
				Player1Cards.add(CardGenerator());
			}else {
				Player2Cards.add(CardGenerator());
			}
		}
		for (int a = 1;a<=3;a++) { // This one generates cards for the table. Ideally this will start at 3, then add 1 then another 1
			CardsOnTable.add(CardGenerator());
		}
		PointsAsker(0);

		System.out.println("Is Player 1 Ready to see their hand? Type \"ready\" \n");
		TruthChecker();
		for (int a = 0;a<Player1Cards.size();a++) {
			OutPutWithLogic(a, true, false, false,Player1Cards);
		}
		System.out.println("\nIs Player 2 Ready to see their hand? Type \"ready\" \n");
		TruthChecker();
		for (int a = 0;a<Player2Cards.size();a++) {
			OutPutWithLogic(a, false, true, false,Player2Cards);
		}
		PointsAsker(1);
		System.out.println("\nAre both players ready to see the first 3 cards on the table (the flop)? Type \"ready\" \n");
		TruthChecker();
		for (int a = 0;a<CardsOnTable.size();a++) {
			OutPutWithLogic(a, true, true, true,CardsOnTable);
		}
		PointsAsker(2);
		CardsOnTable.add(CardGenerator());
		System.out.println("\nAre both players ready to see the fourth card on the table (the turn)? You know the jist. \n");
		TruthChecker();
		OutPutWithLogic(CardsOnTable.size()-1, true, true, true,CardsOnTable);
		//PointsAsker(3);
		CardsOnTable.add(CardGenerator());
		System.out.println("\nAre both players ready to see the fifth card on the table (the river)? You know what to say... \n");
		TruthChecker();
		OutPutWithLogic(CardsOnTable.size()-1, true, true, true,CardsOnTable);
		//
		PointsAsker(4);

		for (int a = 0; a<CardsOnTable.size();a++) {
			Player1Cards.add(CardsOnTable.get(a));
		}
		for (int a = 0; a<CardsOnTable.size();a++) {
			Player2Cards.add(CardsOnTable.get(a));
		}
		Collections.sort(Player1Cards);
		Collections.sort(Player2Cards);
		ResultsOutputter(Player1Cards,Player2Cards);
	}
	
	public static void OutPutWithLogic (int h, boolean player1check, boolean player2check,
			boolean ConsoleCheck, ArrayList <String> Array) {
		// This simplifies the coding mess by putting it in its own method. Takes 5 variables
		if (player1check) {
			Player1TextField.println("\n"+CardNumbChecker(Array.get(h).charAt(0))+
					" of "+CardTypeChecker(CardsHandedOut.get(h).charAt(1)));
		}
		if (player2check) {
			Player2TextField.println("\n"+CardNumbChecker(Array.get(h).charAt(0))+
					" of "+CardTypeChecker(CardsHandedOut.get(h).charAt(1)));
		}
		if (ConsoleCheck) {
			System.out.println("\n"+CardNumbChecker(Array.get(h).charAt(0))+
					" of "+CardTypeChecker(CardsHandedOut.get(h).charAt(1)));
		}
	}
	
	
	public static String CardNumbChecker(char card_letter) {
		String[] numb_names = {
				"Two","Three","Four","Five","Six",
				"Seven","Eight","Nine","Ten",
				"Jack","Queen","King","Ace"};
		return numb_names[card_letter-65];
	}
	
	public static String CardTypeChecker(char card_letter) {
		String[] numb_names = {"Spades","Clovers","Diamonds","Hearts"};
		return numb_names[card_letter-65];
	}

	public static String CardGenerator() {
		// (seems to be 99% reliable)
		String my_new_card;
		my_new_card = card_numb[Dealer.nextInt(card_numb.length)]+
				""+card_type[Dealer.nextInt(card_type.length)];
		for (int j = 0; j<CardsHandedOut.size();j++) {
			if((my_new_card.equals(CardsHandedOut.get(j)))) {
				
			my_new_card = card_numb[Dealer.nextInt(card_numb.length)]+
					""+card_type[Dealer.nextInt(card_numb.length)];
			j=0;
			}
		}
		CardsHandedOut.add(my_new_card);
		return my_new_card;
	}
	
	public static boolean TruthChecker() {
		// all this does is repeatedly check if the person typed ready, and repeat until he does
		String Typed= KB.next();
		while(!Typed.equalsIgnoreCase("Ready")) {
			System.out.println("You did not type the magic word!");
			Typed= KB.next();
		}
		return true;
	}
	
	public static ArrayList<String> StraightChecker(ArrayList<String> HandWTable){
		/* 1 bug with this code is that if the straight starts is  
		*  Ace,2,3,4,5 it will not be able to detect it
		*  Unless I code that as something specific to tell the code to look out for
		*  something i reckon would be a nightmare
		*  I have no way of making it work
		*  Another bug is that there is a "null" that gets printed out and idk how exactly
		*  can be filtered out later so it isn't an issue, just frustrating
		*/ 
		ArrayList<String> result = new ArrayList<>();
			for (int i =0 ; i < HandWTable.size(); i++) {
				for (int j = HandWTable.size()-1; j>i; j--) {
					if ((HandWTable.get(i).charAt(0) == HandWTable.get(j).charAt(0)) &&
						(HandWTable.get(i).charAt(1) != HandWTable.get(j).charAt(1))) {
						HandWTable.remove(j);
						j = HandWTable.size(); i = 0;
					}
				}
			}
			if(HandWTable.size() < 5) {
				result.add("null");
			}else {
				for (int i =0 ; i < HandWTable.size()-4; i++) {
					if (((HandWTable.get(i).charAt(0) == (HandWTable.get(i+1).charAt(0)-1)))&&
						((HandWTable.get(i).charAt(0) == (HandWTable.get(i+2).charAt(0)-2)))&&
						((HandWTable.get(i).charAt(0) == (HandWTable.get(i+3).charAt(0)-3)))&&
						((HandWTable.get(i).charAt(0) == (HandWTable.get(i+4).charAt(0)-4)))) {
						for (int k = 0; k<=i+4; k++) {
							result.add(HandWTable.get(k));
						}
					} else {
						result.add("null");
						break;
					}
				}
			}
			return result;
		}
	/*  Thanks Rabbit!   */
	public static ArrayList<String> FlushChecker(ArrayList<String> HandWTable) {
		ArrayList<String> SpadesFound = new ArrayList<>();
		ArrayList<String> CloversFound = new ArrayList<>();
		ArrayList<String> DiamondsFound = new ArrayList<>();
		ArrayList<String> HeartsFound = new ArrayList<>();
		ArrayList<String> NoneFound =new ArrayList<>();
		NoneFound.add("None");
		for (int i = 0; i<HandWTable.size();i++) {		
			switch (HandWTable.get(i).charAt(1)) {
			case ('A'):
				SpadesFound.add(Character.toString(HandWTable.get(i).charAt(0)));
				break;
			case ('B'):
				CloversFound.add(Character.toString(HandWTable.get(i).charAt(0)));
				break;
			case ('C'):
				DiamondsFound.add(Character.toString(HandWTable.get(i).charAt(0)));
				break;
			case ('D'):
				HeartsFound.add(Character.toString(HandWTable.get(i).charAt(0)));
				break;
			}
		}
		
		if(SpadesFound.size() == 5 || CloversFound.size() == 5 ||
				DiamondsFound.size() == 5 || HeartsFound.size() == 5) {
			// maybe this could be turned into a switch case?
			if(SpadesFound.size() == 5) {
				SpadesFound.add("A");
				return SpadesFound;
			}else if(CloversFound.size() == 5) {
				CloversFound.add("B");
				return CloversFound;
			}else if(DiamondsFound.size() == 5) {
				DiamondsFound.add("C");
				return DiamondsFound;
			}else {
				HeartsFound.add("D");
				return HeartsFound;
			}
		} else {
			return NoneFound;
		}
	}
	
	public static ArrayList<String> GroupChecker(ArrayList<String> HandWTable) {
		/* This mess checks for every other type of group
		 *  pair, trips and quads.
		 *  Every even char is the type of grouping, the odd is the rank
		 *  Full House, and two pairs are found by parsing the resulting list in a different method 
		 */
		ArrayList<String> GroupsFound = new ArrayList<>();
		ArrayList<Character> CompressedDeck = new ArrayList<>();
		char IteratorRank = HandWTable.get(0).charAt(0);
		int IteratorSuite = 1;
		HandWTable.add("Z"); // this compensates for the logic below
		for (int i = 1; i< HandWTable.size(); i++ ) {
			if (IteratorRank == HandWTable.get(i).charAt(0)) {
				IteratorSuite++;
			} else {
				CompressedDeck.add(IteratorRank);
				CompressedDeck.add((char)(IteratorSuite)); 
				// using debug ASCII code for number encoding is risky but oh well
				IteratorSuite = 1;
				IteratorRank = HandWTable.get(i).charAt(0);
			}
		}
		for (int i = 1; i< CompressedDeck.size(); i++ ) {
			if(CompressedDeck.get(i) > 1) {
				switch (CompressedDeck.get(i)) {
				case (4):
					for (int j = 0; j< HandWTable.size(); j++) {
						if (CompressedDeck.get(i-1) == HandWTable.get(j).charAt(0)) {
							GroupsFound.add("Q");
							GroupsFound.add(CompressedDeck.get(i-1)+"");
							j+=3;
						}
					}
					break;
				case (3):
					for (int j = 0; j< HandWTable.size(); j++) {
						if (CompressedDeck.get(i-1) == HandWTable.get(j).charAt(0)) {
							GroupsFound.add("T");
							GroupsFound.add(CompressedDeck.get(i-1)+"");
							j+=2;
					}
				}
					break;
				case (2):
					for (int j = 0; j< HandWTable.size(); j++) {
						if (CompressedDeck.get(i-1) == HandWTable.get(j).charAt(0)) {
							GroupsFound.add("P");
							GroupsFound.add(CompressedDeck.get(i-1)+"");
							j++;
						}
					}
					break;
				
				}
			}
		}
		return GroupsFound;
			
		
	}
	public static String[] HandChecker (ArrayList<String> HandWTable) {
		/* This mess is the master function that actually calls all the other card rank functions 
		 * it parses through it all, and ranks them, based on the ranking, it returns the type
		 * of deck along with the strongest card
		 * high card is handled inside this function, as a last resort if
		 * no match is found
		 * 
		 */
		ArrayList <String> Result = new ArrayList<> (GroupChecker(HandWTable));
		int[] Ranking = new int[5];
		char[] Ranking_Char = new char[5];
		for (int i = 0; i< Result.size() ; i+=2) {
			switch (Result.get(i)) {
			case ("Q"):
				Ranking[0] ++;
				Ranking_Char[0] =  Result.get(i+1).charAt(0);
				break;
			case ("T"):
				Ranking[1] ++;
				Ranking_Char[1] =  Result.get(i+1).charAt(0);
				break;
			case("P"):
				Ranking[2] ++;
				Ranking_Char[2] =  Result.get(i+1).charAt(0);
				break;
			}
		}
		ArrayList <String> Flush_Result = new ArrayList<> ();
		if (Flush_Result.size() >1) {
			Ranking[3] ++;
			Ranking_Char[3] = Flush_Result.get(Flush_Result.size()-2).charAt(0);
		}
		ArrayList <String> Straight_Result = new ArrayList<> (StraightChecker(HandWTable));
		if (Straight_Result.size() >1) {
			Ranking[4] ++;
			Ranking_Char[4] = Straight_Result.get(Straight_Result.size()-2).charAt(0);
		}
		/* This is the ONLY way I figured to handle ranking results.
		 A hack job I am not proud 
		 laughter is mandatory
		 */
		int RankingResult;
		char CharFinal; //yeah IK two named variables that look the same, they serve different purpose
		if(Ranking[3] ==1 && Ranking[4] ==1 && Ranking_Char[4] == 'K') {
			RankingResult = 0;
			CharFinal =Ranking_Char[4];
		} else if (Ranking[3] ==1 && Ranking[4] ==1) {
			RankingResult = 1;CharFinal =Ranking_Char[4];
		} else if (Ranking[0] >1) {
			RankingResult = 2;CharFinal =Ranking_Char[0];
		} else if(Ranking[1] > 1 && Ranking[2] > 1) {
			RankingResult = 3;
			if(Ranking_Char[1] >= Ranking_Char[2]) {
				CharFinal =Ranking_Char[1];
			} else {
				CharFinal =Ranking_Char[2];
			}
		} else if(Ranking[3] ==1) {
			RankingResult = 4;
			CharFinal =Ranking_Char[3];
		} else if(Ranking[4] == 1) {
			RankingResult = 5;
			CharFinal =Ranking_Char[4];
		} else if (Ranking[1] >1) {
			RankingResult = 6;
			CharFinal =Ranking_Char[1];
		} else if( Ranking[2] >=2) {
			RankingResult = 7;
			CharFinal =Ranking_Char[2];
		} else if(Ranking[2] == 1) {
			RankingResult = 8;
			CharFinal =Ranking_Char[2];
		} else {
			RankingResult = 9;
			CharFinal =HandWTable.get(HandWTable.size()-1).charAt(0);
		}
		 String[] FinalResult = { Integer.toString(RankingResult),Character.toString(CharFinal)};
		 return FinalResult;
	}
	public static void HashMapInitializer() {
		// to be initialized at the start of the game
		// this hashmap is a rank of all hand rules of poker. Smaller number is stronger
		HandRules.put("0", "Royal Flush");
		HandRules.put("1", "Straight Flush");
		HandRules.put("2", "Four of a Kind");
		HandRules.put("3", "Full House");
		HandRules.put("4", "Flush");
		HandRules.put("5", "Straight");
		HandRules.put("6", "Three of a Kind");
		HandRules.put("7", "Two Pair");
		HandRules.put("8", "Pair");
		HandRules.put("9", "High Card");
	}
	public static void ResultsOutputter(ArrayList<String> Player1Array, ArrayList<String> Player2Array) {
		/* this actually figures out if Player 1 or 2 won, or if it was a draw
		 draw: no points awarded
		 whoever wins gets the amount of points depending on the stage
		 whoever loses gets their betted points deducted from total score
		 this should be a good workaround against people being too confident
		*/
		String[] P1R = HandChecker(Player1Array);
		String[] P2R = HandChecker(Player2Array);
		String WinnerDeclaration;
		if (P1R[0].equals(P2R[0])){
			if (P1R[1].charAt(0) == P2R[1].charAt(0)) {
				WinnerDeclaration = "Draw! No points awarded (Suprizingly rare!)";
				Player1TextField.println(WinnerDeclaration);
				Player2TextField.println(WinnerDeclaration);
			}
			else {
				System.out.println("While both players had a "+HandRules.get(P1R[0]));
				if(P1R[1].charAt(0) < P2R[1].charAt(0)) {
					WinnerDeclaration = "Player 1 Wins wins with a " + CardNumbChecker(P1R[1].charAt(0))+ " against Player 2 with his puny "+ CardNumbChecker(P2R[1].charAt(0));
					Player1TextField.println("You WON!");
					Player2TextField.println(WinnerDeclaration);
					player1points += p1tempoints;
					player2points -=p2tempoints;
				}
				else {
					WinnerDeclaration = "Player 2 Wins wins with a " + CardNumbChecker(P2R[1].charAt(0))+ " against Player 1 with his puny "+ CardNumbChecker(P1R[1].charAt(0));
					Player1TextField.println(WinnerDeclaration);
					Player2TextField.println("You WON!");
					player2points += p2tempoints;
					player1points -= p1tempoints;
				}
			}
		} else {
			if(P1R[0].charAt(0) < P2R[0].charAt(0)) {
				WinnerDeclaration = "Player 1 Wins wins with a " + HandRules.get(P1R[0])+ " against Player 2 with his puny "+ HandRules.get(P2R[0]);
				Player1TextField.println("You WON!");
				Player2TextField.println(WinnerDeclaration);
				player1points += p1tempoints;
				player2points -= p2tempoints;

			} else {
				WinnerDeclaration = "Player 2 Wins wins with a " + HandRules.get(P2R[0])+ " against Player 1 with his puny "+ HandRules.get(P1R[0]);
				Player2TextField.println("You WON!");
				Player1TextField.println(WinnerDeclaration);
				player2points += p2tempoints;
				player1points -= p1tempoints;
			}
			
		}
		p1tempoints =0; p2tempoints =0;
		System.out.println();
		System.out.println(WinnerDeclaration);
		System.out.println("P1 Points: "+player1points+" P2 Points: "+player2points);
	}
	public static void PointsAsker(int stage_n) {
		/*  This code handles points distriution
		 *  Basically, it checks if each player has been asked
		 *  if they already said yes, it doesn't do anything (ideally)
		 *  
		 */
		String answer;
		if(p1tempoints ==0) {
			//if statement added to make sure it doesn't ask again if they already said yes
			String voided = KB.nextLine(); //getting around the common scanner bug
			System.out.println("Does Player 1 think they can win? Type y ");
			answer = KB.nextLine();
			if(answer.equalsIgnoreCase("y")){
				System.out.println("Player 1 thinks they can win on Stage "+ stage_n +" They can win " +(5-stage_n)+" points");
				p1tempoints = (5-stage_n);
			}
		}
		if(p2tempoints ==0) {
			String voided = KB.nextLine(); //mhm same again
			System.out.println("Does Player 2 think they can win? Type y ");
			
			answer = KB.nextLine();
			if(answer.equalsIgnoreCase("y")){
				System.out.println("Player 2 thinks they can win on Stage "+ stage_n +" They can win " +(5-stage_n)+" points");
				p2tempoints = (5-stage_n);
			}
		}
	}
	
}