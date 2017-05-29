import java.awt.*;
import java.awt.event.*;

public class Yard extends Frame {



	 public static final int COLS = 45;//��
	 public static final int ROWS = 45;//��   �������е�С��
	 public static final int BLOCK_SIZE = 15;//С��Ĵ�С
	 Snake s = new Snake(this);
	 Egg e = new Egg();
	 Image offScreenImage = null;
	 PaintThread paintThread = new PaintThread();
	 private boolean gameOver = false; //���ڿ���ֹͣ��ʱ��ֹͣ��Ϊֹͣˢ��
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
	 
	 public void paint(Graphics g){//�������еĺ����ߣ��γ�С����Ȼ�����draw���������ߵ����н�
		 Color  c=  g.getColor();
		 g.setColor(Color.magenta);
		 g.fillRect(0, 0, COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE); 
		 /*ע���ڴ����ñ�������Frame���췽�������ñ�����������
		  * �ڴ�����ÿ�λ�ˢ���ػ棬��setBackground�ǲ����ػ�ġ�
		  * �����ߵĹ켣���ԣ���� setBackground����ô��ˢ�£����Ǳ������ϴεĺۼ��������ǵ�֮�󲻻����±�¶��
		  * ���paint�����ñ�������ô���������ǵ�֮������ˢ�£����������±�¶�ˡ� 
		  * ������ͼ�йصĽ�����ȫ��������paint�����ж�����setBackground��
		  * */
		 
		 //����
		 g.setColor(Color.WHITE);
		 for(int i = 1;i<ROWS ;i++ ){
			 g.drawLine(0,BLOCK_SIZE * i,COLS * BLOCK_SIZE,BLOCK_SIZE * i);
		 }
		 //����
		 for(int i = 1;i<COLS;i++){
			 g.drawLine(BLOCK_SIZE *i, 0, BLOCK_SIZE *i, ROWS*BLOCK_SIZE);
		 }
		 g.setColor(Color.black);
		 g.setFont(new Font("����", Font.BOLD,20 ));
		 g.drawString("SCORE: "+ score , 40, 60);
		 if(gameOver){
			 g.setFont(new Font("����", Font.BOLD,50 ));
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
	
	 /*����������˸ ����˫������ƣ�offScreenImage �ǻ�����Ļ�ϵ�ͼ��gOff�ǻ�����Ļ�ϵĻ���
	  * �Ƚ�������ͼ�񣨽������ŵ��������У���Ȼ�󽫸�ͼ��Ļ��ʸ���gOff��gOff���ڴ��л��Ƹ�ͼ��
	  * ������ʵ�Ļ��ʽ��ڴ��еĸ�ͼ��浽��ʵ��Ļ������
	 */	 
	public void update(Graphics g) { 
		if(offScreenImage == null)
			offScreenImage = createImage(BLOCK_SIZE*COLS, BLOCK_SIZE*ROWS);
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);//ʵ�ʵĻ���
	}
	
	//���̼������࣬���ڸ��ݼ����������ߵ��ƶ�����
	private class KeyMonitor extends KeyAdapter{ 

		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()){
				case KeyEvent.VK_F1: gameStart();break;
				case KeyEvent.VK_F2: gamePause();break;
				case KeyEvent.VK_F3: gameStart();break; //�����𵽼���ˢ�µ�Ч��
				case KeyEvent.VK_F4: gameRePlay();break;
			}											
			s.keyPressed(e);
		}
		//ʵ�����ǵ�����Snake���keyPressed��д�����������м������������Ҽ�
		//���¼�e���ݸ�Snake���keyPressed��д���������൱�� ���� Snake ���е�public void keyPressed(KeyEvent e) 
		//ע��Snake���keyPressed���طŵ����������棬��ΪYard��Ҳ��Ҫʹ�ü������࣬�ϲ���һ���������༴��
		//����Snake���keyPressed���������Էŵ�������ȥ���������޷������ˡ�

	
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
	
	//�����̣߳���Ϊ���Ƶ��߳���Ҫ���ϵĽ��У�������ѭ���У��������������޷�ִ�У�
	//���Ǵ���һ���̣߳�ר�������ػ浱ǰ��Ļ���ߵ�λ�á����ӻ᲻�ϱ仯��ÿ�α仯��Ҫrepaint��,ÿ0.1���ػ�һ��
	//repaint���ȵ���update��������д��update��������ʹ��˫����
	
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
