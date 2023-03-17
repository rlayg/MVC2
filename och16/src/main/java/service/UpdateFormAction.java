package service;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("UpdateFormAction start..");
		// 1. num , pageNum  GET
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		try {
		// 2. BoardDao bd Instance
			BoardDao bd = BoardDao.getInstance();
		// 4. Board board = bd.select(num);			
			Board board = bd.select(num);
		// 5. request 객체에 pageNum , board
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("board", board);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("UpdateFormAction e.getMessage --> " + e.getMessage());
		}
		
		return "updateForm.jsp";
	}

}
