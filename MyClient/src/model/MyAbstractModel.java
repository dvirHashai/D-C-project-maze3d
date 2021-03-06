package model;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms.search.State;
import presenter.ClientProperties;
import presenter.PropertiesHandler;

public abstract class MyAbstractModel extends Observable implements Model, Closeable {

	/**
	 * data member HashMap to match every maze with he's name
	 */
	protected HashMap<String, Maze3d> mazeMap;

	/**
	 * data member to send our request to the server
	 */
	ArrayList<Object> commandLine;

	/**
	 * data member HashMap to match every maze with he's solution
	 */
	protected HashMap<String, ArrayList<State<Position>>> solutionMap;

	/**
	 * contain the data after change;
	 */

	/**
	 * socket data member to communicate with the sever
	 */
	Socket theServer;

	/**
	 * ObjectOutputStream data member to open the stream into the server
	 */
	ObjectOutputStream toServer;

	/**
	 * ObjectInputStream data member to open the stream into the client
	 */
	ObjectInputStream fromServer;

	/**
	 * Ip data member to know to address of our server
	 */
	String clientIp;

	/**
	 * port data member to know in which frequency we need to connect
	 */
	int port;

	/**
	 * String data member to save our maze in mazeMap and allow us to load save
	 * him
	 */
	String savedName;

	/**
	 * client properties data member to get our Ip , Port, and the num
	 */
	ClientProperties clientProperties;

	/**
	 * updateData to send message to the Command Line Interface
	 */
	protected String[] updateData;

	/**
	 * pool data member to manage our threads
	 */
	protected ExecutorService pool;

	/**
	 * 
	 */
	boolean close = false;

	/**
	 * timer data member to control the sendState method that send for us state
	 * members of solution on by one
	 */
	protected Timer timer;

	/**
	 * task data member to control the sendState method that send for us state
	 * members of solution on by one
	 */
	protected TimerTask task;

	// protected Future<Maze3dSearchable<Position>> futureMaze;

	public MyAbstractModel() throws UnknownHostException, IOException {
		try {
			clientProperties = PropertiesHandler.getInstance();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.port = clientProperties.getClientPort();
		this.clientIp = clientProperties.getClientIp();
		this.commandLine = new ArrayList<>();
		this.mazeMap = new HashMap<String, Maze3d>();
		this.solutionMap = new HashMap<String, ArrayList<State<Position>>>();
		this.pool = Executors.newFixedThreadPool(clientProperties.getNumOfThreads());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getDirPath(java.lang.String)
	 */
	@Override
	abstract public void getDirPath(String pathname);

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getGenrate3dMaze(java.lang.String, int, int, int)
	 */
	@Override
	abstract public void getGenrate3dMaze(String mazeName, int dim, int row, int col);

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getDisplayMaze(java.lang.String)
	 */
	@Override
	abstract public void getDisplayMaze(String mazeName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getDisplayCrossSection(java.lang.String, int,
	 * java.lang.String)
	 */
	@Override
	abstract public void getDisplayCrossSection(String cross, int index, String mazeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getSaveMaze(java.lang.String, java.lang.String)
	 */
	@Override
	abstract public void getSaveMaze(String mazeName, String fileName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getLodeMaze(java.lang.String, java.lang.String)
	 */
	@Override
	abstract public void getLoadeMaze(String fileName, String mazeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getMazeSize(java.lang.String)
	 */
	@Override
	abstract public void getMazeSize(String mazeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getFileSize(java.lang.String)
	 */
	@Override
	abstract public void getFileSize(String mazeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getSolveMaze(java.lang.String, java.lang.String)
	 */
	@Override
	abstract public void getSolveMaze(String mazeName) throws IOException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see Model.Model#getDisplaySolution(java.lang.String)
	 */
	@Override
	abstract public void getDisplaySolution(String mazeName);

	/**
	 * @return get the message that we send
	 */
	@Override
	public String[] getupdateData() {
		return this.updateData;

	}

	/**
	 * exit method to exit neatly from the program and close all the open
	 * threads and processes
	 */
	@Override
	public abstract void exit();

	/**
	 * @param solution
	 *            the sendState method that send for us state of solution on by
	 *            one from the beginning to the end
	 * 
	 */
	@Override
	public abstract void sendState(ArrayList<State<Position>> solution);

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
