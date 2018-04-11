import com.mongodb.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Philosopher implements Runnable {
	Lock leftFork = new ReentrantLock();
	Lock rightFork = new ReentrantLock();
	String name;

	public Philosopher(Lock leftFork, Lock rightFork, String name) {
	    this.leftFork = leftFork;
	    this.rightFork = rightFork;
	    this.name = name; 
	}

	@Override
	public void run() {
		try {
	    think(name);
		eat(leftFork, rightFork, name); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void eat(Lock leftFork, Lock rightFork, String name) throws Exception{
	    leftFork.lock();
	    rightFork.lock();
	    try
	    {
	    MongoClient mongoClient = new MongoClient("localhost");
	    DB db = mongoClient.getDB( "mydb" );
	    DBCollection coll = db.getCollection("mycol");

	    System.out.println(name + " eating...");
	    BasicDBObject doc1 = new BasicDBObject(name , " eating...");
	    coll.insert(doc1);

		Thread.sleep(1000);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } 
	    finally{
		System.out.println(name + " done eating and now thinking...");
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB( "mydb" );
			DBCollection coll = db.getCollection("mycol");
			BasicDBObject doc2 = new BasicDBObject(name , " done eating and now thinking...");
			coll.insert(doc2);
		leftFork.unlock();
		rightFork.unlock(); 
	    }
	}

	public void think(String name) throws Exception{
		try
	    {
	    MongoClient mongoClient = new MongoClient("localhost");
	    DB db = mongoClient.getDB( "mydb" );
	    DBCollection coll = db.getCollection("mycol");
	    System.out.println(name + " thinking...");
	    BasicDBObject doc = new BasicDBObject(name , " thinking...");
	    coll.insert(doc);
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } 
	}
}