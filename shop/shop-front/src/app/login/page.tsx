"use client";

import { useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";
import * as S from "@/styled/Login.styles";
//import "@fortawesome/fontawesome-free/css/all.min.css";

export default function Login() {
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!email || !password) {
      alert("이메일과 비밀번호를 입력해 주세요!");
      return;
    }

    try {
      const res = await axios.post("http://localhost:9500/members/login", {
        email,
        password,
      });

      const token = res.data?.token;

      if (!token) {
        alert("로그인 응답에 token이 없습니다. 백엔드 응답을 확인하세요!");
        console.log("login response:", res.data);
        return;
      }

      // 토큰 저장
      localStorage.setItem("token", token);
      localStorage.setItem("lastName", res.data.lastName);
      localStorage.setItem("firstName", res.data.firstName);

      window.dispatchEvent(new Event("storage"));

      alert("로그인 성공");

      // SPA 방식 페이지 이동
      router.push("/admin");
    } catch (err) {
      console.error(err);
      alert("로그인 실패! 이메일 또는 비밀번호를 확인해 주세요");
    }
  };

  return (
    <S.Wrapper>
      <S.Card>
        <S.LeftImage />
        <S.Right>
          <S.Title>Welcome Back!</S.Title>

          <S.Form onSubmit={handleSubmit}>
            <S.Input
              type="email"
              placeholder="Enter Email Address..."
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <S.Input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <S.CheckboxWrapper>
              <input type="checkbox" id="remember" />
              <label htmlFor="remember">Remember Me</label>
            </S.CheckboxWrapper>

            <S.Button type="submit">Login</S.Button>

            <S.Divider />

            <S.SocialButton variant="google">
              <i className="fab fa-google" />
              Login with Google
            </S.SocialButton>

            <S.SocialButton variant="facebook">
              <i className="fab fa-facebook-f" />
              Login with Facebook
            </S.SocialButton>

            <S.SocialButton variant="instagram">
              <i className="fab fa-instagram" />
              Login with Instagram
            </S.SocialButton>
          </S.Form>

          <S.Divider />

          <S.LinkText href="/forgot">Forgot Password?</S.LinkText>
          <S.LinkText href="/member">Create an Account!</S.LinkText>
        </S.Right>
      </S.Card>
    </S.Wrapper>
  );
}
