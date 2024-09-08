import React, { ChangeEvent, useEffect, useState } from 'react';
import './style.css';
import InputBox from "../../components/InputBox";
import { checkStreamStatus, startBroadCast } from "../../apis/broadcast";
import useLoginUserStore from "../../stores/login-user.store";
import { StartBroadcastRequest, StreamStatusRequestDto } from "../../types/request/broadcast";
import ResponseDto from "../../types/response/response.dto";
import StreamStatusResponseDto from "../../types/response/broadcast/stream-status-response.dto";
import { useNavigate } from "react-router-dom";
import { StartBroadcastResponse } from "../../types/response/broadcast";
import { AUTH_PATH, STREAM_PATH } from "../../constants/paths";
import {
  ATTRIBUTES,
  CATEGORIES,
  SERVER_URL, SERVER_URL_LABEL,
  STREAM_KEY_LABEL
} from "../../constants/broadcast/broadcastConstants";
import {message} from "antd";

export default function BroadCast() {
  const [title, setTitle] = useState<string>('');
  const [titleErrorMessage, setTitleErrorMessage] = useState<string>('');
  const [isTitleError, setIsTitleError] = useState<boolean>(false);
  const { loginUser } = useLoginUserStore();
  const [category, setCategory] = useState<string>('JAVA');
  const [attribute, setAttribute] = useState<string>('NORMAL');
  const navigate = useNavigate();

  useEffect(() => {
    if (!loginUser) {
      alert("로그인을 부탁드립니다.");
      navigate(AUTH_PATH());
    }
  }, [loginUser, navigate]);

  const onTitleChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
    setTitle(event.target.value);
  };

  const onCategoryChangeHandler = (event: ChangeEvent<HTMLSelectElement>) => {
    setCategory(event.target.value);
  };

  const onAttributeChangeHandler = (event: ChangeEvent<HTMLSelectElement>) => {
    setAttribute(event.target.value);
  };

  const validateForm = () => {
    if (title.length < 2 || title.length > 16) {
      setIsTitleError(true);
      setTitleErrorMessage('방송제목은 2자 이상 16자 이하이어야 합니다.');
      return false;
    }
    setIsTitleError(false);
    setTitleErrorMessage('');

    if (!category) {
      alert('카테고리를 선택하세요.');
      return false;
    }

    if (!loginUser) {
      alert("로그인을 부탁합니다.");
      return false;
    }

    return true;
  };

  const onStartBroadcastHandler = async () => {
    if (!validateForm()) return;
    if (!loginUser) return;

    const request: StreamStatusRequestDto = { streamKey: loginUser.streamKey };
    try {
      await checkStreamStatus(request).then(handleStreamStatusResponse);
    } catch (error) {
      alert("스트림 상태를 확인하는 중 오류가 발생했습니다.");
    }
  };

  const handleStreamStatusResponse = async (responseBody: StreamStatusResponseDto | ResponseDto | null) => {
    if (!responseBody) {
      alert("에러발생");
      return;
    }

    const { code, streamStatus } = responseBody as StreamStatusResponseDto;

    if (code !== 'SU' || streamStatus !== 'LIVE_ON') {
      message.error(streamStatus === 'LIVE_OFF' ? "현재 방송이 송출중이지 않습니다. OBS등을 확인해주세요" : "에러발생");
      return;
    }

    if (!loginUser) {
      message.error("로그인을 부탁합니다.");
      return;
    }

    const request: StartBroadcastRequest = {
      streamKey: loginUser.streamKey,
      streamerId: loginUser.userId,
      title,
      category,
      attribute,
      streamerNickname : loginUser.nickname
    };

    try {
      const broadcastResponse = await startBroadCast(request);
      handleStartBroadcastResponse(broadcastResponse);
    } catch (error) {
      alert("방송을 시작하는 중 오류가 발생했습니다.");
    }
  };

  const handleStartBroadcastResponse = (responseBody: ResponseDto | StartBroadcastResponse | null) => {
    if (!responseBody || !loginUser) {
      alert("에러입니다.");
      return;
    }

    const { code } = responseBody;

    if (code !== 'SU') {
      alert("에러입니다");
      return;
    }
    const broadcastId = (responseBody as StartBroadcastResponse).broadcastId;
    navigate(STREAM_PATH(loginUser.userId, broadcastId));
  };

  if (!loginUser) {
    return null;
  }

  return (
      <div className='broadcast-box'>
        <div className='broadcast-logo-box'>
          {'FeedBack'}
          <span className='broadcast-logo-part'>{'TV'}</span>
        </div>

        <div className='broadcast-card'>
          <InputBox
              label={'방송제목'}
              type={'text'}
              onChange={onTitleChangeHandler}
              error={isTitleError}
              placeholder={'방송제목을 입력하세요(2자 이상 16자 이하)'}
              value={title}
              message={titleErrorMessage}
          />

          <div className='input-group'>
            <label htmlFor='category'>카테고리</label>
            <select id='category' value={category} onChange={onCategoryChangeHandler}>
              <option value="">카테고리 선택</option>
              {CATEGORIES.map((cat) => (
                  <option key={cat.value} value={cat.value}>{cat.label}</option>
              ))}
            </select>
          </div>

          <div className='input-group'>
            <label htmlFor='attribute'>방송속성</label>
            <select id='attribute' value={attribute} onChange={onAttributeChangeHandler}>
              {ATTRIBUTES.map((attr) => (
                  <option key={attr.value} value={attr.value}>{attr.label}</option>
              ))}
            </select>
          </div>

          <div className='broadcast-info'>
            <div className='broadcast-url-info'>
              <div className='broadcast-url-label'>{SERVER_URL_LABEL}</div>
              <div className='broadcast-url-detail'>{SERVER_URL}</div>
            </div>

            <div className='broadcast-streamkey-info'>
              <div className='broadcast-streamkey-label'>{STREAM_KEY_LABEL}</div>
              <div className='broadcast-streamkey-detail'>{loginUser.streamKey}</div>
            </div>
          </div>

          <div className='start-broadcast-button-box' onClick={onStartBroadcastHandler}>
            <div className='start-broadcast-button'>{'방송 시작'}</div>
          </div>
        </div>
      </div>
  );
}