package service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class DeleteProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("DeleteProAction start...");
		
		request.setCharacterEncoding("utf-8");
		
		try {
			
			// 1. num , passwd , pageNum Get
			int num = Integer.parseInt(request.getParameter("num"));
			String passwd = request.getParameter("passwd");
			String pageNum = request.getParameter("pageNum");
			
			// 2. BoardDao bd Instance
			BoardDao bd = BoardDao.getInstance();
			
			// 3. 본인의 게시판 만 삭제 
			//    int result = bd.delete(num, passwd);
			
			int result = bd.delete(num, passwd);
			
			// 4. request 객체에  num , pageNum ,result
			request.setAttribute("num", num);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("result", result);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "deletePro.jsp";
	}

}


/*

	try {
		
		String pageNum = request.getParameter("pageNum");
		Board board = new Board();
		board.setNum(Integer.parseInt(request.getParameter("num")));
		board.setWriter(request.getParameter("writer"));
		board.setEmail(request.getParameter("email"));
		board.setSubject(request.getParameter("subject"));
		board.setPasswd(request.getParameter("passwd"));
		board.setRef(Integer.parseInt(request.getParameter("ref")));
		board.setRe_step(Integer.parseInt(request.getParameter("re_step")));
		board.setRe_level(Integer.parseInt(request.getParameter("re_level")));
		board.setContent(request.getParameter("content"));
		board.setIp(request.getRemoteAddr());
		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("DeleteProAction e.getMessage --> " + e.getMessage());
	}

*/
