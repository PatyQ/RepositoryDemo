package com.cy.qxy.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.SessionAttributes;

import com.cy.qxy.pojo.ImageCodePojo;

@WebServlet("/code.do")
public class ImageCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ImageCodeServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		ImageCodePojo icp = getImgcode();//获得验证码的值
		
		request.getSession().setAttribute("verification", icp.getResult());//存验证码的结果
		
		//封装方法，画一个验证码并将验证码输出到前台
		printImgCode(response,icp.getResultStr());
	}
	
	
	
	private void printImgCode(HttpServletResponse response, String resultStr) throws IOException {
		int width = 130;
		int height = 40;
		
		//创建一个画布
		BufferedImage bufferedimage = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR);
		
		//创建画笔工具
		Graphics graphics = bufferedimage.getGraphics();
		
		//画画
		graphics.setColor(randLowrColor());//背景色
		graphics.fillRect(0, 0, width, height);//前两位从哪里开始
		
		//绘制字符串
		graphics.setColor(randDeepColor());// 重新给画笔上色(必须重新上色否则跟背景色相同)
		graphics.setFont(new Font("Times New Roman", Font.PLAIN, 20));
//		graphics.drawString(resultStr, 10, 10);
		//重点
		char[] cs = resultStr.toCharArray();//转换成字符数组
		for(int i=0;i<cs.length;i++){
//			graphics.drawString( resultStr,10*i+rd.nextInt(10),rd.nextInt(18)+15);
			graphics.drawString(String.valueOf(cs[i]),10*i+rd.nextInt(10),rd.nextInt(18)+15);
		}
		ImageIO.write(bufferedimage, "jpg", response.getOutputStream());//将画布响应到前台
		
	}

	static final char[] symbol = {'+','-','*'};
	static final int[] NUMBER = {1,2,3,4,5,6,7,8,9,0};
	Random rd = new Random();
	
	private ImageCodePojo getImgcode() {
		ImageCodePojo icp = new ImageCodePojo();//获得验证码对象
		//随机得到两个数字和一个符号
		int num1 = NUMBER[rd.nextInt(NUMBER.length)];
		int num2 = NUMBER[rd.nextInt(NUMBER.length)];
		char symbol1 = symbol[rd.nextInt(symbol.length)];
		
		//利用switch
		switch (symbol1) {
		case '+':
			icp.setResult(num1+num2);
			break;
		case '-':
			if (num1<num2) {
				int num;
				num = num1;
				num1 = num2;
				num2 = num;
			}		
			icp.setResult(num1-num2);
			break;
		case '*':
			icp.setResult(num1*num2);
			break;
		default:
			break;
		}
		//获得验证码的符号
		icp.setResultStr(num1+" "+symbol1+" "+num2+" =");
		return icp;
	}
	
	// 得到比较深的颜色
	private Color randDeepColor() { // 深色
		
		int r = rd.nextInt(255);
		int g = rd.nextInt(255);
		int b = rd.nextInt(255);

		while(isDark(r,g,b)){ //代表是浅色
			r = rd.nextInt(255);
			g = rd.nextInt(255);
			b = rd.nextInt(255);
		}
		
		return new Color(r, g, b);
	}

	// 判断是浅色还是深色
	private boolean isDark(int r, int g, int b) {

		return r * 0.299 + g * 0.578 + b * 0.114 > 100; // 值越小,颜色越深
	}

	// 得到比较浅的颜色
	private Color randLowrColor() { // 浅色
		int r = rd.nextInt(255);
		int g = rd.nextInt(255);
		int b = rd.nextInt(255);
		
		while(!isDark(r,g,b)){ //代表是浅色
			r = rd.nextInt(255);
			g = rd.nextInt(255);
			b = rd.nextInt(255);
		}

		return new Color(r, g, b);
	}

}

