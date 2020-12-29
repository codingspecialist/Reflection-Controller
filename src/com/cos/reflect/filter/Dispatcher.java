package com.cos.reflect.filter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.reflect.anno.RequestMapping;
import com.cos.reflect.controller.UserController;

public class Dispatcher implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		System.out.println("컨텍스트패스 : " + request.getContextPath()); // 프로젝트 시작주소
		System.out.println("식별자주소 : " + request.getRequestURI()); // 끝주소
		System.out.println("전체주소 : " + request.getRequestURL()); // 전체주소

		String endPoint = request.getRequestURI().replaceAll(request.getContextPath(), "");
		System.out.println("엔드포인트 : " + endPoint);

		UserController userController = new UserController();
		Method[] methods = userController.getClass().getDeclaredMethods();

		for (Method method : methods) { // 리플렉션한 메서드 개수만큼 순회함

			Annotation annotation = method.getDeclaredAnnotation(RequestMapping.class);
			RequestMapping requestMapping = (RequestMapping) annotation;

			if (requestMapping.uri().equals(endPoint)) {
				System.out.println("리플렉션 컨틀롤러 함수 어노테이션값 : " + requestMapping.uri());
				try {
					Parameter[] params = method.getParameters();
					String path;
					if (params.length != 0) {
						Object dtoInstance = params[0].getType().newInstance();
						setData(dtoInstance, request); // 인스턴스에 파라메터 값 추가하기 (레퍼런스를 넘겨서 리턴 안받아도 됨)
						path = (String) method.invoke(userController, dtoInstance);
					} else {
						path = (String) method.invoke(userController);
					}

					System.out.println("path : " + path);
					RequestDispatcher dis = request.getRequestDispatcher(path);
					dis.forward(request, response);
					break; // 더 이상 메서드를 리플렉션 할 필요 없어서 빠져나감.
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	private String keyToMethodKey(String key) {
		String firstkey = key.substring(0, 1);
		String remainKey = key.substring(1);
		return "set" + firstkey.toUpperCase() + remainKey;
	}

	private <T> void setData(T instance, HttpServletRequest request) {
		System.out.println("인스턴스 타입 : " + instance.getClass());
		Enumeration<String> params = request.getParameterNames();

		while (params.hasMoreElements()) {
			String key = (String) params.nextElement();
			String methodKey = keyToMethodKey(key);
			System.out.println("실행할 setter메서드 :" + methodKey);
			Method[] methods = instance.getClass().getMethods();
			for (Method m : methods) {
				if (m.getName().equals(methodKey)) {
					try {
						m.invoke(instance, request.getParameter(key));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
