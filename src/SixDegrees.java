import java.io.*;
import java.util.*;

public class SixDegrees {

  // ------------------------------
  // INSTANCE VARIABLE: people HashMap
  // ------------------------------
  // This HashMap stores the graph itself.
  // Each key is a String corresponding to an actor's name.
  // Each value is an ArrayList of PersonMovie objects.
  // This ArrayList is how we store the adjacency list for the current key.
  // Each PersonMovie object contains the name of an actor and a
  // movie that actor was in with the actor in the key.
  HashMap<String, ArrayList<PersonMovie>> people;


  // ------------------------------
  // INNER CLASS FOR ADJACENCY LIST
  // ------------------------------
  // The PersonMovie class stores an actor
  // and one movie that actor was in.
  public class PersonMovie {
    String person;
    String movie;

    public PersonMovie(String p, String m) {
      this.person = p;
      this.movie = m;
    }
  }

  // ------------------------------
  // CONSTRUCTOR
  // ------------------------------
  // Constructor - initializes the HashMap for storing the graph.
  public SixDegrees() {
    this.people = new HashMap<String, ArrayList<PersonMovie>>();
  }

  // ------------------------------
  // METHOD FOR READING IN IMDB
  // ------------------------------
  // This method reads in the IMDB file and creates the graph.
  // The IMDB file has the 999 most popular movies from 2006-2016.
  public void populateGraph(String s) throws IOException {

    // read in the file line by line
    BufferedReader br = new BufferedReader(new FileReader(s));
    String line;
    while ((line = br.readLine()) != null) {

      // split the line on tab
      String[] parts = line.split("\\t+");

      // the first element is the movie
      String movie = parts[0];

      // the second element is a comma-sepaated list of actors
      String[] actors = parts[1].split(",");

      // add an edge between each actor and every other actor
      for (int i = 0; i < actors.length-1; i++) {
        for (int j=i+1; j < actors.length; j++) {
          addEdge(actors[i], actors[j], movie);
        }
      }
    }
  }


  // -------------------------------------
  // METHODS FOR CREATING NODES AND EDGES
  // -------------------------------------
  // This method just creates a "node", i.e., an entry in the
  // HashMap with an actor as the key and an empty adjacency list.
  public void createNode(String a) {
    ArrayList<PersonMovie> ll = new ArrayList<PersonMovie>();
    people.put(a, ll);
  }

  // This method adds an edge to actor a's list to actor b in the selected movie.
  // And it adds an edge to actor b's list to actor a in the selected movie.
  public void addEdge(String a, String b, String movie) {

    // if neither actor is in the graph yet, create a vertex for them
    if (!people.containsKey(a)) {
      createNode(a);
    }
    if (!people.containsKey(b)) {
      createNode(b);
    }

    // get the current adjacency list for actor a, and add a new
    // PersonMovie object to it for person b and the selected movie
    ArrayList<PersonMovie> ll = people.get(a);
    PersonMovie pm = new PersonMovie(b, movie);
    ll.add(pm);
    people.put(a, ll);

    // get the current adjacency list for actor b, and add a new
    // PersonMovie object to it for person a and the selected movie
    ArrayList<PersonMovie> ll2 = people.get(b);
    pm = new PersonMovie(a, movie);
    ll2.add(pm);
    people.put(b, ll2);

  }


  // -------------------------------------
  // RANDOM WALK METHOD
  // -------------------------------------
  // This method performs a random walk of length=steps,
  // starting at actor a, and returns the final actor.
  public ArrayList<String> randomWalk(String a, int steps) {
    ArrayList<String> visitedActors = new ArrayList<String>();
    // while the number of steps is greater than 0
    while (steps > 0) {
      steps--;

      // Get a random person from the adjacency list.
      ArrayList<PersonMovie> al = people.get(a);
      int rando = (int)(Math.random() * al.size());
      PersonMovie pm = al.get(rando);

      // Print out the relationship.
      visitedActors.add(pm.person);

      // Move on to that person, and keep walking.
      a = pm.person;
    }
    return visitedActors;
  }

  // --------------------------------
  //     YOUR METHODS GO HERE!!!
  // --------------------------------

  // TASK 1: print out the actor(s) with the maximum degree
  // signature: public void maxDegree()
  // Instructions are in the problem set.
  public void maxDegree() {
    int max = 0; // initialized max
    String maxActors = ""; // initializing String for actors with max degree
    for (String actor : people.keySet()) {
      ArrayList<PersonMovie> alist = people.get(actor);
      int degree = alist.size();
      if (degree > max) {
        max = degree;
      } // this loops through each actor in the HashMap and finds the max degree
    }
    for (String actor : people.keySet()) {
      ArrayList<PersonMovie> alist = people.get(actor);
      int degree = alist.size();
      if (degree == max) {
        maxActors = maxActors + " " + actor;
      } // this repeats the loop, adding each actor with the max degree to the String
    }
    System.out.println("The actor(s) with the maximum degree are: " + maxActors);
  }
  // TASK 1: print out the actor(s) with the minimum degree
  // signature: public void minDegree()
  // Instructions are in the problem set.
  public void minDegree() {
    int min = 100; // I initialized min with an arbitrary large number
    String minActors = ""; // initializing String for actors with min degree
    for (String actor : people.keySet()) {
      ArrayList<PersonMovie> alist = people.get(actor);
      int degree = alist.size();
      if (degree < min) {
        min = degree;
      } // this loops through each actor in the HashMap and finds the min degree
    }
    for (String actor : people.keySet()) {
      ArrayList<PersonMovie> alist = people.get(actor);
      int degree = alist.size();
      if (degree == min) {
        minActors = minActors + " " + actor;
      } // this repeats the loop, adding each actor with the min degree to the String
    }
    System.out.println("The actor(s) with the minimum degree are: " + minActors);
  }

  public void smallDegree() {
    int min = 3;
    String minActors = "";
    for (String actor : people.keySet()) {
      ArrayList<PersonMovie> alist = people.get(actor);
      int degree = alist.size();
      if (degree <= min) {
        minActors = minActors + actor + ", ";
      } // this loops through each actor in the HashMap and finds the min degree
    }
    System.out.println("The actor(s) with the degree<=3 are: " + minActors);
  }

  // TASK 2: print out the 5 most popular actors based on number of visits in a random walk
  // signature: public void mostPopular()
  // Instructions are in the problem set.
  // Note: this code will pick a random actor from the people HashMap and save it to key.
  //       ArrayList<String> allActors = new ArrayList<String>(people.keySet());
  //       String key = allActors.get(new Random().nextInt(allActors.size()));
  public void mostPopular() {
    HashMap<String, Integer> tracker = new HashMap<String, Integer>();
    // initializes HashMap for keeping track
    for (int i = 0; i < 10000; i++) { // loops through 10,000 times
      ArrayList<String> allActors = new ArrayList<String>(people.keySet());
      String key = allActors.get(new Random().nextInt(allActors.size()));
      // walks through starting with random actor
      ArrayList<String> visited = randomWalk(key, 100);
      // takes 100 steps, creating ArrayList of visited actors
      for (String actor : visited) {
        if (!tracker.containsKey(actor)) {
          tracker.put(actor, 1);
        }
        else {
          int count = tracker.get(actor);
          count++;
          tracker.put(actor, count);
        } // keeps count of how many times each actor was visited
      }
    }
    String topFive = "";
    for (int i = 0; i < 5; i++) { // loops through 5 times to find top five
      int max = 0; // initializes max number of appearances
      String topActor = "";
      for (String actor : tracker.keySet()) {
        int count = tracker.get(actor);
        if (count > max) {
          max = count;
        } // finds the max number of appearances
      }
      for (String actor : tracker.keySet()) {
        int count = tracker.get(actor);
        if (count == max) {
          topActor = actor;
          topFive = topFive + topActor + ", ";
        } // finds the actor with the max appearances and adds him/her to String
      }
      tracker.remove(topActor); // this removes the top actor so I can find the next one
    }
    System.out.println("The five most popular actors are: " + topFive);
  }
  // TASK 3: Find the shortest path between two actors using breadth-first search.
  // You need to print out the full path from actor a to actor b.
  // You also need to keep track of the length of the path.
  // The algorithm is set out for you below.
    public void findShortestPath(String a, String b) {

    // For each actor you encounter, keep track of how you got there
    // (i.e., from which PersonMovie, i.e., which other actor and what movie).
    // You also use this to keep track of which actors you have visited.
    HashMap<String,PersonMovie> camefrom = new HashMap<String,PersonMovie>();

    // In BFS, you use a queue. I know this is confusing, but the appropriate
    // implementation of the Queue interface in Java is called LinkedList.
    LinkedList<String> queue = new LinkedList<String>();

    // YOUR CODE GOES HERE!
    // Start by adding the starting actor, a, to the queue.
    queue.add(a);
    camefrom.put(a, null);
    // While the queue is not empty
    while (!queue.isEmpty()) {
      String current = queue.poll();
      // poll() off the actor at the front of the queue.
      ArrayList<PersonMovie> alist = people.get(current);
      // Get the adjacency list for that actor.
      for (PersonMovie pmovie : alist) { // For each PersonMovie in the adjacency list...
        if (pmovie.person.equals(b)) { // If the actor in the PersonMovie is the actor, b, you are looking for.
          int length = 0;
          while (pmovie.person != a) {
            System.out.println("We got to "  + pmovie.person + " from " + current + ".");
            pmovie.person = current;
            PersonMovie newpmovie = camefrom.get(current);
            length++;
            try {
              current = newpmovie.person;
            }
            catch(Exception e) {
              System.out.println("The length of path is " + length);
              return;
            }
          } // If it is, you are ready to print out the path that took you here.
          // You must also print out its length.
          // Use the camefrom variable to help you do this.
          // Don't forget to return!!!
          return;
        }
        else { // Otherwise...
          if (!queue.contains(pmovie.person) && !camefrom.containsKey(pmovie.person)) {
            // If that actor has already been visited or is in the queue...
            queue.add(pmovie.person); // Add that actor to the queue.
            PersonMovie newpmovie = new PersonMovie(current, pmovie.movie);
            camefrom.put(pmovie.person, newpmovie);
            // And add that actor to camefrom with the current actor
            // and the movie that they were in together as the value.
          }
        }
      }
    }
    System.out.println("No path found.");
    return;
    // If you end up with an empty queue and no match, there was no path.
  }


  // --------------------------------
  // MAIN METHOD
  // --------------------------------
  public static void main (String[] args) throws IOException {
    SixDegrees sd = new SixDegrees();
    sd.populateGraph(args[0]);
    sd.randomWalk("Kevin Bacon", 5);

    // -------------------------------------------
    // UNCOMMENT THESE TO TEST YOUR IMPLEMENTATION
    // -------------------------------------------
    sd.maxDegree();
    sd.minDegree();
    sd.mostPopular();
    sd.findShortestPath("Pablo Schreiber", "Sarah Clarke");

    // ---------------------------
    // CODE FOR TASK 4 GOES HERE!
    // ---------------------------
    //sd.smallDegree();
    sd.findShortestPath("Storm Acheche Sahlstrom", "Elizabeth Reaser");
    // Using the smallDegree method, I came up with a list of actors
    // with degree <= 3. By randomly plugging in actors, I found a max
    // path length of 7, as shown.
  }

}
