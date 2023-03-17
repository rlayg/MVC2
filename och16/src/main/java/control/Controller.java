package control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.CommandProcess;

/**
 * Servlet implementation class Controller
 */
// xml에 param 등 써서 여기서는 @WebServlet을 안써
// @WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Map<String, Object> commandMap = new HashMap<String, Object>();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
    //                             /WEB-INF/command.properties
	public void init(ServletConfig config) throws ServletException {
		// 초기화 작업, 일생에 단 한번 하는거
		// web.xml에서 propertyConfig에 해당하는 init-param의 값을 읽어옴
		String props = config.getInitParameter("config");  //config는 properties 주소다
		// /WEB-INF/command.properties
		System.out.println("1. init String props => " + props); // /ch16/com / props이 여기까지는 String 이야
		
		Properties		pr = new Properties();		
		FileInputStream f = null;					//여기서 파일로 만들어줘
		
		
		try {
			String configFilePath = config.getServletContext().getRealPath(props);		//여기서 톰캣서버로 바뀜
			System.out.println("2. init String configFilePath => " + configFilePath); // /ch16
			f = new FileInputStream(configFilePath);
			// Memory Up
			pr.load(f);			//메모리로 올라가
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(f != null)
				try {
					f.close();
				} catch (IOException ex) {
	
				}
		}
		//		/ list.do - service.ListAction
		//		/content.do=service.ContentAction
		//		/list.do / content.do -키셋이 이렇게 두개(아직 2개밖에 안해서 그런듯 더 하면 더늘수도)
		Iterator keyIter = pr.keySet().iterator();  // Iterator 이거 자체가 디자인패턴
		
		while(keyIter.hasNext()) { // has는 가져오는게 아니라 있냐 물어보는거
			String command = (String) keyIter.next();
			String className = pr.getProperty(command);
			System.out.println("3. init command => " + command); // /och/com
			System.out.println("4. init className => " + className); 	// 여기까지는 String으로 class명
			
			try {
				// ListAction la = new ListAction();
				// 소멸 Class
			// Class commandClass		= Class.forName(className);
			// Object commandInstance	= commandClass.newInstance();
				// new Class	--> 제네릭의 요점은 클래스 유형을 모른다
				Class<?> commandClass = Class.forName(className); //해당 문자열을 진짜 클래스로 만든다.
				CommandProcess commandInstance =
						(CommandProcess)commandClass.getDeclaredConstructor().newInstance();
				
				//	 			list.do		service.ListAction
				//				content.do	service.ContentAction
				
				commandMap.put(command, commandInstance);   // 여기에 command.properties의 키,값이 들어간다
				
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("init e.getMessage --> " + e.getMessage());
			}
		}
	}
	/*
	서비스가 먼저 시작된다 밑에 doGet, doPost보다 먼저 실행된다 근데 생략되어있다
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
	}

	*/
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		requestServletPro(request, response); // 난 get방식 post 상관없이 똑같이 처리할거야
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		requestServletPro(request, response); // 난 get방식 post 상관없이 똑같이 처리할거야
	}
	
	protected void requestServletPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String view			= null;
		CommandProcess com  = null;
		String command		= request.getRequestURI();
		System.out.println("1. requestServletPro command => " + command); //och16/list.do
		command = command.substring(request.getContextPath().length()); // 프로젝트 명을 잘라내는 과정이다 /och16 잘라내는 과정
		System.out.println("2. requestServletPro command substring => " + command); // /och16
		
		try {
			//	service.ListAction Instance
			com = (CommandProcess) commandMap.get(command);
			System.out.println("3. requestServletPro command => " + command); // /och16/com
			System.out.println("4. requestServletPro com => " + com); // /och16/com 해시코드다 --> 인스턴스를 가리키고 있따
			//	com --> service.ListAction@32a22787
			view = com.requestPro(request, response);
			System.out.println("5. requestServletPro view => " + view); // /ch16/com
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		// Ajax String을 포함하고 있으면
		if(command.contains("ajaxGet")) {
			System.out.println("ajaxGet String -> " + command);
			String writer = (String) request.getAttribute("writer");
			PrintWriter pw = response.getWriter();
			pw.write(writer);
			pw.flush(); // 아작스는 스프링가서 한대
			
		} else {
			//일반적인 경우
			RequestDispatcher dispatcher = request.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		}
	}
	
}
