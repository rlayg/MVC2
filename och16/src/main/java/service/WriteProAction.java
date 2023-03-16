package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class WriteProAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("WriteProAction start...");
		try {
			request.setCharacterEncoding("utf-8");
			// 1. num , pageNum, writer ,  email , subject , passwd , content   Get
			
			String pageNum = request.getParameter("pageNum");
			// 2. Board board 생성하고 Value Setting
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
			
			// 3. BoardDao bd Instance
			//          int result = bd.insert(board);
			BoardDao bd = BoardDao.getInstance(); //DB
			int result = bd.insert(board);
			
			
	        // 4. request 객체에 result, num , pageNum
			request.setAttribute("result", result);
			request.setAttribute("num", board.getNum());
			request.setAttribute("pageNum", pageNum);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "writePro.jsp";
	}

}
