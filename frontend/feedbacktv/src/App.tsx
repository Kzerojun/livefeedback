import React, { useEffect, useState } from 'react';
import './App.css';
import Authentication from "./views/auth";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useCookies } from "react-cookie";
import Container from "./layouts/container";
import useLoginUserStore from "./stores/login-user.store";
import User from "./types/interface/user.interface";

import DonationBox from "./components/DonationBox";
import Buy from "./views/buy";
import PayBox from "./components/PayBox";
import Station from "./views/station";
import { GetLoginUserResponseDto } from "./types/response/member";
import ResponseDto from "./types/response/response.dto";
import {
  AUTH_PATH,
  BROAD_CAST_PATH,
  CANDY_BUY_PATH,
  DONATION_PATH, MAIN_PATH, MYPAGE_PATH,
  STATION_PATH,
  STREAM_PATH, USER_PATH
} from "./constants/paths";
import { getSignInUserRequest } from "./apis/member";
import Main from "./views/main";
import Stream from "./views/stream";
import BroadCast from "./views/broadcast";
import MyPage from "./views/mypage";

function App() {
  // state : 로그인 유저 전역 상태 //
  const { setLoginUser, resetLoginUser } = useLoginUserStore();
  // state : cookie 상태 //
  const [cookies] = useCookies(['accessToken']);
  // state : 로딩 상태 //
  const [isLoading, setIsLoading] = useState<boolean>(true);

  // effect : accessToken cookie 값이 변경 될때 마다 실행할 함수 //
  useEffect(() => {
    const fetchUser = async () => {
      if (!cookies.accessToken) {
        resetLoginUser();
        setIsLoading(false); // 로딩 상태를 false로 설정
        return;
      }
      try {
        const response = await getSignInUserRequest(cookies.accessToken);
        handleSignInUserResponse(response);
      } catch (error) {
        console.error("Failed to fetch user:", error);
        resetLoginUser();
      } finally {
        setIsLoading(false); // 로딩 상태를 false로 설정
      }
    };

    fetchUser();
  }, [cookies.accessToken, resetLoginUser]);

  // function : get sign in user response 처리 함수 //
  const handleSignInUserResponse = (responseBody: GetLoginUserResponseDto | ResponseDto | null) => {
    if (!responseBody) {
      resetLoginUser();
      return;
    }

    const { code } = responseBody;

    if (code === 'AF' || code === 'NEM' || code === 'DBE' || code !== 'SU') {
      resetLoginUser();
      return;
    }

    const loginUser: User = { ...responseBody as GetLoginUserResponseDto };
    setLoginUser(loginUser);
  };

  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Container />}>
            <Route path={MAIN_PATH()} element={<Main />}/>
            <Route path ={USER_PATH(':userId')} element={<MyPage/>}/>
            <Route path={AUTH_PATH()} element={<Authentication />} />
            <Route path={STATION_PATH(':streamerId')} element={<Station />} />
            <Route path={BROAD_CAST_PATH()} element={<BroadCast />} />
            <Route path={STREAM_PATH(':streamerId', ':broadcastId')} element={<Stream />} />
            <Route path={DONATION_PATH(':donorId', ':recipientId')} element={<DonationBox />} />
            <Route path="/pay" element={<PayBox />} />
          </Route>
          <Route path={CANDY_BUY_PATH()} element={<Buy />} />
        </Routes>
      </BrowserRouter>
  );
}

export default App;
