import java.awt.*;
import java.awt.event.*;

public class Yard extends Frame {



	 public static final int COLS = 45;//列
	 public static final int ROWS = 45;//行   多少行列的小格
	 public static final int BLOCK_SIZE = 15;//小格的大小
	 Snake s = new Snake(this);
	 Egg e = new Egg();
	 Image offScreenImage = null;
	 PaintThread paintThread = new PaintThread();
	 private boolean gameOver = false; //用于控制停止的时候，停止即为停止刷新
	 private int score = 0;
	 

	 Yard(){
		super("Snake F1:Start F2:Pause F3:Speedup F4:Replay Welcome to Snake!");
		this.setBounds(200,200,COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				System.exit(0);
			}
		} );
		this.addKeyListener(new KeyMonitor());
		//new Thread(paintThread).start();
		this.setVisible(true);		
	 }
	 
	 public void paint(Graphics g){//画出所有的横竖线，形成小方格，然后调用draw方法画出蛇的所有节
		 Color  c=  g.getColor();
		 g.setColor(Color.magenta);
		 g.fillRect(0, 0, COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE); 
		 /*注意在此设置背景和在Frame构造方法中设置背景的区别是
		  * 在此设置每次会刷新重绘，而setBackground是不会重绘的。
		  * 对于蛇的轨迹而言，如果 setBackground，那么不刷新，总是保留着上次的痕迹。被覆盖掉之后不会重新暴露。
		  * 如果paint中设置背景，那么背景被覆盖掉之后，重新刷新，背景就重新暴露了。 
		  * 因此与绘图有关的将背景全部设置在paint方法中而不是setBackground。
		  * */
		 
		 //横线
		 g.setColor(Color.WHITE);
		 for(int i = 1;i<ROWS ;i++ ){
			 g.drawLine(0,BLOCK_SIZE * i,COLS * BLOCK_SIZE,BLOCK_SIZE * i);
		 }
		 //竖线
		 for(int i = 1;i<COLS;i++){
			 g.drawLine(BLOCK_SIZE *i, 0, BLOCK_SIZE *i, ROWS*BLOCK_SIZE);
		 }
		 g.setColor(Color.black);
		 g.setFont(new Font("宋体", Font.BOLD,20 ));
		 g.drawString("SCORE: "+ score , 40, 60);
		 if(gameOver){
			 g.setFont(new Font("宋体", Font.BOLD,50 ));
			 g.setColor(Color.white);
			 g.drawString("Game Over", COLS/3*BLOCK_SIZE, ROWS/3*BLOCK_SIZE);
			 g.drawString("Your Score is "+score, COLS/4*BLOCK_SIZE, (int) ((int)ROWS/1.5*BLOCK_SIZE));			 
			 paintThread.gameOver();
		 }	
		 s.eat(e);
		 s.draw(g);
		 e.draw(g);
		
		 g.setColor(c);
	 }
	 
	public void stop(){
		gameOver = true;
	}
	
	 /*用于消除闪烁 采用双缓冲机制：offScreenImage 是缓冲屏幕上的图像，gOff是缓冲屏幕上的画笔
	  * 先建立缓冲图像（将背景放到缓冲区中），然后将该图像的画笔赋给gOff，gOff在内存中绘制该图像
	  * 再用真实的画笔将内存中的该图像绘到真实屏幕中来。
	 */	 
	public void update(Graphics g) { 
		if(offScreenImage == null)
			offScreenImage = createImage(BLOCK_SIZE*COLS, BLOCK_SIZE*ROWS);
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);//实际的绘制
	}
	
	//键盘监视器类，用于根据键盘来调整蛇的移动方向
	private class KeyMonitor extends KeyAdapter{ 

		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
				case KeyEvent.VK_F1: gameStart();break;
				case KeyEvent.VK_F2: gamePause();break;
				case KeyEvent.VK_F3: gameStart();break; //可以起到加速刷新的效果
				case KeyEvent.VK_F4: gameRePlay();break;
			}											
			s.keyPressed(e);
		}
		//实际上是调用了Snake类的keyPressed重写方法，方法中监听了上下左右键
		//将事件e传递给Snake类的keyPressed重写方法，就相当于 调用 Snake 类中的public void keyPressed(KeyEvent e) 
		//注意Snake类的keyPressed不必放到监视器里面，因为Yard类也需要使用监视器类，合并到一个监视器类即可
		//并且Snake类的keyPressed方法不可以放到子类中去，这样就无法访问了。

	
	}
	
	private void gameStart() {
		new Thread(paintThread).start();
	}
	private void gamePause(){
		paintThread.pause();
	}
	
	private void gameRePlay() {
		s = new Snake(this);
		this.setScore(0);
		this.gameOver = false;
		paintThread.rePlay();
		new Thread(paintThread).start();

	}
	
	//绘制线程，因为绘制的线程需要不断的进行，处于死循环中，所以其他代码无法执行，
	//于是创建一个线程，专门用于重绘当前屏幕（蛇的位置、身子会不断变化，每次变化都要repaint）,每0.1秒重绘一次
	//repaint会先调用update，我们重写了update方法，会使用双缓冲
	
	private class PaintThread implements Runnable {
		private boolean running = true;
		private boolean pause = false;
		
		public void run(){
			while(running){
				if(pause)
					continue;
				else
					repaint();
				
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}			
		}
		public void gameOver() {
			running = false;
		}
		
		public void pause(){
			pause = true;
		}		
		public void rePlay(){
			running = true;
			pause = false;
		}

	}
	
	 public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	
	public static void main(String []args){
		new Yard();
		
	}
}
