import {ChangeEvent, useRef, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useCookies} from "react-cookie";
import InputBox from "../../components/InputBox";
import {KeyboardEvent} from 'react';
import './style.css';
import {LoginResponseDto, SignUpResponseDto} from "../../types/response/auth";
import ResponseDto from "../../types/response/response.dto";
import {LoginRequestDto, SignUpRequestDto} from "../../types/request/auth";
import {MAIN_PATH} from "../../constants/paths";
import {logInRequest, signUpRequest} from "../../apis/auth";
import {message} from "antd";

export default function Authentication() {

  //     state : 화면 상태     //
  const [view, setView] = useState<'login' | 'sign-up'>('login');

  //     state : 쿠키 상태     //
  const [cookies, setCookie] = useCookies();

  //    function : 네비게이트 함수     //
  const navigate = useNavigate();


  //     component : sign in card 컴포넌트     //
  const LoginInCard = () => {

    //    state : 이메일 요소 참조 상태    ///
    const loginRef = useRef<HTMLInputElement | null>(null);

    //    state : 패스워드 요소 참조 상태    ///
    const passwordRef = useRef<HTMLInputElement | null>(null);

    //    state : 이메일 상태     //
    const [loginId, setLoginId] = useState<string>('');

    //    state : 패스워드 상태     //
    const [password, setPassword] = useState<string>('');

    //    state : 패스워드 타입 상태     //
    const [passwordType, setPasswordType] = useState<'text' | 'password'>('password');

    //    state : 패스워드 버튼 아이콘 상태     ///
    const [passwordButtonIcon, setPasswordButtonIcon] = useState<'eye-light-on-icon' | 'eye-light-off-icon'>('eye-light-off-icon')

    //    state : 에러 상태     //
    const [error, setError] = useState<boolean>(false);


    //    function : log in response 처리 함수     //
    const logInResponse = (responseBody: LoginResponseDto | ResponseDto | null) => {
      if (!responseBody) {
        message.error('서버 에러입니다.');
        return;
      }

      const {code} = responseBody;
      if (code === 'LF' || code === 'VF') {
        message.error("아이디 또는 비밀번호가 일치하지 않습니다.");
        setError(true);
        return;
      }

      if (code !== 'SU') {
        message.error('서버 에러입니다.');
        return;
      }

      const {
        accessToken,
        refreshToken,
        accessTokenExpirationTime,
        refreshTokenExpirationTime
      } = responseBody as LoginResponseDto

      const now = new Date().getTime();
      const accessTokenExpires = new Date(now + accessTokenExpirationTime * 1000);
      setCookie('accessToken', accessToken, {expires: accessTokenExpires, path: '/'});

      const refreshTokenExpires = new Date(now + refreshTokenExpirationTime * 1000);
      setCookie('refreshToken', refreshToken, {
        expires: refreshTokenExpires,
        path: '/',
        httpOnly: true
      }
      );

      navigate(MAIN_PATH());
      message.success('로그인이 완료되었습니다.');

      setTimeout(() => {
        navigate(MAIN_PATH());
      }, 10);
    }


    //     event handler : 이메일 변경 이벤트 처리     //
    const onEmailChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      setError(false);
      const {value} = event.target;
      setLoginId(value);
    }

    //     event handler : 비밀번호 변경 이벤트 처리     //
    const onPasswordChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      setError(false);
      const {value} = event.target;
      setPassword(value);
    }

    //     event handler : 로그인 버튼 클릭 이벤트 처리     //
    const onSignInButtonClickHandler = () => {
      const requestBody: LoginRequestDto = {loginId, password};
      if(error) {
        return;
      }
      logInRequest(requestBody).then(logInResponse)
    }

    //     event handler : 회원가입 버튼 클릭 이벤트 처리     //
    const onSignUpLinkClickHandler = () => {
      setView('sign-up');
    }

    //     event handler : 패스워드 버튼 클릭 이벤트 처리 함수     //
    const onPasswordButtonClickHandler = () => {
      if (passwordType === 'text') {
        setPasswordType('password');
        setPasswordButtonIcon('eye-light-off-icon');
      } else {
        setPasswordType('text');
        setPasswordButtonIcon('eye-light-on-icon');
      }
    }

    //     event handler : 이메일 인풋 키 다운 이벤트 처리    //
    const onEmailKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) => {
      if (event.key !== 'Enter') return;
      if (!passwordRef.current) return;
      passwordRef.current.focus();
    }

    //     event handler : 비밀번호 인풋 키 다운 이벤트 처리    //
    const onPasswordKeyDownHandler = (event: KeyboardEvent<HTMLInputElement>) => {
      if (event.key !== 'Enter') return;
      onSignInButtonClickHandler();
    }

    //     render : sign in 컴포넌트  렌더링   //
    return (
        <div className='auth-card'>
          <div className='auth-card-box'>
            <div className='auth-card-top'>
              <div className='auth-card-title-box'>
                <div className='auth-card-title-logo'>{'FeedBack'}<span
                    className='auth-card-logo-part'>{'TV'}</span></div>
              </div>
              <InputBox label='아이디' type='text' placeholder='아이디를 입력해주세요.'
                        error={error} value={loginId} onChange={onEmailChangeHandler}
                        onKeyDown={onEmailKeyDownHandler}/>
              <InputBox label='패스워드' type={passwordType}
                        placeholder='비밀번호를 입력해주세요.' error={error} value={password}
                        onChange={onPasswordChangeHandler} icon={passwordButtonIcon}
                        onButtonClick={onPasswordButtonClickHandler}
                        onKeyDown={onPasswordKeyDownHandler}/>
            </div>

            <div className='auth-card-bottom'>

              {error &&
                  <div className='auth-sign-in-error-box'>
                    <div className='auth-sign-in-error-message'>
                      {'아이디 또는 비밀번호를 잘못 입력했습니다. \n 입력하신 내용을 다시 확인해주세요 '}
                    </div>
                  </div>}

              <div className='black-large-full-button'
                   onClick={onSignInButtonClickHandler}>{'로그인'}</div>
              <div className='pink-large-full-button'
                   onClick={onSignUpLinkClickHandler}>{'회원가입'}</div>

              <div className="auth-sign-in-social-login">
                <hr className="divider"/>
                <span className="text">SNS 간편 로그인</span>
                <hr className="divider"/>
              </div>
            </div>
          </div>
        </div>);
  };

  const SignUpCard = () => {

    // state : 로그인 아이디 상태 //
    const [loginId, setLoginId] = useState<string>('');

    // state : 비밀번호 상태 //
    const [password, setPassword] = useState<string>('');

    // state : 비밀번호 상태 //
    const [passwordCheck, setPasswordCheck] = useState<string>('');


    // state : 닉네임 상태 //
    const [nickname, setNickname] = useState<string>('');

    // state : 로그인 아이디 에러 상태 //
    const [isLoginError, setIsLoginError] = useState<boolean>(false);

    // state : 패스워드 에러 상태 //
    const [isPasswordError, setIsPasswordError] = useState<boolean>(false);

    // state : 패스워드 에러 상태 //
    const [isPasswordCheckError, setIsPasswordCheckError] = useState<boolean>(false);

    // state : 닉네임 에러 상태 //
    const [isNicknameError, setIsNickNameError] = useState<boolean>(false);

    // state : 로그인 아이디 에러 메시지 상태 //
    const [loginIdErrorMessage, setLoginIdErrorMessage] = useState<string>('');

    // state : 비밀번호 에러 메시지 상태 //
    const [passwordErrorMessage, setPasswordErrorMessage] = useState<string>('');

    // state : 비밀번호 에러 메시지 상태 //
    const [passwordCheckErrorMessage, setPasswordCheckErrorMessage] = useState<string>('');

    // state : 닉네임 에러 메시지 상태 //
    const [nicknameErrorMessage, setNicknameErrorMessage] = useState<string>('');

    //      function : sign up response 처리 함수    //
    const signUpResponse = (responseBody: SignUpResponseDto | ResponseDto | null) => {
      if (!responseBody) {
        alert('네트워크 이상입니다.');
        return;
      }

      const {code} = responseBody;

      if (code === 'DLI') {
        setIsLoginError(true);
        setLoginIdErrorMessage('중복되는 아이디 입니다.');
      }
      if (code === 'DN') {
        setIsNickNameError(true);
        setNicknameErrorMessage('중복되는 닉네임 입니다.');
      }

      if (code === 'VF') {
        alert('모든 값을 입력하세요.')
      }

      if (code !== 'SU') return;

      message.success("회원가입이 완료되었습니다.");
      setView('login');
    }

    // event handler : 로그인 아이디 변경 처리 //
    const onLoginIdChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      const {value} = event.target;
      setLoginId(value);
      setIsLoginError(false);
      setLoginIdErrorMessage('');
    }

    // event handler : 비밀번호 변경 처리 //
    const onPasswordChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      const {value} = event.target;
      setPassword(value);
      setIsPasswordError(false);
      setPasswordErrorMessage('');
    }

    // event handler : 비밀번호 확인 변경 처리 //
    const onPasswordCheckChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      const {value} = event.target;
      setPasswordCheck(value);
      setIsPasswordCheckError(false);
      setPasswordCheckErrorMessage('');
    }

    // event handler : 닉네임 변경 처리 //
    const onNicknameChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      const {value} = event.target;
      setNickname(value);
      setIsNickNameError(false);
      setNicknameErrorMessage('');
    }

    //event handler : 로그인 링크 버튼 클릭 처리 //
    const onSignInLinkClickHandler = () => {
      setView("login")
    }

    const onSignUpButtonClickHandler = () => {
      const loginIdPattern = /^[A-Za-z0-9]{6,15}$/;
      const isLoginPattern = loginIdPattern.test(loginId);

      if (!isLoginPattern) {
        setIsLoginError(true);
        setLoginIdErrorMessage('아이디를 확인해주세요.')
      }

      const passwordPattern = /^(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;
      const isPasswordPattern = passwordPattern.test(password);

      if (!isPasswordPattern) {
        setIsPasswordError(true);
        setPasswordErrorMessage('비밀번호 8~15자 이내 대 소문자, 숫자, 특수문자입니다.');
      }

      const isEqualPassword = password === passwordCheck;
      if (!isEqualPassword) {
        setIsPasswordCheckError(true);
        setPasswordCheckErrorMessage("비밀번호가 일치하지않습니다.");
      }

      const nicknamePattern = /^[가-힣A-Za-z0-9!@#$%^&*()_+]{2,7}$/;
      const isNicknamePattern = nicknamePattern.test(nickname);

      if (!isNicknamePattern) {
        setIsNickNameError(true);
        setNicknameErrorMessage('닉네임을 확인해주세요.');
      }

      if (!isLoginPattern || !isPasswordPattern || !isEqualPassword || !isNicknamePattern) {
        return;
      }

      const requestBody: SignUpRequestDto = {
        loginId, password, nickname
      };


      signUpRequest(requestBody).then(signUpResponse);
    }

    return (
        <div className='auth-card'>
          <div className='auth-card-box'>
            <div className='auth-card-top'>
              <div className='auth-card-title-box'>
                <div className='auth-card-title-logo'>FeedBack<span className='auth-card-logo-part'>TV</span> </div>
              </div>
              <InputBox label={'아이디'} type={"text"} onChange={onLoginIdChangeHandler}
                        error={isLoginError} placeholder={'아이디(6자~15 영문,숫자)'} value={loginId}
                        message={loginIdErrorMessage}
              />
              <InputBox label={'비밀번호'} type={"password"} onChange={onPasswordChangeHandler}
                        error={isPasswordError}
                        placeholder={'비밀번호(8~15 이내 대 소문,숫자,특수문자)'} value={password}
                        message={passwordErrorMessage}
              />
              <InputBox label={'비밀번호 확인'} type={"password"} onChange={onPasswordCheckChangeHandler}
                        error={isPasswordCheckError}
                        placeholder={'비밀번호를 다시 입력해주세요.'} value={passwordCheck}
                        message={passwordCheckErrorMessage}
              />
              <InputBox label={'닉네임'} type={'text'} onChange={onNicknameChangeHandler}
                        error={isNicknameError} placeholder={'닉네임(2~7 국문,영어,숫자,특수문자)'}
                        value={nickname} message={nicknameErrorMessage}
              />

            </div>
            <div className='auth-card-bottom'>
              <div className='black-large-full-button'
                   onClick={onSignUpButtonClickHandler}>{'회원가입'}
              </div>
              <div className='auth-description-box'>
                <div className='auth-description'>{'이미 계정이 있으신가요? '}<span
                    className='auth-description-link'
                    onClick={onSignInLinkClickHandler}>{'로그인'}</span>
                </div>
              </div>
            </div>
          </div>


        </div>
    )
  }

  return (
      <div id='auth-wrapper'>
        {view === 'login' && <LoginInCard/>}
        {view == "sign-up" && <SignUpCard/>}
      </div>
  )
}