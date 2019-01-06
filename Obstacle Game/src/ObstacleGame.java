import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*
    Class:      ObstacleGame
    Purpose:    Creates a simple obstacle course for a player to navigate
    Author:     Tyler Beveridge
*/

public class ObstacleGame extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Rectangle player = new Rectangle(); //a rectangle that represents the player
    private Rectangle goal = new Rectangle(); //a rectangle that represents the goal
    private Enemy[] enemies = new Enemy[6]; //the array of Enemy objects
    
    private boolean up, down, left, right; //booleans that track which keys are currently pressed
    private Timer timer; //the update timer
    
    private int gameWidth = 600; //the width of the game area
    private int gameHeight = 600; //the height of the game area
    
    private int level = 1; //the level of the game
    private int updateTimer;
    private int score = 0;
    private int deathTimer = 30;
    private int tries = 1;

    //Sets up the basic GUI for the game
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        
        frame.setTitle("Obstacle Game");
        frame.setLayout(new BorderLayout());
        
        ObstacleGame game = new ObstacleGame();
        frame.add(game, BorderLayout.CENTER);
        
        game.addKeyListener(game);
        frame.addKeyListener(game);
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        
        game.setUpGame();
    }
    
    //Constructor for the game panel
    public ObstacleGame() {
         setPreferredSize(new Dimension(gameWidth, gameHeight));
    }
    
    //Method that is called by the timer 30 times per second (roughly)
    //Most games go through states - updating objects, then drawing them
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }
    
    //Called every time a key is pressed
    //Stores the down state for use in the update method
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }
    
    //Called every time a key is released
    //Stores the down state for use in the update method
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }
    
    //Called every time a key is typed
    public void keyTyped(KeyEvent e) {
    }
    
    //Sets the initial state of the game
    //Could be modified to allow for multiple levels
    public void setUpGame() {
    
        if(timer != null) {
            timer.stop();
        }
    
        timer = new Timer(1000 / 30, this); //roughly 30 frames per second
        timer.start();
        
        up = down = left = right = false;
    
        player = new Rectangle(50, 50, 50, 50);
        goal = new Rectangle(500, 500, 50, 50);
        deathTimer = 30;
        
        if(level == 1) {
        	enemies[0] = new HorizontalFlashingEnemy(200, 200, 50, 50, 5, gameWidth);
        	enemies[1] = new HorizontalFlashingEnemy(100, 400, 50, 50, 5, gameWidth);
        	enemies[2] = new VerticalEnemy(200, 300, 50, 50, gameHeight, 5);
        	enemies[3] = new VerticalEnemy(400, 500, 50, 50, gameHeight, 5);
        	
        } else if(level == 2) {
        	player.y = 525;
        	player.x = 300 - (player.width / 2);
        	goal.y = 50;
        	goal.x = 50;
        	enemies[0] = new HorizontalFlashingEnemy(200, 200, 50, 50, 5, gameWidth);
        	enemies[1] = new HorizontalFlashingEnemy(100, 400, 50, 50, 5, gameWidth);
        	enemies[2] = new VerticalEnemy(200, 300, 50, 50, gameHeight, 5);
        	enemies[3] = new VerticalEnemy(400, 500, 50, 50, gameHeight, 5);
        	enemies[4] = new DiagonalEnemy(200, 50, 50, 50, gameHeight, 5, gameWidth, 6);
        	enemies[5] = new DiagonalEnemy(50, 200, 50, 50, gameHeight, 5, gameWidth, 6);        	
        } else if(level == 3) { 
        	enemies[0] = new VerticalEnemy(200, 300, 50, 50, gameHeight, 5);
        	enemies[1] = new DiagonalEnemy(300, 300, 50, 50, gameHeight, 5, gameWidth, 6);
        	enemies[2] = new StalkerEnemy(100, 200, 50, 50, player);
        	enemies[3] = new SpinningEnemy(450, 400, 50, 50, 100); 
        	enemies[4] = null;
        	enemies[5] = null; 
        } else if(level == 4) {
        	player.y = 10;
        	player.x = 300 - (player.width / 2);
        	goal.y = 400;
        	goal.x = 25;
        	enemies[0] = new StalkerEnemy(50, 250, 50, 50, player);
        	enemies[1] = new StalkerEnemy(gameWidth - 100, 0, 50, 50, player);
        	enemies[2] = new SpinningEnemy(gameWidth/2, gameHeight/3 * 2, 50, 50, 100);
        	enemies[3] = new VerticalEnemy(500, 250, 50, 50, gameHeight, 5);
        	enemies[4] = new HorizontalFlashingEnemy(50, gameHeight - 100, 50, 50, 5, gameWidth);
        	enemies[5] = new DiagonalEnemy(250, 250, 50, 50, gameHeight, 5, gameWidth, 6);
        }
        
    }
    
    //The update method does 5 things
    //1 - it has the player move based on what key is currently being pressed
    //2 - it prevents the player from leaving the screen
    //3 - it checks if the player has reached the goal, and if so congratulates them and restarts the game
    //4 - it checks if any of the Enemy objects are touching the player, and if so notifies the player of their defeat and restarts the game
    //5 - it tells each of the Enemy objects to update()
    public void update() {
        if(up) {
            player.y-=3;
        }
        if(down) {
            player.y+=3;
        }
        if(left) {
            player.x-=3;
        }
        if(right) {
            player.x+=3;
        }
        
        if(player.x < 0) {
            player.x = 0;
        }
        else if(player.x + player.width > gameWidth) {
            player.x = gameWidth - player.width;
        }
        
        if(player.y < 0) {
            player.y = 0;
        }
        else if(player.y + player.height > gameHeight) {
            player.y = gameHeight - player.height;
        }
        
        
        
        updateTimer++;
        if(updateTimer % 30 == 0) {
        	updateTimer = 0;
        	deathTimer--;
        	for(Enemy e: enemies) {
                 if(e == null)
                     continue;
                 if(e instanceof HorizontalFlashingEnemy) {
                	 ((HorizontalFlashingEnemy) e).setActive(!((HorizontalFlashingEnemy)e).getActive());
                 }
        	}
        	if(deathTimer == 0) {
        		tries++;
            	JOptionPane.showMessageDialog(null, "You lost");
                setUpGame();
        	}
        }
        
        
        
        if(player.intersects(goal)) {
        	level += 1;
        	score += deathTimer/tries;
        	if(level == 5) {
        		JOptionPane.showMessageDialog(null, "You won!\n Your final score was " + score);
        		System.exit(0);
        	} else {
        		JOptionPane.showMessageDialog(null, "You get to move on!");
        	}
            setUpGame();
        }
        
        for(Enemy e: enemies) {
            if(e == null)
                continue;
        
            if(e.intersects(player) && e instanceof HorizontalFlashingEnemy) {
            	if(((HorizontalFlashingEnemy)e).getActive() == false) {
            		e.move();
            		continue;
            	} else {
            		tries++;
            		JOptionPane.showMessageDialog(null, "You lost");
                    setUpGame();
            	}
            } else if(e.intersects(player)) {
            	tries++;
            	JOptionPane.showMessageDialog(null, "You lost");
                setUpGame();
            }
            
            e.move();
        }
        
    }
    
    //The paint method does 4 things
    //1 - it draws a white background
    //2 - it draws the player in blue
    //3 - it draws the goal in green
    //4 - it draws all the Enemy objects
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, gameWidth, gameHeight);
     for(Enemy e: enemies) {
            if(e == null)
                continue;
            e.draw(g);
        }
     	
        g.setColor(Color.BLUE);
        g.fillRect(player.x, player.y, player.width, player.height);
        
        g.setColor(Color.GREEN);
        g.fillRect(goal.x, goal.y, goal.width, goal.height);
        
        g.setColor(Color.BLACK);
        g.drawString("Time Left: " + deathTimer, 250, 50);
        g.drawString("Score: " + score, 250, 75);
        g.drawString("Attempts: " + tries, 250, 100);
        
       
    }

}