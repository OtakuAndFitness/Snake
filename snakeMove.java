import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
/**
 * 
 * @author Bowen Tao
 *
 */

public class snakeMove extends JPanel implements KeyListener, ActionListener,Runnable{
	JButton start=new JButton("Start");
	JButton end=new JButton("End");
	JDialog dialog =new JDialog();
	Label label =new Label();
	JButton exit=new JButton("Exit");
	int score,speed=5,x,y,dir;
	boolean run=false;
	Random random=new Random();
	ArrayList<snakeData> snake =new ArrayList<snakeData>();
	Thread thread;
	
	public snakeMove(){
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(start);
		this.add(end);
		dialog.setLayout(new GridLayout(2,1));
		dialog.setSize(200, 200);
		dialog.setLocation(200, 200);
		dialog.add(label);
		dialog.add(exit);
		start.addActionListener(this);
		end.addActionListener(this);
		exit.addActionListener(this);
		this.addKeyListener(this);
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawRect(10, 40, 400, 300);
		g.drawString("Score: "+score, 150, 20);
		g.drawString("Speed: "+speed, 220, 20);
		
		if (run){
			//color snake
			g.setColor(Color.RED);
			for (int i=0;i<snake.size();i++){
				g.fillRect(10+snake.get(i).getxCoor()*10, 40+snake.get(i).getyCoor()*10, 10, 10);
			}
			//color apple
			g.setColor(Color.BLUE);
			g.fillRect(10+x*10, 40+y*10, 10, 10);
		}
	}

	boolean maxLimite(int x, int y){//maximum boundary judgment
		if (x<0 || x>=40 || y<0 || y>=30){
			return false;
		}
		for (int i=0;i<snake.size();i++){
			if (i>1 && snake.get(0).getxCoor()==snake.get(i).getxCoor() && snake.get(0).getyCoor()==snake.get(i).getyCoor()){
				return false;
			}
		}
		return true;
	}
	
	boolean minLimite(int x, int y){//minimum boundary judgment 
		if (maxLimite(snake.get(0).getxCoor()+x, snake.get(0).getyCoor()+y)){
			return true;
		}
		return false;
	}
	
	public void move(int x, int y){//snake move
		if (minLimite(x, y)){
			bodyMove();
			snake.get(0).setxCoor(snake.get(0).getxCoor()+x);
			snake.get(0).setyCoor(snake.get(0).getyCoor()+y);
			eat();
			repaint();
		}else{
			//dead
			thread=null;
			label.setText("You Are Dead! Total Score is: "+score);
			dialog.setVisible(true);
		}
	}
	
	public void bodyMove(){
		snakeData s=new snakeData();
		for (int i=0;i<snake.size();i++){
			if (i==1){
				snake.get(i).setxCoor(snake.get(0).getxCoor());
				snake.get(i).setyCoor(snake.get(0).getyCoor());
			}else if (i>1){
				s=snake.get(i-1);
				snake.set(i-1, snake.get(i));
				snake.set(i, s);	
			}
		}
	}
	
	public void eat(){//eat apple
		if (x==snake.get(0).getxCoor() && y==snake.get(0).getyCoor()){
			//randomly come out another apple
			x=random.nextInt(40);
			y=random.nextInt(30);
			//score increased
			score+=10;
			if (score>10 && score%100==0){
				speed--;
			}
			if (speed<1){
				speed=1;
			}
			//increase snake body
			snakeData s=new snakeData();
			s.setxCoor(snake.get(snake.size()-1).getxCoor());
			s.setyCoor(snake.get(snake.size()-1).getyCoor());
			snake.add(s);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource()==start){//game start
			run=true;
			start.setEnabled(false);
			//a new snake
			snakeData s=new snakeData();
			s.setxCoor(20);
			s.setyCoor(15);
			snake.add(s);
			//a new apple
			x=random.nextInt(40);
			y=random.nextInt(30);
			
			thread=new Thread(this);
			thread.start();
			
			requestFocus();
			repaint();
			
		}
		if (arg0.getSource()==end){
			System.exit(0);
		}
		if (arg0.getSource()==exit){
			snake.clear();
			run=false;
			start.setEnabled(true);
			score=0;
			speed=0;
			thread=null;
			dialog.setVisible(false);
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (run){
			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_UP:
				move(0, -1);
				dir=1;
				break;

			case KeyEvent.VK_DOWN:
				move(0, 1);
				dir=2;
				break;
				
			case KeyEvent.VK_LEFT:
				move(-1, 0);
				dir=3;
				break;
				
			case KeyEvent.VK_RIGHT:
				move(1, 0);
				dir=4;
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {//auto move right 
		// TODO Auto-generated method stub
		while (run){
			switch (dir) {
			case 1:
				move(0, -1);
				break;

			case 2:
				move(0, 1);
				break;
				
			case 3:
				move(-1, 0);
				break;
				
			case 4:
				move(1, 0);
				break;
				
			default:
				move(1, 0);
				break;
			}
			repaint();
			try {
				Thread.sleep(speed*100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
