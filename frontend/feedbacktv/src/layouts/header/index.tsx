import './style.css'
import {useNavigate, useParams} from "react-router-dom";
import {useCookies} from "react-cookie";
import {ChangeEvent, useEffect, useRef, useState} from "react";
import {KeyboardEvent} from 'react';
import {LogoutResponseDto} from "../../types/response/auth";
import ResponseDto from "../../types/response/response.dto";
import {
  AUTH_PATH,
  BROAD_CAST_PATH,
  MAIN_PATH,
  SEARCH_PATH,
  STATION_PATH,
  USER_PATH
} from "../../constants/paths";
import {logoutRequest} from "../../apis/auth";
import useLoginUserStore from "../../stores/login-user.store";
import {VideoCameraTwoTone} from '@ant-design/icons';

export default function Header() {

  const navigate = useNavigate();

  // state : 로그인 유저 상태  //
  const {loginUser, resetLoginUser} = useLoginUserStore();

  // state : cookie 상태     //
  const [cookies, setCookie] = useCookies();

  // state : 로그인 상태 //
  const [isLogin, setLogin] = useState<boolean>(false);

  // state : 인증 페이지 상태     //
  const [isAuthPage, setAuthPage] = useState<boolean>(false);
  // state : 메인 페이지 상태     //
  const [isMainPage, setMainPage] = useState<boolean>(false);
  // state : 검색 페이지 상태     //
  const [isSearchPage, setSearchPage] = useState<boolean>(false);
  // state : 유저 페이지 상태     //
  const [isUserPage, setUserPage] = useState<boolean>(false);

  // event handler : 로고 클릭 이벤트 처리 함수 //
  const onLogoClickHandler = () => {
    navigate(MAIN_PATH());
  }

  //     component : 검색 버튼 컴포넌트     //
  const SearchButton = () => {

    //     state : 검색 버튼 요소 참조 상태      //
    const searchButtonRef = useRef<HTMLDivElement | null>(null);

    //     state : 검색어 상태     //
    const [word, setWord] = useState<string>('');


    //     event handler : 검색어 변경 이벤트 처리 함수     //
    const onSearchWordChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      const value = event.target.value;
      setWord(value);
    }

    //     event handler : 검색어 키 이벤트 처리 함수     //
    const onSearchWordKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) => {
      if (event.key !== 'Enter') return;
      if (!searchButtonRef) return;

      navigate(SEARCH_PATH(word));
    };

    //     render : 검색 버튼 컴포넌트 렌더링( 클릭 true 상태)    //
    return (
        <div className='header-search-input-box'>
          <input className='header-search-input' type='text' placeholder='검색어를 입력해주세요.'
                 value={word} onChange={onSearchWordChangeHandler}
                 onKeyDown={onSearchWordKeyDownHandler}/>
          <div className='search-icon-box'>
            <div className='icon search-light-icon'></div>
          </div>

        </div>
    );
  };

  // component : 로그인 또는 마이페이지 버튼 컴포넌트
  const MyPageButton = () => {

    //     event handler : 마이페이지 버튼 클릭 이벤트 처리 함수      //
    const onMyPageButtonClickHandler = () => {
      if (!loginUser) return;
      console.log(loginUser);
      const{userId} = loginUser
      navigate(USER_PATH(userId));
    };

    //     event handler : 로그아웃 버튼 클릭 이벤트 처리 함수      //
    const onSignOutButtonClickHandler = () => {
      resetLoginUser();
      const accessToken = cookies.accessToken;
      if (!accessToken) {
        navigate(MAIN_PATH());
        return;
      }

      logoutRequest(cookies.accessToken).then(logoutResponse)
      navigate(MAIN_PATH());
    };

    //     event handler : 로그인 버튼 클릭 이벤트 처리 함수      //
    const onSignInButtonClickHandler = () => {
      navigate(AUTH_PATH());
    };

    const onMyStationButtonClickHandler = () =>{
      if (!loginUser) return;
      const {userId} = loginUser;
      navigate(STATION_PATH(userId));
    }

    const onBroadcastButtonClickHandler = () =>{
      if(!loginUser) return;
      navigate(BROAD_CAST_PATH());
    }

    // function : logout response 처리함수
    const logoutResponse = (responseBody: LogoutResponseDto | ResponseDto | null) => {
      if (!responseBody) return;
      const {code} = responseBody;

      if (code === 'NPT') alert('토큰 권한이 없습니다.');
      if (code === 'DBE') alert('데이터베이스 오류입니다.');

      setCookie('accessToken', '', {path: MAIN_PATH(), expires: new Date()})
      setCookie('refreshToken','',{path: MAIN_PATH(), expires: new Date()})
    }

    //     render : 로그아웃 및 마이페이지 버튼 컴포넌트 렌더링   //
    if (isLogin) {
      return (
          <>
            <div className='broadcast-icon-box'>
              <VideoCameraTwoTone onClick={onBroadcastButtonClickHandler} style={{ fontSize: '35px' }} />
            </div>
            <div className='mypage-button' onClick={onMyPageButtonClickHandler}>{'마이페이지'}</div>
            <div className='mystation-button'
                 onClick={onMyStationButtonClickHandler}>{'내 방송국'}</div>
            <div className='logout-button' onClick={onSignOutButtonClickHandler}>{'로그아웃'}</div>
          </>
      )
    }

    //     render : 로그인 버튼 컴포넌트 렌더링   //
    return <div className='login-button' onClick={onSignInButtonClickHandler}>{'로그인'}</div>
  }

  //      effect : 로그인 유저가 변경될 때 마다 실행될 함수     ///
  useEffect(() => {
    setLogin(loginUser !== null);
  }, [loginUser])

  return (
      <div id='header'>
        <div className='header-container'>
          <div className='header-left-box' onClick={onLogoClickHandler}>
            <div className='header-logo'>{'FeedBack'}<span
                className='header-logo-part'>{'TV'}</span></div>
          </div>

          <div className='header-right-box'>
            <SearchButton/>
            <MyPageButton/>
          </div>
        </div>
      </div>
  )
};