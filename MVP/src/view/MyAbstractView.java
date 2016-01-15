package view;

import java.awt.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms.search.State;

public abstract class MyAbstractView extends Observable implements View, Observer {

	protected String commandRegex;
	protected String[] userCommand;
	protected CLI client;
    protected MazeDisplayer mazeDisplayer;
	protected MazeWindow mazeWindow;
	
	public MyAbstractView() {
	}

	public void setBasicWindow(MazeWindow mazeWindow) {
		this.mazeWindow = mazeWindow;
	}

	/**
	 * start method
	 */
	public void start() {
		this.client.start();
	}

	public void setUserCommand(String[] userCommnd) {
		
		this.userCommand = userCommnd;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see view.View#getUserCommand(java.lang.String[])
	 */
	@Override
	public String[] getUserCommand() {

		return this.userCommand;
	}

	@Override
	public void setCommandRegex(String commandRegex) {
		this.commandRegex = commandRegex;

	}

	public String getCommandRegex() {
		return commandRegex;
	}

	/**
	 * @param client
	 */
	public void setClient(CLI cli) {
		this.client = cli;
	}

	/**
	 * ShutDown method
	 */
	public void shutdown() {
	}

	/**
	 * @param run
	 */
	abstract public void startRunable(Runnable run);

	/*
	 * (non-Javadoc)
	 * 
	 * @see view.View#sendDisplySolution(java.lang.String[])
	 */
	@Override
	abstract public void sendDisplySolution(String[] solution);

	@Override
	public void update(Observable o, Object arg) {
		if (o == client) {
             //System.out.println(client.commandsList.get(0)[0]);
			setCommandRegex(client.commandsList.get(0)[0]);
			setUserCommand(client.commandsList.get(1));
			setChanged();
            notifyObservers();
		}
		if(o == mazeWindow){
			setCommandRegex(mazeWindow.commandsList.get(0)[0]);
			setUserCommand(mazeWindow.commandsList.get(1));
			
			setChanged();
            notifyObservers();
		}

	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void DisplySolution(Object arg) {
/*		if (mazeDisplayer == null)
			mazeDisplayer = new Maze3D(mazeWindow.shell, SWT.BORDER);*/
		    //mazeDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		mazeWindow.mazePainter.setCanvas(arg);
		
	}

	@Override
	public void exit() {
		mazeWindow.mazePainter.dispose();
		mazeWindow.shell.dispose();
		
	}
	
			
		
		//mazeWindow.paintConsole();
	}





