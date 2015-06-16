package com.listen.member.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.listen.base.controller.BaseController;
import com.listen.chatting.dao.ChattingDao;
import com.listen.member.dao.MemberDao;
import com.listen.member.vo.MemberVo;
import com.listen.notice.dao.NoticeDao;

@Controller
public class LoginAction extends BaseController {

	private MemberDao memberDao;
	private MemberVo memberVo;
	private ChattingDao chattingDao;
	private NoticeDao noticeDao;

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public void setChattingDao(ChattingDao chattingDao) {
		this.chattingDao = chattingDao;
	}

	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	@RequestMapping("/Login.listen")
	// index.jsp 에서 들어오는 listen
	public String testmainPage(HttpServletRequest request, HttpSession session) {

		return "member/Login";
	}

	@RequestMapping("/loginAction.listen")
	   // login.jsp에서 submit하면 들어오는 listen
	   public String login(HttpServletRequest request, HttpSession session) {
	      String id = (String) request.getParameter("username");
	      String pass = (String) request.getParameter("password");
	      String latitude = (String) request.getParameter("latitude");
	      String longitude = (String) request.getParameter("longitude");
	      String email = "";
	      String password = "";
	      

	      ArrayList EmailList = memberDao.getEmailList(id);
	      if (EmailList.size() != 0) {
	         MemberVo mv = (MemberVo) EmailList.get(0);
	         email = (String) mv.getEmail();
	         password = (String) mv.getPassword();
	         
	         System.out.println(email);
	         System.out.println("chatPage 들어옴");
	         ArrayList chatList = chattingDao.getChattinglist(email);
	         session.setAttribute("chatList",  chatList);
	         System.out.println(chatList.size());
	         ArrayList noticeList = noticeDao.getNoticelist(email);
	         session.setAttribute("noticeList",  noticeList);
	         System.out.println(noticeList.size());
	      }

	      memberVo = new MemberVo();
	      if (EmailList.size() != 0 && pass.equals(password)) {
	         MemberVo memberVo = (MemberVo) EmailList.get(0);
	         session.setAttribute("email", memberVo.getEmail());
	         session.setAttribute("pass", memberVo.getPassword());

	         session.setAttribute("LoginYn", "Y");
	         session.setAttribute("selectItem", "main");
	         memberVo.setEmail(email);
	         memberVo.setLatitude(latitude);
	         memberVo.setLongitude(longitude);
	         memberDao.locationUpdate(memberVo);
	         return "redirect:/main.listen";
	      } else {
	         session.setAttribute("Error", "N");
	         return "member/testLogin";
	      }
	   }

	
	@RequestMapping("/m_loginAction.listen")
	   // login.jsp에서 submit하면 들어오는 listen
	   public String m_login(HttpServletRequest request, HttpSession session) {
	      String id = (String) request.getParameter("username");
	      String pass = (String) request.getParameter("password");
	      String latitude = (String) request.getParameter("latitude");
	      String longitude = (String) request.getParameter("longitude");
	      String email = "";
	      String password = "";
	      

	      ArrayList EmailList = memberDao.getEmailList(id);
	      if (EmailList.size() != 0) {
	         MemberVo mv = (MemberVo) EmailList.get(0);
	         email = (String) mv.getEmail();
	         password = (String) mv.getPassword();
	         
	         System.out.println(email);
	         System.out.println("chatPage 들어옴");
	         ArrayList chatList = chattingDao.getChattinglist(email);
	         session.setAttribute("chatList",  chatList);
	         System.out.println(chatList.size());
	         ArrayList noticeList = noticeDao.getNoticelist(email);
	         session.setAttribute("noticeList",  noticeList);
	         System.out.println(noticeList.size());
	      }

	      memberVo = new MemberVo();
	      if (EmailList.size() != 0 && pass.equals(password)) {
	         MemberVo memberVo = (MemberVo) EmailList.get(0);
	         session.setAttribute("email", memberVo.getEmail());
	         session.setAttribute("pass", memberVo.getPassword());

	         session.setAttribute("LoginYn", "Y");
	         session.setAttribute("selectItem", "main");
	         memberVo.setEmail(email);
	         memberVo.setLatitude(latitude);
	         memberVo.setLongitude(longitude);
	         memberDao.locationUpdate(memberVo);
	         return "redirect:/m_main.listen";
	      } else {
	         session.setAttribute("Error", "N");
	         return "member/testLogin";
	      }
	   }
	
	
	@RequestMapping("/Logout.listen")
	// 로그아웃
	public String logout(HttpServletRequest request, HttpSession session) {
		System.out.println("Logout!!!!!");
		session = request.getSession(false);
		session.setAttribute("LoginYn", "N");
		session.setAttribute("email", null);
		return "member/Login";
	}

	@RequestMapping("/join.listen")
	// join.jsp로 진입
	public String join(HttpServletRequest request, HttpSession session) {
		return "member/join";
	}

	@RequestMapping("/joinResult.listen")
	// 회원가입 결과 submit
	public String joinResult(HttpServletRequest request, MemberVo memberVo) {
		String email_id = (String) request.getParameter("email_id");
		String email_kind = (String) request.getParameter("email_domain");
		String password = (String) request.getParameter("password");
		String birthyear = (String) request.getParameter("birthyear");
		String gender = (String) request.getParameter("gender");
		String realId = email_id + "@" + email_kind;
		String latitude = (String) request.getParameter("latitude");
		String longitude = (String) request.getParameter("longitude");

		if (gender.equals("01")) {
			gender = "1";
		} else if (gender.equals("02")) {
			gender = "2";
		}
		memberVo.setLatitude(latitude);
		memberVo.setLongitude(longitude);
		memberVo.setRealId(realId);
		memberVo.setPassword(password);
		memberVo.setBirthyear(birthyear);
		memberVo.setGender(gender);
		memberDao.join(memberVo);

		request.setAttribute("mainUrl", prefix + "member/Login.jsp");
		return "redirect:/Login.listen";
	}

	@RequestMapping("/emailCheck.listen")
	// id중복 확인
	public String idCheck(HttpServletRequest request, HttpSession session) {
		String realId = (String) request.getParameter("realId");

		ArrayList EmailList = memberDao.getCheckEmail(realId);
		memberVo = new MemberVo();
		if (EmailList.size() == 0) {
			request.setAttribute("realId", realId);

			request.setAttribute("code", "1");
			return "member/idCheck";
		} else {

			request.setAttribute("realId", realId);
			request.setAttribute("code", "2");
			return "member/idCheck";
			// return "redirect:/member/idCheck.jsp?code=2";
		}
	}

	@RequestMapping("/memberInfo.listen")
	// 회원정보보기
	public String memberInfo(HttpServletRequest request, HttpSession session) {
		String id = (String) session.getAttribute("id");

		String email = "";
		String password = "";
		String gender = "";
		String birthyear = "";
		ArrayList InfoList = memberDao.getInfoList(id);
		if (InfoList.size() != 0) {
			MemberVo mv = (MemberVo) InfoList.get(0);
			email = (String) mv.getEmail();
			password = (String) mv.getPassword();
			gender = (String) mv.getGender();
			birthyear = (String) mv.getBirthyear();
		}
		if (gender.equals("1")) {
			gender = "남자";
		}
		if (gender.equals("2")) {
			gender = "여자";
		}
		System.out.println(gender);
		request.setAttribute("email", email);
		request.setAttribute("password", password);
		request.setAttribute("gender", gender);
		request.setAttribute("birthyear", birthyear);

		return "member/memberInfo";
	}

	@RequestMapping("/memberUpdate.listen")
	// 회원정보수정 페이지진입
	public String memberUpdate(HttpServletRequest request, HttpSession session) {
		return "member/memberUpdate";
	}

	@RequestMapping("/update.listen")
	// 회원정보 수정하기 submit
	public String update(HttpServletRequest request, HttpSession session) {
		String realId = (String) session.getAttribute("email");
		String password = (String) request.getParameter("password");
		String birthyear = (String) request.getParameter("birthyear");
		String gender = (String) request.getParameter("gender");

		if (gender.equals("01")) {
			gender = "1";
		} else if (gender.equals("02")) {
			gender = "2";
		}
		System.out.println(realId + " " + password + " " + birthyear + " "
				+ gender);
		memberVo.setRealId(realId);
		memberVo.setPassword(password);
		memberVo.setBirthyear(birthyear);
		memberVo.setGender(gender);
		memberDao.memberUpdate(memberVo);
		request.setAttribute("page", "main");
		request.setAttribute("mainUrl", prefix + "member/Login.jsp");
		return frame;
	}

	// //////////////////////////////////////////////////////////////////////////////////////pc끝
	@RequestMapping("/mLogin.listen")
	// 모바일 로그인
	public String mLogin(HttpServletRequest request, HttpSession session) {

		return "";
	}
}
