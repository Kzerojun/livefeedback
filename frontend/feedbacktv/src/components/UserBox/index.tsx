import defaultProfileImage from "../../assets/image/user-default-image.png";
import './style.css';
import User from "../../types/interface/user.interface";
import {Station} from "../../types/interface";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useRef, useState} from "react";
import useLoginUserStore from "../../stores/login-user.store";
import {AUTH_PATH, DONATION_PATH, MAIN_PATH} from "../../constants/paths";
import {GetIsFavoriteResponseDto} from "../../types/response/favorite";
import ResponseDto from "../../types/response/response.dto";
import {GetFanListResponseDto} from "../../types/response/donation";
import {getTop20FanListRequest} from "../../apis/donation";

interface Props {
  user: User;
  station: Station;
  fans: User[];
}

export default function UserBox({user, station, fans}: Props) {

  //function : 네비게이트 함수  //
  const navigate = useNavigate();

  const {streamerId} = useParams();

  const [showTop20Fans, setShowTop20Fans] = useState<boolean>(false);

  const popupRef = useRef<HTMLDivElement>(null); // 팝업 참조 생성

  const [isFavorite, setIsFavorite] = useState<boolean>(false);

  const {loginUser} = useLoginUserStore();

  // event handler : passionate detail 버튼 클릭 핸들러 //
  const onMoreButtonClickHandler =() => {
    setShowTop20Fans(!showTop20Fans);
  }

  const onLolliPopClickHandler = () => {

    if (!loginUser) {
      navigate(AUTH_PATH());
      return
    }

    if (!streamerId) {
      navigate(MAIN_PATH());
      return; // 이후 코드 실행을 중단
    }

    const donorId = loginUser?.userId; // 후원하는 사용자의 ID

    // URL 생성
    const donationUrl = DONATION_PATH(donorId, streamerId);

    // 새 창 옵션
    const windowFeatures = "menubar=no,location=no,resizable=yes,scrollbars=yes,status=yes,height=300,width=500";

    // 새 창을 해당 URL로 열기
    window.open(donationUrl, 'NewWindow', windowFeatures);
  };

  // function :
  const handleClickOutside = (event: MouseEvent) => {
    if (popupRef.current && !popupRef.current.contains(event.target as Node)) {
      setShowTop20Fans(false); // 팝업 참조의 외부를 클릭하면 팝업을 닫음
    }
  };

  const getIsFavoriteResponse =(responseBody : GetIsFavoriteResponseDto | ResponseDto |null) => {
    if(!responseBody) return;
    const {code} = responseBody;

    if (code === 'NEM') alert('존재하지 않는 유저입니다.');
    if (code === 'DBE') alert('데이터베이스 오류입니다.');

    if (code !== 'SU') {
      navigate(MAIN_PATH());
      return;
    }

    const{favorite} = responseBody as GetIsFavoriteResponseDto;
    setIsFavorite(favorite);
  }

  useEffect(() => {
    if(!streamerId) return;

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);


  const Top20FanCard = ()=>{

    const [top20Fans, setTop20Fans] = useState<User[]>([]);

    const getTop20FanListResponse= (responseBody: GetFanListResponseDto | ResponseDto | null) =>{
      if(!responseBody) return;
      const {code} = responseBody;

      if (code === 'NEM') alert('존재하지 않는 유저입니다.');
      if (code === 'DBE') alert('데이터베이스 오류입니다.');

      if (code !== 'SU') {
        navigate(MAIN_PATH());
        return;
      }

      const{fans} = responseBody as GetFanListResponseDto
      setTop20Fans(fans);
    }

    useEffect(() => {
      if(!user) return;
      getTop20FanListRequest(user.userId).then(getTop20FanListResponse);
    },[]);

    return(
        <div className='user-top-20-fan-card' ref ={popupRef}>
          <div className='user-box-passionate'>
            <div className='user-box-best-passionate'>{'TOP20 열혈팬'}</div>
            <div className='user-box-passionate-more-button'
                 onClick={onMoreButtonClickHandler}>{'닫기'}</div>
          </div>
          {top20Fans && top20Fans.length > 0 && (
              top20Fans.map(fanUser =>
                  <div className='user-box-passionate-fan-list'>
                    <div className='user-box-passionate-fan'>
                      <div className='user-box-profile-container'>
                        <div className='user-box-fan-profile-image-box'>
                          <div className='user-box-fan-profile-image'
                               style={{backgroundImage: `url(${fanUser.profileImage ? fanUser.profileImage : defaultProfileImage})`}}>
                          </div>
                        </div>
                        <div className='user-box-fan-info'>
                          <div className='user-box-fan-nickname'>{fanUser.nickname}</div>
                          <div className='user-box-fan-userId'>{fanUser.userId}</div>
                        </div>
                        <div className='user-box-descripton-box'>
                        </div>
                      </div>
                    </div>
                  </div>
              )
          )}
        </div>
    )
  }

  return (
      <div className='user-box'>
        <div className='user-box-main'>
          <div className='user-box-profile-box'>
            <div className='user-box-profile-container'>
              <div className='user-box-profile-image-box'>
                <div className='user-box-profile-image'
                     style={{backgroundImage: `url(${user.profileImage ? user.profileImage : defaultProfileImage})`}}>
                </div>
              </div>
              <div className='user-box-info'>
                <div className='user-box-nickname'>{user.nickname}</div>
                <div className='user-box-loginId'>{user.userId}</div>
              </div>
            </div>

              {loginUser?.userId !== streamerId &&
                  <div className='user-donation-option'>
                    <div className='user-lollipop-box' onClick={onLolliPopClickHandler}>
                      <div className='icon lollipop'/>
                    </div>
                    <div className='user-favorites-box'>
                      <div className={`icon ${isFavorite ? 'favorites-on' : 'favorites-off'}`}/>
                    </div>
                    <div className='user-alarm-box'>
                      <div className='icon alarm off'/>
                    </div>
                  </div>
              }

            <div className='user-stream-status'>
              <div className='user-stream-status-close'>{'오프라인'}</div>
            </div>
            <div className='divider'></div>

            <div className='user-box-follower'>
              <div className='user-box-passionate'>
                <div className='user-box-best-passionate'>{'열혈팬'}</div>
                <div className='user-box-passionate-more-button' onClick={onMoreButtonClickHandler}>{'자세히'}</div>
              </div>
              {fans &&fans.length > 0 &&!showTop20Fans && (
                  fans.map(fanUser =>
                      <div className='user-box-passionate-fan-list'>
                        <div className='user-box-passionate-fan'>
                          <div className='user-box-profile-container'>
                            <div className='user-box-fan-profile-image-box'>
                              <div className='user-box-fan-profile-image'
                                   style={{backgroundImage: `url(${fanUser.profileImage ? fanUser.profileImage : defaultProfileImage})`}}>
                              </div>
                            </div>
                            <div className='user-box-fan-info'>
                              <div className='user-box-fan-nickname'>{fanUser.nickname}</div>
                              <div className='user-box-fan-userId'>{fanUser.userId}</div>
                            </div>
                          </div>
                        </div>
                      </div>
                  )
              )}
            </div>
            {showTop20Fans && (
                <Top20FanCard/>
            )}
          </div>
        </div>
      </div>
  )
};