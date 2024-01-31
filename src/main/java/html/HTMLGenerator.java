package html;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class HTMLGenerator {
	private static final Logger logger = LoggerFactory.getLogger(HTMLGenerator.class);
	private static final String FILE_PATH = "src/main/resources/templates";

	public static byte[] getHTML(String userName, String uri) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH+uri))) {
			String line;
			while ((line = br.readLine()) != null) {
				//로그인 버튼 로그아웃 버튼으로 바꿈
				if (line.contains("login-button")) {
					line = line.replace("로그인", "로그아웃");
					line = line.replace("user/login.html", "#");
				}
				// 사용자 이름 표시
				if (line.contains("navbar-name-space")) {
					sb.append("<li><span class=\"navbar-text\" style=\"font-weight: 600;\">");
					sb.append(userName).append(" 님 안녕하세요").append("</span></li>").append("\n");
				}
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return sb.toString().getBytes();
	}

	public static byte[] getUserListHTML(String userName) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH+"/user/list.html"))) {
			String line;
			while ((line = br.readLine()) != null) {
				//로그인 버튼 로그아웃 버튼으로 바꿈
				if (line.contains("login-button")) {
					line = line.replace("로그인", "로그아웃");
					line = line.replace("user/login.html", "#");
				}
				// 사용자 이름 표시
				if (line.contains("navbar-name-space")) {
					sb.append("<li><span class=\"navbar-text\" style=\"font-weight: 600;\">");
					sb.append(userName).append(" 님 안녕하세요").append("</span></li>").append("\n");
				}
				sb.append(line).append("\n");
				//테이블 생성
				if (line.contains("user-table")) {
					sb.append("<thead>").append("\n");
					sb.append("<tr>").append("\n");
					sb.append("<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>").append("\n");
					sb.append("</tr>").append("\n");
					sb.append("</thead>").append("\n");
					sb.append("<tbody>").append("\n");
					// 디비에서 읽으면서 사용자 리스트 뿌리기
					int count = 0;
					Collection<User> users = Database.findAll();
					for (User user : users) {
						sb.append("<tr>").append("\n");
						sb.append("<th scope=\"row\">").append(++count).append("</th>");
						sb.append(" <td>").append(user.getUserId()).append("</td>");
						sb.append(" <td>").append(user.getName()).append("</td>");
						sb.append(" <td>").append(user.getEmail()).append("</td>");
						sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>").append("\n");
						sb.append("</tr>").append("\n");
					}
					sb.append("</tbody>").append("\n");
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return sb.toString().getBytes();
	}

	public static byte[] getSignUpHTML(String uri, String message) {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH+uri))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("btn btn-success clearfix pull-right")) {
					sb.append("<span style=\"color: red;font-weight: 500;\">").append(message).append("</span>");
				}
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return sb.toString().getBytes();
	}

}
