package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;
// 서비스에서 보통 다오를 호출. 서비스 안에 비지니스 로직
public class ContentAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("ContentAction Service start...");
		
		// 1. num, pageNum --> 나중 수정
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
//		int num = 1;		수정전
//		String pageNum = "1";
		
		try {
			// 2. BoardDao bd Instance
			BoardDao bd = BoardDao.getInstance();
			
			// 3. num의 readCount 증가
			bd.readCount(num);
			
			// 4. Board board = bd.select(num);
			Board board = bd.select(num);
			
			// 5. request 객체에 num, pageNum, board    /  JSP 에서 ${num}와 같은 형식으로 해당 값을 사용할 수 있따
			request.setAttribute("num", num);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("board", board);    
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// View
		return "content.jsp";
	}

}
