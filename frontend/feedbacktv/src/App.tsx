import React, {useEffect} from 'react';
import './App.css';
import Authentication from "./views/auth";
import Header from "./layouts/header";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useCookies } from "react-cookie";
import useLoginUserStore from "./stores/login-user.store";
import Container from "./layouts/container";
import {AUTH_PATH, STATION_PATH} from "./constants";
import Station from "./views/station";
import ResponseDto from "./apis/response/response.dto";
import GetLoginUserResponseDto from "./apis/response/member/get-login-user.response.dto";
import User from "./types/interface/user.interface";
import {getSignInUserRequest, logInRequest} from "./apis";

function App() {

  // state : 로그인 유저 전역 상태 //
  const { setLoginUser, resetLoginUser } = useLoginUserStore();
  // state : cookie 상태 //
  const [cookies, setCookie] = useCookies();

  // effect : accessToken cookie 값이 변경 될때 마다 실행할 함수 //
  useEffect(() => {
    if (!cookies.accessToken) {
      resetLoginUser();
      return;
    }

    getSignInUserRequest(cookies.accessToken).then(getSignInUserResponse)
  }, [cookies.accessToken])


  //function : get sign in user response 처리 함수  //
  const getSignInUserResponse = (responseBody: GetLoginUserResponseDto | ResponseDto | null) => {
    if (!responseBody) return;

    const {code} = responseBody;

    if (code === 'AF' || code === 'NEM' || code === 'DBE') {
      resetLoginUser();
      return;
    }

    const loginUser: User = {...responseBody as GetLoginUserResponseDto};
    setLoginUser(loginUser);
  }

  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Container />} />
          <Route path={AUTH_PATH()} element={<Authentication/>}/>
          <Route path={STATION_PATH(':userId')} element={<Station/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
