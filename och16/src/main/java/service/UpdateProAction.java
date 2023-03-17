package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UpdateProAction start..");
		// 1. num , pageNum, writer ,  email , subject , passwd , content   Get
		request.setCharacterEncoding("utf-8");  // utf-8 하면서 throws 한거 CommandProcess.java에도 같은 throws를 해줘야 한다. CommandProcess.java에서 먼저하는게 정석이라는데 상관없는듯 
		//int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		//String writer = request.getParameter("writer");
		//String email = request.getParameter("email");
		//String subject = request.getParameter("subject");
		//String passwd = request.getParameter("passwd");
		//String content = request.getParameter("content");
		
		
		
		try {
			// 2. Board board 생성하고 Value Setting
			Board board = new Board();
			board.setNum(Integer.parseInt(request.getParameter("num")));
			board.setWriter(request.getParameter("writer"));
			board.setEmail(request.getParameter("email"));
			board.setSubject(request.getParameter("subject"));
			board.setPasswd(request.getParameter("passwd"));
			board.setContent(request.getParameter("content"));
			board.setIp(request.getRemoteAddr());
			
			// 3. BoardDao bd Instance
			BoardDao bd = BoardDao.getInstance();
			
			// 4. int result = bd.update(board);
			int result = bd.update(board);
			
			
			// 5. request 객체에 result, num , pageNum
			request.setAttribute("result", result);
			request.setAttribute("num", board.getNum());
			request.setAttribute("pageNum", pageNum);
			
			
		} catch (Exception e) {
			System.out.println("UpdateProAction e.getMessage() --> " + e.getMessage());
		}
		
		// 6. updatePro.jsp Return
		return "updatePro.jsp";
	}

}

/*
선생님 답 원본

package service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		// 1. num , pageNum, writer ,  email , subject , passwd , content   Get
		request.setCharacterEncoding("utf-8");  
        String pageNum = request.getParameter("pageNum");
		
		try {
			// 2. Board board 생성하고 Value Setting
	        Board board = new Board();
			board.setNum(Integer.parseInt(request.getParameter("num")));
	        board.setWriter(request.getParameter("writer"));
	        board.setEmail(request.getParameter("email"));
	        board.setSubject(request.getParameter("subject"));
	        board.setPasswd(request.getParameter("passwd"));
			board.setContent(request.getParameter("content"));
			board.setIp(request.getRemoteAddr());
			// 3. BoardDao bd Instance
			BoardDao bd = BoardDao.getInstance();
			int result = bd.update(board);
			// 4. request 객체에 result, num , pageNum 
			request.setAttribute("result", result);
			request.setAttribute("num", board.getNum());
			request.setAttribute("pageNum", pageNum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
        // 5.updatePro.jsp Return
        return  "updatePro.jsp";	
    }

}




*/