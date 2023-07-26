/* Eric Whye
Student Number: 19336881
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import util.GameConstants;
import util.UnitTests;

/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */ 



public class MainWindow{
	 private static JFrame frame = new JFrame("Game");// Change to the name of your game
	 private static Model gameworld= new Model();
	 private static Viewer canvas = new Viewer(gameworld);
	 private static int TargetFPS = GameConstants.TARGET_FPS;
	 private final AudioManager audioManager = AudioManager.getInstance();
	 private static boolean startGame = false;
	 private JLabel BackgroundImageForStartMenu;
	  
	public MainWindow() {
		frame.setSize(GameConstants.FRAME_SIZE_WIDTH, GameConstants.FRAME_SIZE_HEIGHT);  // you can customise this later and adapt it to change on size.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//If exit // you can modify with your way of quitting , just is a template.
		frame.setLayout(null);
		canvas.setBounds(0, 0, GameConstants.FRAME_SIZE_WIDTH, GameConstants.FRAME_SIZE_HEIGHT);
		canvas.setBackground(new Color(255,255,255)); //white background  replaced by Space background but if you remove the background method this will draw a white screen
		canvas.setVisible(false);   // this will become visible after you press the key.

		audioManager.playOnLoop(GameConstants.LEVEL_ONE_BACKGROUND_MUSIC_FILENAME, -10);
		JButton startMenuButton = new JButton("Start Game");  // start button
		startMenuButton.setBorderPainted(false);
		startMenuButton.setFocusPainted(false);
		startMenuButton.setVisible(true);
		startMenuButton.addActionListener(e -> {
			startMenuButton.setVisible(false);
			BackgroundImageForStartMenu.setVisible(false);
			canvas.setVisible(true);
			canvas.addKeyListener(KeyController.getInstance());    //adding the controller to the Canvascanvas.requestFocusInWindow();   // making sure that the Canvas is in focus so keyboard input will be taking in .
			canvas.addMouseListener(gameworld.getPlayer2MouseListener());
			canvas.requestFocusInWindow();
			startGame=true;
		});
		startMenuButton.setBounds((GameConstants.FRAME_SIZE_WIDTH-200)/2, (frame.getHeight()-40)/2, 200, 40);

		JLabel labelForSpace = new JLabel("<html>Space to Attack</html>");
		labelForSpace.setFont(new Font("Arial", Font.PLAIN, 19));
		labelForSpace.setVerticalTextPosition(JLabel.TOP);
		labelForSpace.setVerticalAlignment(JLabel.TOP);
		labelForSpace.setForeground(new Color(125, 206, 210));
		labelForSpace.setOpaque(false);
		labelForSpace.setBackground(new Color(0,0,0,0));
		labelForSpace.setVisible(true);
		labelForSpace.setBounds((GameConstants.FRAME_SIZE_WIDTH-200)/2, 50+(frame.getHeight()-40)/2, 200, 80);

		JLabel labelForMouse = new JLabel("<html>Mouse to Aim and Shoot</html>");
		labelForMouse.setFont(new Font("Arial", Font.PLAIN, 19));
		labelForMouse.setVerticalTextPosition(JLabel.TOP);
		labelForMouse.setVerticalAlignment(JLabel.TOP);
		labelForMouse.setForeground(new Color(125, 206, 210));
		labelForMouse.setOpaque(false);
		labelForMouse.setBackground(new Color(0,0,0,0));
		labelForMouse.setVisible(true);
		labelForMouse.setBounds((GameConstants.FRAME_SIZE_WIDTH-200)/2, 70+(frame.getHeight()-40)/2, 200, 80);

		frame.add(labelForMouse);
		frame.add(labelForSpace);
		frame.add(startMenuButton);
		frame.add(canvas);

		//loading background image
		File BackroundToLoad = new File("res/background.png");  //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
		try {
			BufferedImage myPicture = ImageIO.read(BackroundToLoad);
			BackgroundImageForStartMenu = new JLabel(new ImageIcon(myPicture));
			BackgroundImageForStartMenu.setBounds(0, 0, GameConstants.FRAME_SIZE_WIDTH, GameConstants.FRAME_SIZE_HEIGHT);
			frame.add(BackgroundImageForStartMenu);
		}  catch (IOException e) {e.printStackTrace();}

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		MainWindow hello = new MainWindow();  //sets up environment


		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				//swing has timer class to help us time this but I'm writing my own, you can of course use the timer, but I want to set FPS and display it
				int TimeBetweenFrames =  1000 / TargetFPS;
				long FrameCheck = System.currentTimeMillis() + (long) TimeBetweenFrames;
				//wait till next time step
				while (FrameCheck > System.currentTimeMillis()){}
				if(startGame)
					gameloop();
				//UNIT test to see if framerate matches
				UnitTests.CheckFrameRate(System.currentTimeMillis(),FrameCheck, TargetFPS);
				this.run();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	} 
	//Basic Model-View-Controller pattern 
	private static void gameloop() { 
		// GAMELOOP  
		
		// controller input  will happen on its own thread 
		// So no need to call it explicitly 
		
		// model update   
		gameworld.gamelogic();
		// view update 
		canvas.updateview();
		
		// Both these calls could be setup as a thread but we want to simplify the game logic for you.
	}
}