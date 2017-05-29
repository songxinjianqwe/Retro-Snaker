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
	//用于设置随机位置,减2加2是为了将随机值限制到2~row   2~col 因为可能会出现在窗口的边，而边是被覆盖的
	Egg(){
		this(r.nextInt(Yard.ROWS-2)+2,r.nextInt(Yard.COLS-2)+2);
	}
	//用于重新设置随机的蛋的位置
	public void reAppear(){
		this.row = r.nextInt(Yard.ROWS-2)+2;
		this.col = r.nextInt(Yard.COLS-2)+2;
	}
	//用于表示出蛋的位置
	public Rectangle getRect(){
		return new Rectangle(col*Yard.BLOCK_SIZE,row*Yard.BLOCK_SIZE,Yard.BLOCK_SIZE,Yard.BLOCK_SIZE);
	}
	//画图时注意，x坐标是列宽，y坐标是行距
	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(cEgg);
		g.fillOval(col*Yard.BLOCK_SIZE, row*Yard.BLOCK_SIZE, Yard.BLOCK_SIZE, Yard.BLOCK_SIZE);
		g.setColor(c);
		//实现颜色的切换，闪烁
		if(cEgg == Color.blue)
			cEgg = Color.green;
		else
			cEgg = Color.blue;
	}
}
