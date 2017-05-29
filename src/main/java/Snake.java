import java.awt.*;
import java.awt.event.KeyEvent;

public class Snake { //����������ʾ�ߵĽ� ������������ڲ��� ��
	private Node head = null;
	private Node tail = null;
	private int size = 0;
	private Node n = new Node(20,20,Dir.L);//������new �ⲿ��֮ǰ��new �ߵĽڣ������ȶ���һ��ͷ����new �ⲿ���ʱ��ͷ����
	private Yard y;
	
	Snake(Yard y){
		head = n;
		tail = n;
		size = 1;
		this.y = y;
	}
			
	public void draw(Graphics g){
		if(size <= 0 )
			return ;
		//Yard��PaintThreadÿһ��ˢ�¶������һ��update������update�����ֵ�����paint������paint�����ֵ�����
		   //draw������ÿ�ε���draw�����������move����������ÿ��ˢ���߾��ƶ�һ�Ρ�
		move();
		for(Node n = head; n != null; n = n.next){ //����������ÿ����㶼�����			
			n.draw(g);
			
		}
		
	}

	public void move(){//�ƶ�һ�ξ�����ͷ������һ����㣬��β��ɾ��һ����㣻����ֱ�ӵ���addToHead��������
		addToHead();
		deleteFromTail();
		checkDead();//ÿ���ƶ�����Ƿ��Ѿ�ײ��
	}
	
	public void checkDead(){ //�������Yard��stop���������ܷ���Snake������
		if(head.row < 2 ||head.col < 0 ||head.row >=Yard.ROWS|| head.col >= Yard.COLS )//������Ļ���ڴ�Сʵʱ������
			y.stop();
		for(Node n = head.next; n != null; n = n.next){
			if(head.col == n.col && head.row == n.row)
				y.stop();
		} 	
	}
	

	
	public void keyPressed(KeyEvent e) {	//����д�ķ�����ΪSnake��ķ�����Ŀ���Ƿ���Yard��ļ�����������
		switch(e.getKeyCode()){				//���̵��������Ҿ�����ͷ�ķ���
		case KeyEvent.VK_UP:if(head.dir != Dir.D)
								head.dir = Dir.U;
								break;
		case KeyEvent.VK_DOWN:if(head.dir != Dir.U)
								head.dir = Dir.D;
								break;
		case KeyEvent.VK_LEFT:if(head.dir != Dir.R)
								head.dir = Dir.L;
								break;
		case KeyEvent.VK_RIGHT:if(head.dir != Dir.L)
								head.dir = Dir.R;
								break;
		}
	}		
	
	public void addToTail(){
		Node node = null;
		switch(tail.dir){
		case L: node  = new Node(tail.row,tail.col+1,tail.dir);
				break;
		case R: node  = new Node(tail.row,tail.col-1,tail.dir);
				break;
		case U: node  = new Node(tail.row+1,tail.col,tail.dir);
				break;
		case D: node  = new Node(tail.row-1,tail.col,tail.dir);
				break;
		}
		tail.next = node;
		node.prev = tail;
		tail = node;
		tail.next = null;
		size++;

	}
	
	public void addToHead(){
		Node node = null;
		switch(head.dir){
		case L: node  = new Node(head.row,head.col-1,head.dir);
				break;
		
		case R: node  = new Node(head.row,head.col+1,head.dir);
				break;
		
		case U: node  = new Node(head.row-1,head.col,head.dir);
				break;
		
		case D: node  = new Node(head.row+1,head.col,head.dir);
				break;
		}
		node.next = head;
		head.prev = node;
		head = node;
		size++;
	}
	
	public void deleteFromTail(){
		if(size <= 0)
			return;				
		tail = tail.prev;
		tail.next = null;
		size--;
	}
	
	public  Rectangle getRect(){
		return new Rectangle(head.col*Yard.BLOCK_SIZE,head.row*Yard.BLOCK_SIZE,Yard.BLOCK_SIZE,Yard.BLOCK_SIZE);
	}
	
	public void eat (Egg e){
		if(this.getRect().intersects(e.getRect())){	//�ж���ײ�ķ���1
	//	if(e.col == head.col && e.row == head.row){ //�ж���ײ�ķ���2
			e.reAppear();
			this.addToHead();//������һ��
			y.setScore(y.getScore()+5);
		}
	}
	
	private class Node {

		int row,col;
		Node next = null;
		Node prev = null; //Ϊ��ʵ��ɾ�����һ�����ķ�����������ǰ��ָ��
		Dir dir = Dir.L;
		
		Node(int row,int col,Dir dir) {
			this.row = row;
			this.col = col;
			this.dir = dir;		
		}
				
		public void draw(Graphics g){	
			Color c = g.getColor();
			g.setColor(Color.cyan);
			g.fillRect(col*Yard.BLOCK_SIZE,row*Yard.BLOCK_SIZE,Yard.BLOCK_SIZE, Yard.BLOCK_SIZE);
			g.setColor(c);
		}		
	}

}