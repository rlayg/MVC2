package service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

//	CommandProcess를 상속받아야해
//				Service ----> Buz Logic 비니지스 로직
public class ListAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("ListAction Service start..."); //controller 지시를 받고 시작함
		BoardDao bd = BoardDao.getInstance();
		
		try {
			int totCnt = bd.getTotalCnt();	// 37
			System.out.println("ListAction Service totCnt -> " + totCnt);
			
			//request.setAttribute("totCnt", totCnt);
			
			String pageNum = request.getParameter("pageNum");
			if(pageNum==null || pageNum.equals("")) {pageNum = "1";} // null이거나 없으면 1페이지부터 시작해
			int currentPage = Integer.parseInt(pageNum);	//	1  / 현재 페이지를 만들어. 현재 1페이지야
			int pageSize = 10, blockSize = 10; //한페이지에 보여주는 페이지의 블록 / pageSize 가 현재 페이지의 게시글 수
			int startRow = (currentPage - 1) * pageSize + 1; // 1	11
			int endRow	 = startRow + pageSize -1;			 // 10	20
			int startNum = totCnt - startRow + 1; 	// 게시글 번호의 시작 번호를 계산하는 것 
													// 예를 들어, 첫 페이지에서는 37 - 1 + 1 = 37이고, 
													// 두 번째 페이지에서는 37 - 11 + 1 = 27입니다.
			
			//	Board 조회						1		10
			List<Board> list = bd.boardList(startRow, endRow);
			//								 		37	/	10
			int pageCnt = (int)Math.ceil((double)totCnt/pageSize);	//4  / 한페이지라도 10개씩 보여줘. 31개라도 4페이지 나와야해 그래서 ceil
			//						1
			int startPage = (int)(currentPage-1)/blockSize*blockSize + 1;	//1 / currentPage-1을 blockSize로 나눈 후, 정수형으로 형변환합니다 이를 다시 blockSize와 곱하고 1을 더합니다.
																			//    이렇게 하면 currentPage가 속한 블록의 첫 번째 페이지 번호가 계산됩니다.
			int endPage = startPage + blockSize -1;							//10
			//	공갈 Page 방지 10 > 4
			if(endPage > pageCnt) endPage = pageCnt;						//4
			
			request.setAttribute("list", list);		//*** 얘가 핵심 밑에는 페이지 구성 위해 가져오는 애들
			request.setAttribute("totCnt", totCnt);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("startNum", startNum);
			request.setAttribute("blockSize", blockSize);
			request.setAttribute("pageCnt", pageCnt);
			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			
			
		} catch (Exception e) {
			System.out.println("ListAction.e.getMessage() -> " + e.getMessage());
		}
		
		
//		View 명칭	 / View를 지칭	
		return "list.jsp";
	}

}
