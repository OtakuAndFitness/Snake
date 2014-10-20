import javax.swing.JFrame;


public class snakeWin extends JFrame{
	//game window
	public snakeWin(){
		this.setVisible(true);
		this.setLocation(200, 200);
		this.setSize(435, 390);
		this.setTitle("SNAKE--v1.0");
		
		snakeMove sMove =new snakeMove();
		add(sMove);
		
	}
	
	public static void main(String[] args){
		new snakeWin();
	}
}
