import './style.css';
import {ChangeEvent, KeyboardEvent, useCallback, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useCookies} from "react-cookie";
import {CANDY_BUY_PATH} from "../../constants/paths";
import {GetStarCandyResponseDto} from "../../types/response/member";
import ResponseDto from "../../types/response/response.dto";
import {getMemberCandyAmount} from "../../apis/member";


export default function DonationBox() {

  const navigate = useNavigate();
  const [candyCount,setCandyCount] = useState<number>(0);
  const [isCandyInsufficient, setIsCandyInsufficient] = useState<boolean>(false);


  const {donorId} = useParams();

  const {recipientId} = useParams();

  //     state : 쿠키 상태     //
  const [cookies, setCookie] = useCookies();

  const [nickname, setNickname] = useState<string>('');
  const [candyAmount, setCandyAmount] = useState<number>(0);

  const onBuyButtonClickHandler = () => {
    window.open(CANDY_BUY_PATH());
  }

  const onCancelButtonClickHandler = useCallback(() => {
    window.close(); // 현재 창 닫기 시도
  }, []);

  const onCandyCountChangeHandler = (event : ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    const number = parseInt(value, 10);

    if (!isNaN(number) && number > 0) {
      setCandyCount(number);  // 숫자이고 양수일 경우 상태 업데이트
    } else if (value === '') {
      setCandyCount(0);  // 입력란을 비웠을 경우 빈 문자열 허용
    }

    if (number > candyAmount) {
      setIsCandyInsufficient(true);
      setIsCandyInsufficient(number > candyAmount);
    }

  }

  const getMemberCandyAmountResponse = (responseBody : GetStarCandyResponseDto | ResponseDto |null) => {
    if(!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      alert("에러가 발생했습니다. 다시 시도해주세요")
      window.close();
      return;
    }

    const {nickname, starCandyAmount} = responseBody as GetStarCandyResponseDto;

    setNickname(nickname);

    if (starCandyAmount == null) {
      setCandyCount(0);
      return;
    }

    setCandyAmount(starCandyAmount);
  }


  useEffect(() => {
    if(!donorId || !recipientId) {
      console.log("에러");
      return;
    }
    getMemberCandyAmount(cookies.accessToken).then(getMemberCandyAmountResponse)
  }, []);

  return (
      <div className='donation-box'>
        <div className='donation-box-top'>
          <div className='donoation-box-recipient-info'>
            <div className='donation-box-recipient-nickname'>
              {`${nickname}님 선물`}
            </div>
          </div>
          <div className='donation-box-donate'>
            <span className='text-default'>{'선물할 별사탕'}</span>
            <input type="text" value={candyCount} onChange={onCandyCountChangeHandler} placeholder={'0'} autoComplete={'off'}/>
            <span className='text-count'>{'개'}</span>
          </div>
          <div className='donation-box-starcandy'>
            <div className='donation-box-starcandy-text'>
              {'보유한 별풍선'}
              <div className='donation-box-starcandy-amount'>
                {`${candyAmount}`}
                <span className='text'>{'개'}</span>
              </div>
              {isCandyInsufficient && <div style={{ color: 'red' }}>보유한 별풍선이 부족합니다</div>}
            </div>
            <div className='donation-box-buy-button' onClick={onBuyButtonClickHandler}>{'구매하기'}</div>
          </div>
        </div>
        {/*<div className='donation-box-middle'>*/}
        {/*  <div className='donation-box-message-label'>*/}
        {/*    {'메시지'}*/}
        {/*  </div>*/}
        {/*  <div className='donation-box-message-input'>*/}
        {/*    <input type='text' placeholder='메시지를 입력해주세요.'/>*/}
        {/*  </div>*/}
        {/*</div>*/}
        <div className='donation-box-bottom'>
          <div className='donation-box-donate-button'>
            {'선물하기'}
          </div>
          <div className='donation-box-donate-cancel' onClick={onCancelButtonClickHandler}>
            {'취소'}
          </div>
        </div>
      </div>
  );
};

