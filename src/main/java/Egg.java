import java.util.*;
import java.awt.*;

public class Egg {
	int row;
	int col;
	private static Random r = new Random();
	private Color cEgg = Color.blue;
	
	Egg(int row,int col){
		this.row = row;
		this.col = col;
	}
	//�����������λ��,��2��2��Ϊ�˽����ֵ���Ƶ�2~row   2~col ��Ϊ���ܻ�����ڴ��ڵıߣ������Ǳ����ǵ�
	Egg(){
		this(r.nextInt(Yard.ROWS-2)+2,r.nextInt(Yard.COLS-2)+2);
	}
	//����������������ĵ���λ��
	public void reAppear(){
		this.row = r.nextInt(Yard.ROWS-2)+2;
		this.col = r.nextInt(Yard.COLS-2)+2;
	}
	//���ڱ�ʾ������λ��
	public Rectangle getRect(){
		return new Rectangle(col*Yard.BLOCK_SIZE,row*Yard.BLOCK_SIZE,Yard.BLOCK_SIZE,Yard.BLOCK_SIZE);
	}
	//��ͼʱע�⣬x�������п�y�������о�
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(cEgg);
		g.fillOval(col*Yard.BLOCK_SIZE, row*Yard.BLOCK_SIZE, Yard.BLOCK_SIZE, Yard.BLOCK_SIZE);
		g.setColor(c);
		//ʵ����ɫ���л�����˸
		if(cEgg == Color.blue)
			cEgg = Color.green;
		else
			cEgg = Color.blue;
	}
}
