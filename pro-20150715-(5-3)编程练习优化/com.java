package imooc;
import java.util.Scanner;
public class com {
		public static void main(String[] args)
		{
			Scanner input_1=new Scanner(System.in);
			System.out.print("请输入考试成绩信息:");			
			int score=input_1.nextInt();
			int count=0;
			System.out.println("加分前的分数:"+score);
			while(score<60){
				score++;
				count++;
			}
			System.out.println("加分后的成绩:"+score);
			System.out.println("加分次数:"+count);
		}
}
