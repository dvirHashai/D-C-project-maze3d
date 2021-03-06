package view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import presenter.ClientProperties;
import presenter.PropertiesHandler;

public class MazeWindow extends BasicWindow {
	KeyListener keyListener;
	MazeDisplayAdapter mazePainterAdapter;
	MazeDisplayer mazePainter;
	GenerateWindow generatewindow;
	MouseWheelListener mouseZoomlListener;
	ClientProperties clientProperties;
	MessageBox messageBox;
	Clip music;
	
	MenuItem exit;
	String mazeName;
	int p = 0;
	int counter = 0;
	

	public MazeDisplayer getMaze() {
		return mazePainter;
	}

	Timer timer;
	TimerTask task;

	/*
	 * public MazeDisplayer getMazeDisplayer(){ return mazePainter; }
	 */
	public MazeWindow(String title, int width, int height) {
		super(title, width, height);
		try {
			clientProperties = PropertiesHandler.getInstance();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMazePainter(MazeDisplayer mazePainter) {
		this.mazePainter = mazePainter;
	}

	public void setMazeDisplayAdapter(MazeDisplayAdapter mazeDisplayAdapter){
		this.mazePainterAdapter = mazeDisplayAdapter;
	}
	
	public String getFile(String file){
		if(file !=null){
		Character v = new Character('\\');
		int index= 0;
		int count1 = 0;
		int count =0;
		char[] c= new char[file.toCharArray().length];
		c = file.toCharArray();
		
		
		for (Character a : c) {
			if(a.equals('\\')){
				count++;
				
			}
		}
		for (int i = 0; i < c.length; i++) {
			if(v.equals(c[i])){
				count1++;
				if(count1 == count){
					index = i;
					break;
				}
			}
		}
		file=file.substring(index+1, file.length());
		return file;
		}
		return null;
	}
	
	
	@Override
	void initWidgets() {
		shell.setLayout(new GridLayout(2, false));
		//shell.setLayout(new gr);
		shell.setText("Game Window");
		/*shell.setBackgroundImage(mazePainter.back);
		shell.setLayoutData(mazePainter.back);*/
		
		
		// Bar menu
		Menu menuButton = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuButton);

		// File button in the bar
		MenuItem fileItem = new MenuItem(menuButton, SWT.CASCADE);
		fileItem.setText("&File");

		// Drop down functions for file button
		Menu subMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(subMenu);

		MenuItem properties = new MenuItem(subMenu, SWT.PUSH);
		properties.setText("Open Properties"); // Listener for load maze
		properties.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setText("Open Properties");
				fd.setFilterPath("");
				String[] filterExt = { "*.xml", "*.java","*.txt" , "*.maze", "*.*" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
 
				selected = getFile(selected);
				
			
				if (selected!=null){
				try {
					clientProperties = PropertiesHandler.read(selected);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
				else return;
			}
			
		});

		// load maze button in the sub menu
		MenuItem LoadMaze = new MenuItem(subMenu, SWT.PUSH);
		LoadMaze.setText("LoadMaze\tCtrl+L");
		// Listener for load maze
		LoadMaze.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				fd.setText("Load");
				fd.setFilterPath("");
				String[] filterExt = { "*.maze", "*.java","*.txt" ,"*.xml" , "*.*" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				selected = getFile(selected);
				
				
				if(selected!=null){
				String[] line = ("load" + " " +"maze" + " "+selected+" "+ "[A-Za-z0-9]").split(" ");
				String[] regexLoad = { "load maze [^ \n]+ [A-Za-z0-9]+" };
				commandsList.add(regexLoad);
				commandsList.add(line);
				setChanged();
				notifyObservers();
				commandsList.clear();
				}
				else return;
			

			}
		});
		// Save maze button in the sub menu
		MenuItem SaveMaze = new MenuItem(subMenu, SWT.PUSH);
		SaveMaze.setText("SaveMaze\tCtrl+S");
		// Listener for save maze
		SaveMaze.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd = new FileDialog(shell, SWT.SAVE);
				fd.setText("Save");
				fd.setFilterPath("");
				fd.setFileName(mazeName);
				String[] filterExt = { "*.maze", "*.java","*.txt" ,"*.xml" , "*.*" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				selected = getFile(selected);
				if(selected!=null){
				
				String[] line = ("save" + " " +"maze" + " "+"[A-Za-z0-9]"+" "+ selected).split(" ");
				String[] regexSave = { "save maze [A-Za-z0-9]+ [^ \n]+" };
				commandsList.add(regexSave);
				commandsList.add(line);
				setChanged();
				notifyObservers();
				}
				else return;
			}
		});

		// exit maze button in the sub menu
		exit = new MenuItem(subMenu, SWT.PUSH);
		exit.setText("EXIT");
		// Listener for exit maze

		exit.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				commandsList.add("exit".split("\b"));
				commandsList.add("null".split("\b"));
				setChanged();
				notifyObservers();
				commandsList.clear();
				music.stop();
				music.close();
				// mazePainter.getDisplay().getThread().;
				/*
				 * shell.getDisplay().close(); shell.dispose();
				 */

			}
		});

		/*
		 * LoadMaze.setAccelerator(SWT.MOD1 + 'A');
		 * SaveMaze.setAccelerator(SWT.MOD1 + 'A');
		 */

		// Help button in the bar
		MenuItem helpItem = new MenuItem(menuButton, SWT.CASCADE);
		helpItem.setText("&Help");
		// Drop down functions for help button
		Menu subMenu1 = new Menu(shell, SWT.DROP_DOWN);
		helpItem.setMenu(subMenu1);
		
        MenuItem about = new MenuItem(subMenu1,SWT.PUSH);
        about.setText("About");
        
		
		//item.setText("About");
		about.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				Shell aboutShell = new Shell(shell, SWT.TITLE | SWT.SYSTEM_MODAL | SWT.CLOSE | SWT.MAX);
				aboutShell.setLayout(new GridLayout(2, false));
				aboutShell.setSize(500, 200);

				aboutShell.setText("About");
				aboutShell.setLayout(new GridLayout(2, false));
				Text aboutText = new Text(aboutShell, SWT.NONE);
				aboutText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				aboutText.setText("Final project java 2016        Authors : Chen Luigi & Dvir Hashai     Lecturer : Eliahu Khlastchi ");
				
				aboutShell.open();

			}
		});
		MenuItem item1 = new MenuItem(subMenu, SWT.PUSH);
		item1.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				System.out.println("Select All");

			}
		});

		Button generateButton = new Button(shell, SWT.PUSH);
		generateButton.setText("Generate");
		generateButton.setLayoutData(new GridData(SWT.None, SWT.NONE, false, false, 1,1 ));

		generateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				generatewindow = new GenerateWindow(shell);
				generatewindow.setTriggerOk(new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						String[] generateline = { "generate", "3d", "maze", generatewindow.nameText.getText(),
								generatewindow.heightText.getText(), generatewindow.rowText.getText(),
								generatewindow.columnText.getText() };
						mazeName = generatewindow.nameText.getText();
						String[] regex = ("generate 3d maze [A-Za-z0-9]+ [0-9]{1,2} [0-9]{1,2} [0-9]{1,2}").split("\b");
						commandsList.add(regex);
						commandsList.add(generateline);
						mazePainterAdapter.generateBG = true;
						if(mazePainterAdapter.mazePainter.sound != null){
							mazePainterAdapter.mazePainter.DisposMazePicList();
						}
						setChanged();
						notifyObservers();
						
						commandsList.clear();
						generatewindow.generateshell.close();
						// mazeCanvas.mazePainter.redraw();
						//shell.setBackgroundImage(mazePainter.back);

					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}

				});
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});

		mazePainter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,3));
//mazePainter.drawBackground(GC.win32_new(mazePainter, mazePainter.back), 0, 0, shell.getSize().x, shell.getSize().y);
		System.out.println("fffg");
		Button solve = new Button(shell, SWT.PUSH);
		solve.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
		solve.setText("solve");
		solve.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] line = ("solve" + " " + mazeName + " " + "airdistance").split(" ");
				String[] regexSolve = { "solve [A-Za-z0-9]+ [A-Za-z0-9]+" };
				commandsList.add(regexSolve);
				commandsList.add(line);
				
				setChanged();
				notifyObservers();
				commandsList.clear();
				/*
				 * timer = new Timer(); task = new TimerTask() {
				 * 
				 * @Override public void run() { display.asyncExec(new
				 * Runnable() {
				 * 
				 * @Override public void run() {
				 * 
				 * if (mazePainterAdapter.mazePainter.curentPosition.equals(
				 * mazePainterAdapter.mazePainter.goalPosition)) {
				 * task.cancel(); }
				 * 
				 * if (!(mazePainterAdapter.mazePainter.solList.isEmpty()) &&
				 * (mazePainterAdapter.mazePainter.solList.get(counter) !=
				 * null)) { mazePainterAdapter.mazePainter.setFocus();
				 * mazePainterAdapter.mazePainter.curentPosition =
				 * mazePainterAdapter.mazePainter.solList.get(counter).getState(
				 * ); mazePainterAdapter.mazePainter.redraw();
				 * 
				 * }
				 * 
				 * }
				 * 
				 * }); } };
				 * 
				 * timer.scheduleAtFixedRate(task, 0, 500);
				 */
			}
		});

		
	
		
		
		Button music = new Button(shell, SWT.PUSH);
		music.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 1, 1));
		music.setText("music");
		music.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
			
			
				playMusic(new File("mario.wav"));
				
		

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		
		
		
		
		mouseZoomlListener = new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent e) {
				// if both ctrl and wheel are being operated
				if ((e.stateMask & SWT.CTRL) != 0)
					mazePainterAdapter.mazePainter.setSize(mazePainterAdapter.mazePainter.getSize().x + e.count,
							mazePainterAdapter.mazePainter.getSize().y + e.count);

			}
		};
		shell.addMouseWheelListener(mouseZoomlListener);
		mazePainterAdapter.mazePainter.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP)
					mazePainterAdapter.mazePainter.moveCharacterUp();
				if (e.keyCode == SWT.ARROW_DOWN)
					mazePainterAdapter.mazePainter.moveCharacterDown();
				if (e.keyCode == SWT.ARROW_LEFT)
					mazePainterAdapter.mazePainter.moveCharacterLeft();
				if (e.keyCode == SWT.ARROW_RIGHT)
					mazePainterAdapter.mazePainter.moveCharacterRight();
				if (e.keyCode == SWT.PAGE_UP)
					mazePainterAdapter.mazePainter.moveCharacterUpFloor();
				if (e.keyCode == SWT.PAGE_DOWN)
					mazePainterAdapter.mazePainter.moveCharacterDownFloor();
			}
		});
		// mazePainter.setMaze(maze3d);
		shell.setSize(clientProperties.getWidthSize(), clientProperties.getHeightSize());
		
		shell.open();

		// TODO
		shell.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				commandsList.add("exit".split("\b"));
				commandsList.add("null".split("\b"));
				setChanged();
				notifyObservers();
				commandsList.clear();

			}
		});
		shell.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ERROR_WIDGET_DISPOSED) {
					commandsList.add("exit".split("\b"));
					commandsList.add("null".split("\b"));
					commandsList.add("null".split("\b"));
					setChanged();
					notifyObservers();
				}

			}
		});

	}




    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

	private void playMusic(File file) {
		
		try {
			
			if(p%2 == 0){
			music = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			music.open(inputStream);
			// loop infinitely
			music.setLoopPoints(0, -1);
			music.loop(Clip.LOOP_CONTINUOUSLY);
			p++;
			}
			else{
				music.stop();
				p++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	public void exit(){
		shell.close();
		shell.dispose();
		
	}

	
}
