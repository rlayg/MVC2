package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.Board;

public class dadao {

	public Board select(int num) throws SQLException {
		Connection conn = null;    
		Statement stmt = null;		
		ResultSet rs = null;
		String sql = "select * from board where num = " + num;
		Board board = new Board();
		
		try {
			conn = getConnection();			// 데이터베이스와 연결되어 있는 상태
			stmt = conn.createStatement();	// Connection 객체를 통해 데이터베이스와 연결된 상태에서 Statement 객체를 생성
											// Statement 객체는 SQL 쿼리문을 실행하기 위한 메서드들을 제공
											// 이를 통해 데이터베이스에 쿼리를 보내고, 쿼리 결과를 받아와서 처리할 수 있음
			rs = stmt.executeQuery(sql); //map set list  / executeQuery() 메서드를 사용하여 SQL 쿼리를 실행
										 // 각 dto를 저장
			

			/*
			executeQuery() 메서드는 SQL 쿼리문을 실행한 결과로 ResultSet 객체를 반환합니다. ResultSet 객체는 SQL 쿼리문 실행 결과 집합(ResultSet)을 나타냅니다. 이 집합에는 테이블의 한 개 이상의 레코드와 컬럼의 값이 포함될 수 있습니다.

			ResultSet 객체는 쿼리문 실행 결과를 커서(cursor)를 이용하여 탐색할 수 있도록 제공됩니다. 커서는 결과 집합을 하나씩 순서대로 읽어가면서 결과를 처리할 때 사용합니다.
			
			executeQuery() 메서드의 호출 결과로 반환되는 ResultSet 객체는 커서의 초기 위치가 첫 번째 레코드 이전에 위치하게 됩니다. ResultSet 객체의 next() 메서드를 호출하면 커서가 다음 레코드를 가리키게 됩니다. 이를 통해 다음 레코드가 존재하는지 확인할 수 있습니다.
			
			또한, ResultSet 객체는 결과 집합에서 특정 레코드나 컬럼의 값에 접근하기 위한 여러 메서드를 제공합니다. 예를 들어, getInt(), getString() 등의 메서드를 이용하여 컬럼의 값에 접근할 수 있습니다. 이를 통해 데이터베이스에서 가져온 결과 집합을 애플리케이션에서 활용할 수 있습니다.	
			*/
			
			/*
			if(rs.next()) {
				int a = rs.getInt("num");
				board.setNum(a);
				String b = rs.getString(2);
				board.setWriter(b);
				String c = rs.getString("subject");
				board.setSubject(c);
				String d =  rs.getString(5);
				board.setEmail(d);
				board.setPasswd(rs.getString("passwd")); 
				
			}
			
			}catch (Exception e) {
				// TODO: handle exception
			} 
			return board;
			
			*/
			
			if(rs.next()) {		//next는 boolean을 반환
				board.setNum(rs.getInt("num"));				//set에서 가져온 int타입 컬럼명 or 컬럼번호를 넣고 dto에 저장
				board.setWriter(rs.getString("writer"));		
				board.setSubject(rs.getString("subject"));
				board.setContent(rs.getString("content"));
				board.setEmail(rs.getString("email"));
				board.setReadcount(rs.getInt("readcount"));
				board.setIp(rs.getString("ip"));
				board.setReg_date(rs.getDate("reg_date"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));			//dto라는 바구니에 저 값들을 담음
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		}
		return board;
	}
	
	//--------------------------------------------
	
	public Board select(int num) throws SQLException {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql = "SELECT * FROM board WHERE num = ?";
	    Board board = new Board();

	    try {
	        conn = getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, num);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            board.setNum(rs.getInt("num"));
	            board.setWriter(rs.getString("writer"));
	            board.setSubject(rs.getString("subject"));
	            board.setContent(rs.getString("content"));
	            board.setEmail(rs.getString("email"));
	            board.setReadcount(rs.getInt("readcount"));
	            board.setIp(rs.getString("ip"));
	            board.setReg_date(rs.getDate("reg_date"));
	            board.setRef(rs.getInt("ref"));
	            board.setRe_level(rs.getInt("re_level"));
	            board.setRe_step(rs.getInt("re_step"));
	        }

	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    } finally {
	    }
	    return board;
	}
	
	
	
	
	//========================================================================
	
	private Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Board> boardList(int startRow, int endRow) throws SQLException {
//		Board 객체들을 담을 수 있는 ArrayList를 생성
		List<Board> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * "
				   + "FROM (Select rownum rn, a.* "
				   + "     From (select * from board order by ref desc, re_step) a ) "
				   + "WHERE rn BETWEEN ? AND ? " ; //보통 페이징작업 between안하고 이렇게 서브쿼리 함 빗윈으로만하면 글 삭제했을때 이빨빠져있음
					
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Board board = new Board();
				System.out.println("board -> " + board);
				board.setNum(rs.getInt("num"));
				board.setWriter(rs.getString("writer"));
				board.setSubject(rs.getString("subject"));
//				board.setContent(rs.getString("content"));
				board.setEmail(rs.getString("email"));
				board.setReadcount(rs.getInt("readcount"));
				board.setIp(rs.getString("ip"));
				board.setRef(rs.getInt("ref"));
				board.setRe_level(rs.getInt("re_level"));
				board.setRe_step(rs.getInt("re_step"));
				board.setReg_date(rs.getDate("reg_date"));
				
				list.add(board);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		return list;
	}

	
	
	
	
	
	
}
